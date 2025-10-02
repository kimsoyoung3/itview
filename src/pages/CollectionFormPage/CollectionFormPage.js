import {useEffect, useRef, useState} from 'react';
import "./CollectionFormPage.css"
import 'bootstrap/dist/css/bootstrap.min.css';
import { toast } from 'react-toastify';
import { CollectionCreate, editCollection, getCollectionForm, getCollectionItems } from '../../API/CollectionApi';
import { searchContent } from '../../API/ContentApi';
import { useNavigate, useParams } from 'react-router-dom';

const CollectionFormPage = ({action}) => {
    /* 모달 상태 */
    const [collectionAddModal, setCollectionAddModal] = useState(false);
    const openCollectionAddModal = () => setCollectionAddModal(true);
    const closeCollectionAddModal = () => { setCollectionAddModal(false); setKeyword(""); }

    /* 제목, 설명 ref */
    const title = useRef();
    const description = useRef();

    /* 검색 관련 상태 */
    const [keyword, setKeyword] = useState("");
    const [debounceQuery, setDebounceQuery] = useState(keyword);
    const [searchResults, setSearchResults] = useState(null);

    const { id } = useParams();
    const navigation = useNavigate();

    /* 컬렉션 아이템 ID 리스트 및 선택된 아이템 */
    const [collectionItemIds, setCollectionItemIds] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const [tempItems, setTempItems] = useState([]);

    /* 컬렉션 아이템 페이지 정보 */
    const [collectionItemsPage, setCollectionItemsPage] = useState({});

    useEffect(() => {
        console.log(collectionItemIds);
    }, [collectionItemIds]);

    /* 컬렉션 수정 시 기존 데이터 불러오기 */
    useEffect(() => {
        const fetchCollectionDetail = async () => {
            try {
                const res = await getCollectionForm(id);
                console.log(res.data);
                title.current.value = res.data.title;
                description.current.value = res.data.description;
                setCollectionItemIds(res.data.contentId);
                const contentRes = await getCollectionItems(id, 1);
                setSelectedItems(contentRes.data.content);
                setCollectionItemsPage(contentRes.data.page);
            } catch (error) {
                toast("컬렉션 정보를 불러오는데 실패했습니다.");
            }
        };
        if(action === "edit" && id) {
            fetchCollectionDetail();
        }
    }, [action, id]);

    /* 디바운스 */
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebounceQuery(keyword);
        }, 500);

        return () => {
            clearTimeout(handler);
        };
    }, [keyword]);

    /* 검색 결과 가져오기 */
    useEffect(() => {
        const fetchSearchResults = async () => {
            if(debounceQuery){
                try {
                    const res = await searchContent(debounceQuery, 1);
                    setSearchResults(res.data);
                } catch (error) {
                    toast("작품 검색에 실패했습니다.");
                    return;
                }
            } else {
                setSearchResults(null);
            }
        }
        fetchSearchResults();
    }, [debounceQuery]);

    useEffect(() => {
        console.log(searchResults);
    }, [searchResults]);

    useEffect(() => {
        console.log(selectedItems);
    }, [selectedItems]);

    useEffect(() => {
        console.log(tempItems);
    }, [tempItems]);

    /* 아이템 더보기 */
    const LoadMoreItemRef = useRef();

    const loadMoreItems = async () => {
        if (collectionItemsPage && collectionItemsPage.number + 1 < collectionItemsPage.totalPages) {
            try {
                const res = await getCollectionItems(id, collectionItemsPage.number + 2);
                setSelectedItems([...selectedItems, ...res.data.content]);
                setCollectionItemsPage(res.data.page);
            } catch (error) {
                toast("아이템을 불러오는데 실패했습니다.");
                return;
            }
        }
    };

    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                loadMoreItems();
            }
        }, {
            threshold: 0.1
        });
        if (LoadMoreItemRef.current) {
            observer.observe(LoadMoreItemRef.current);
        }
        return () => {
            if (LoadMoreItemRef.current) {
                observer.unobserve(LoadMoreItemRef.current);
            }
        };
    }, [collectionAddModal, selectedItems]);

    /* 체크박스 핸들러 */
    const handleCheck = async (item) => {
        if (tempItems.some(i => i.id === item.id)) {
            setTempItems(tempItems.filter(i => i.id !== item.id));
        } else {
            setTempItems([...tempItems, item]);
        }
    };

    /* 선택 아이템 추가 */
    const handleAddItems = async () => {
        setSelectedItems([...tempItems.filter(item => !selectedItems.some(i => i.id === item.id)), ...selectedItems]);
        setCollectionItemIds([...tempItems.map(item => item.id), ...collectionItemIds]);
        setTempItems([]);
        closeCollectionAddModal();
        setKeyword("");
        setSearchResults(null);
    }

    /* 아이템 수정 상태 */
    const [edit, setEdit] = useState(false);

    const handleEditItems = async () => {
        if (!edit) {
            setEdit(true);
            setTempItems([]);
        } else {
            setEdit(false);
            setSelectedItems(prev => prev.filter(item => !tempItems.some(i => i.id === item.id)));
            setCollectionItemIds(prev => prev.filter(id => !tempItems.some(i => i.id === id)));
            setTempItems([]);
        }
    }


    const handleEditCancel = async () => {
        setEdit(false);
        setTempItems([]);
    }

    /* 검색 더보기 */
    const loadMoreSearchRef = useRef();

    const loadMore = async () => {
        if (searchResults && searchResults.page.number + 1 < searchResults.page.totalPages) {
            try {
                const res = await searchContent(debounceQuery, searchResults.page.number + 2);
                setSearchResults({
                    ...searchResults,
                    content: [...searchResults.content, ...res.data.content],
                    page: res.data.page
                });
            } catch (error) {
                toast("정보를 불러오는데 실패했습니다.");
                return;
            }
        }
    }

    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                loadMore();
            }
        }, {
            threshold: 0.1
        });
        if (loadMoreSearchRef.current) {
            observer.observe(loadMoreSearchRef.current);
        }
        return () => {
            if (loadMoreSearchRef.current) {
                observer.unobserve(loadMoreSearchRef.current);
            }
        };
    }, [collectionAddModal, searchResults]);

    /* 컬렉션 제출 */
    const handleSubmitCollection = async () => {
        if(title.current.value === ""){
            toast("컬렉션 제목을 입력해주세요");
            return;
        }
        const data = {
            title: title.current.value,
            description: description.current.value,
            contentId: selectedItems.map(item => item.id)
        };
        if (action === "new") {
            try {
                const res = await CollectionCreate(data);
                toast("컬렉션이 생성되었습니다.");
                navigation(`/collection/${res.data}`);
            } catch (error) {
                toast("컬렉션 생성에 실패했습니다.");
                return;
            }
        } else if (action === "edit") {
            try {
                await editCollection(id, data);
                toast("컬렉션이 수정되었습니다.");
                navigation(`/collection/${id}`);
            } catch (error) {
                toast("컬렉션 수정에 실패했습니다.");
                return;
            }
        }
    }

    /* 도메인 이름 매핑 */
    const domainNameMap = {
        "MOVIE" : "영화" ,
        "SERIES" : "시리즈",
        "BOOK" : "책",
        "WEBTOON" : "웹툰",
        "RECORD" : "음반"
    }

    return (
        <div className="user-collection-add-page container">
            <div className="user-collection-add-page-wrap">
                <h1>{action === "new" ? "새 컬렉션" : "컬렉션 수정"}</h1>

                <div className="user-collection-add-btn-box">
                    <button className="user-collection-add-btn" onClick={handleSubmitCollection}>{action === "new" ? "만들기" : "수정하기"}</button>
                </div>

                <input type="text" placeholder="컬렉션 제목을 입력해주세요" className="user-collection-add-title" maxLength={50} ref={title}/>

                <textarea placeholder="컬렉션 설명을 입력해주세요" rows={5} className="user-collection-add-text" maxLength={200} ref={description}/>

                {/*이미지 추가*/}
                <div className="user-collection-add-image-wrap">
                    <div className="user-collection-add-image-title">
                        <p>작품들</p>
                        <div className="user-collection-add-image-title-btn">
                            {edit &&(
                                <button className="user-collection-add-image-title-prev-btn" onClick={handleEditCancel}>취소</button>
                            )}

                            {selectedItems?.length > 0 &&(
                                <button onClick={handleEditItems}>{edit ? `${tempItems.length}개 삭제` : "수정하기"}</button>
                            )}
                        </div>
                    </div>

                    <div className="user-collection-add-image-list">
                        <div className="user-collection-add-image-box">
                            <button className="user-collection-add-image-btn" onClick={() => {openCollectionAddModal(); setTempItems([]); setEdit(false);}}>
                                <i className="bi bi-plus-lg"></i>
                                <p>작품추가</p>
                            </button>
                            <p>아무것도 없음</p>
                        </div>

                        {selectedItems?.length > 0 && (selectedItems?.map((item, index) =>
                            <div key={item.id} className="form-check user-collection-add-image-map">
                                {edit &&(
                                    <input className="form-check-input add-image-input " type="checkbox" value="" id={`add-image-input-${index}`} onChange={() => handleCheck(item)} checked={tempItems.some(i => i.id === item.id)}/>
                                )}

                                <label className="form-check-label user-collection-add-image" htmlFor={`add-image-input-${index}`}>
                                    <img src={item.poster} alt=""/>
                                    <p>{item.title}</p>
                                </label>
                            </div>
                        ))}
                        <div ref={LoadMoreItemRef} hidden={action === "new" || (collectionItemsPage && collectionItemsPage.number + 2 > collectionItemsPage.totalPages)}></div>
                    </div>
                </div>
            </div>

            {/*컬렉션 이미지 추가 모달창*/}
            {collectionAddModal && (
                <div className="collection-add-modal-overlay" onClick={closeCollectionAddModal}>
                    <div className="collection-add-modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="collection-add-modal-header">
                            <button className="collection-add-modal-close-button" onClick={closeCollectionAddModal}>
                                <i className="bi bi-x-lg"></i>
                            </button>

                            <div className="collection-add-modal-content-top">
                                <p className="collection-add-modal-title">작품 추가</p>
                                <button className={`collection-add-modal-content-btn ${tempItems?.length > 0 ? 'has-items' : ''}`} onClick={handleAddItems}>
                                    {tempItems?.length > 0 && `${tempItems?.length}개`} 추가
                                </button>
                            </div>

                            <div className="collection-add-modal-content-middle">
                                <div className="collection-add-modal-search-bar">
                                    <button className="collection-add-modal-search-button"><i className="bi bi-search"/></button>
                                    <input
                                        type="text"
                                        placeholder="검색하여 작품 추가하기"
                                        className="collection-add-modal-search-input"
                                        onChange={(e) => setKeyword(e.target.value)}
                                        value={keyword}
                                    />
                                </div>
                            </div>
                        </div>

                        <div className="collection-add-modal-content-bottom">
                            {searchResults?.content?.map((item, index) =>
                                <div className="form-check" key={index}>
                                    <input className="form-check-input" type="checkbox" value="" id={`checkDefault-${index}`} onChange={() => handleCheck(item)} checked={tempItems.some(i => i.id === item.id) || collectionItemIds.some(i => i === item.id)} disabled={collectionItemIds.some(i => i === item.id)}/>
                                    <label className="form-check-label" htmlFor={`checkDefault-${index}`}>
                                        <div className="collection-add-modal-search-results">
                                            <div className="collection-add-modal-search-results-img">
                                                <img src={item.poster ? item.poster : `${process.env.PUBLIC_URL}/basic-bg.jpg`} alt=""/>
                                            </div>
                                            <div className="collection-add-modal-search-results-info">
                                                <p className="collection-add-modal-search-results-info-top">{item.title}</p>
                                                <p className="collection-add-modal-search-results-info-bottom"><span>{domainNameMap[item.contentType]} &middot; </span><span>{(new Date(item.releaseDate).getFullYear())}</span></p>
                                            </div>
                                        </div>
                                    </label>
                                </div>
                            )}
                            <div ref={loadMoreSearchRef} hidden={searchResults === null || searchResults.page.number + 2 > searchResults.page.totalPages}></div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CollectionFormPage;