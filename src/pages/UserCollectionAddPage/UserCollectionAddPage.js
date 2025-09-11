import {React, use, useEffect, useRef, useState} from 'react';
import "./UserCollectionAddPage.css"
import 'bootstrap/dist/css/bootstrap.min.css';
import { toast } from 'react-toastify';
import { CollectionCreate } from '../../API/CollectionApi';
import { searchContent } from '../../API/ContentApi';
import ContentEach from "../../components/ContentEach/ContentEach";

const UserCollectionAddPage = () => {
    const [collectionAddModal, setCollectionAddModal] = useState()

    const openCollectionAddModal = () => setCollectionAddModal(true)
    const closeCollectionAddModal = () => setCollectionAddModal(false)

    const title = useRef();
    const description = useRef();

    const [keyword, setKeyword] = useState("");
    const [debounceQuery, setDebounceQuery] = useState(keyword);
    const [searchResults, setSearchResults] = useState({});

    useEffect(() => {
        const handler = setTimeout(() => {
            setDebounceQuery(keyword);
        }, 500);

        return () => {
            clearTimeout(handler);
        };
    }, [keyword]);

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
                setSearchResults({});
            }
        }
        fetchSearchResults();
    }, [debounceQuery]);

    useEffect(() => {
        console.log(searchResults);
    }, [searchResults]);

    const [selectedItems, setSelectedItems] = useState([]);

    useEffect(() => {
        console.log(selectedItems);
    }, [selectedItems]);

    const [tempItems, setTempItems] = useState([]);

    useEffect(() => {
        console.log(tempItems);
    }, [tempItems]);

    const handleCheck = async (item) => {
        if (tempItems.some(i => i.id === item.id)) {
            setTempItems(tempItems.filter(i => i.id !== item.id));
        } else {
            setTempItems([...tempItems, item]);
        }
    };

    const handleAddItems = async () => {
        setSelectedItems([...selectedItems, ...tempItems.filter(item => !selectedItems.some(i => i.id === item.id))]);
        setTempItems([]);
        closeCollectionAddModal();
        setKeyword("");
        setSearchResults({});
    }

    const [edit, setEdit] = useState(false);

    const handleEditItems = async () => {
        if (!edit) {
            setEdit(true);
            setTempItems([]);
        } else {
            setEdit(false);
            setSelectedItems(prev => prev.filter(item => !tempItems.some(i => i.id === item.id)));
            setTempItems([]);
        }
    }

    const handleDeleteItems = async (item) => {
        setTempItems(prev => ([...prev,  item]));
    }

    const handleEditCancel = async () => {
        setEdit(false);
        setTempItems([]);
    }

    const handleCreateCollection = async () => {
        if(title.current.value === ""){
            toast("컬렉션 제목을 입력해주세요");
            return;
        }
        if(description.current.value === ""){
            toast("컬렉션 설명을 입력해주세요");
            return;
        }
        const data = {
            title: title.current.value,
            description: description.current.value
        }
        try {
            await CollectionCreate(data);
            toast("컬렉션이 생성되었습니다.");
        } catch (error) {
            toast("컬렉션 생성에 실패했습니다.");
            return;
        }
    }

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
                <h1>새 컬렉션</h1>

                <div className="user-collection-add-btn-box">
                    <button className="user-collection-add-btn" onClick={handleCreateCollection}>만들기</button>
                </div>

                <input type="text" placeholder="컬렉션 제목을 입력해주세요" className="user-collection-add-title" maxLength={50} ref={title}/>

                <textarea placeholder="컬렉션 설명을 입력해주세요" rows={5} className="user-collection-add-text" maxLength={200} ref={description}/>

                <div className="user-collection-add-image-wrap">
                    <div className="user-collection-add-image-title">
                        <p>작품들</p>
                        {selectedItems?.length > 0 &&(
                            <span onClick={handleEditItems}>수정하기</span>
                        )}
                    </div>

                    <div className="user-collection-add-image-list">
                        <div className="user-collection-add-image-box">
                            <button className="user-collection-add-image-btn" onClick={openCollectionAddModal}>
                                <i className="bi bi-plus-lg"></i>
                                <p>작품추가</p>
                            </button>
                            <p>아무것도 없음</p>
                        </div>
                        {selectedItems?.length > 0 && (selectedItems?.map(item =>
                            <div className="user-collection-add-image">
                                <img src={item.poster} alt=""/>
                                <p>{item.title}</p>
                                <button hidden={!edit} onClick={() => handleDeleteItems(item)}>test</button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/*컬렉션 이미지 추가 모달창*/}
            {collectionAddModal && (
                <div className="collection-add-modal-overlay" onClick={closeCollectionAddModal}>
                    <div className="collection-add-modal-content" onClick={(e) => e.stopPropagation()}>
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

                        <div className="collection-add-modal-content-bottom">
                            {searchResults?.content?.map((item, index) =>
                                <div className="form-check" key={index}>
                                    <input className="form-check-input" type="checkbox" value="" id={`checkDefault-${index}`} onChange={() => handleCheck(item)} checked={tempItems.some(i => i.id === item.id)} disabled={selectedItems.some(i => i.id === item.id)}/>
                                    <label className="form-check-label" htmlFor={`checkDefault-${index}`}>
                                        <div className="collection-add-modal-search-results">
                                            <div className="collection-add-modal-search-results-img">
                                                <img src={item.poster ? item.poster : "/basic-bg.jpg"} alt=""/>
                                            </div>
                                            <div className="collection-add-modal-search-results-info">
                                                <p className="collection-add-modal-search-results-info-top">{item.title}</p>
                                                <p className="collection-add-modal-search-results-info-bottom"><span>{domainNameMap[item.contentType]} &middot; </span><span>{(new Date(item.releaseDate).getFullYear())}</span></p>
                                            </div>
                                        </div>
                                    </label>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserCollectionAddPage;