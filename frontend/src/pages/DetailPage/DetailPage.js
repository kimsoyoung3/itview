import React, {useEffect, useRef, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./DetailPage.css";
import "../../components/ContentSwiper/ContentSwiper.css";
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css';
import 'swiper/css/grid';
import { getContentCredit, deleteRating, getContentComment, getContentDetail, postContentComment, postContentRating, unwishContent, wishContent } from "../../API/ContentApi";
import {Navigation, Pagination} from "swiper/modules";
import {Swiper, SwiperSlide} from "swiper/react";
import CreditOrPersonCard from "../../components/CreditOrPersonCard/CreditOrPersonCard";
import CommentCard from "../../components/CommentCard/CommentCard";
import {NavLink, useParams} from "react-router-dom";
import { deleteComment, updateComment } from "../../API/CommentApi";
import {toast} from "react-toastify";
import NotFound from "../NotFound/NotFound";
import { addCollectionItem, getCollectionToAdd } from "../../API/CollectionApi";

const DetailPage = ({userInfo, openLogin}) => {
    /* notFound 상태 */
    const [notFound, setNotFound] = useState(false);

    /* URL에서 :id 가져오기 */
    const { id } = useParams();

    /* 컨텐츠 상태 */
    const [contentDetail, setContentDetail] = useState(null);
    const [contentCredit, setContentCredit] = useState(null);
    const [chunkedContentCredit, setChunkedcontentCredit] = useState([]);

    /* chunkedContentCredit 확인 */
    useEffect(() => {
        console.log(chunkedContentCredit)
    }, [chunkedContentCredit]);

    /* 점수 상태 */
    const [score, setScore] = useState(0);
    /* 마우스 올릴 때 임시 점수 */
    const [hoverScore, setHoverScore] = useState(0);

    /* 코멘트 텍스트 영역 참조 */
    const textRef = React.useRef(null);

    /* 코멘트 모달 상태 */
    const [myCommentModal, setMyCommentModal] = useState();
    const openMyComment = () => {
        setMyCommentModal(true);
        document.body.style.overflow = 'hidden';
    };
    const closeMyComment = () => {
        setMyCommentModal(false);
        document.body.style.overflow = 'auto';
    };

    /* 코멘트 작성 */
    const handleCommentPost = async () => {
        try {
            await postContentComment(id, { text: textRef.current.value })
            closeMyComment();
            getContentComment(id).then(response => {
                setContentDetail(prev => ({
                    ...prev,
                    myComment: response.data
                }));
            });
            toast("코멘트가 등록되었습니다.");
        } catch (error) {
            toast(error.response.data);
        }
    };

    /* 코멘트 수정 */
    const handleCommentUpdate = async () => {
        try {
            const res = await updateComment(contentDetail?.myComment.id, { text: textRef.current.value })
            if (res.status === 200) {
                closeMyComment();
                getContentComment(id).then(response => {
                    setContentDetail(prev => ({
                        ...prev,
                        myComment: response.data
                    }));
                });
            }
            toast("코멘트가 수정되었습니다.");
        } catch (error) {
            toast(error.response.data);
        }
    };

    /* 코멘트 제출 */
    const handleMyCommentSubmit = () => {
        if (contentDetail?.myComment) {
            handleCommentUpdate();
        } else {
            handleCommentPost();
        }
        closeMyComment();
    };

    /* 마이코멘트 삭제 확인 모달창 */
    const [deleteConfirmModal, setDeleteConfirmModal] = useState(false);

    const handleDeleteClick = () => {
        setDeleteConfirmModal(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            await deleteComment(contentDetail?.myComment.id);
            setDeleteConfirmModal(false);
            getContentComment(id).then(response => {
                setContentDetail(prev => ({
                    ...prev,
                    myComment: response.data
                }));
            });
        } catch (error) {
            console.error("삭제 중 오류:", error);
        }
    };

    /* 별점 삭제 로직 구현 */
    const handleScoreDelete = () => {
        deleteRating(id).then(response => {
            console.log('Rating deleted:', response.status);
        });
    };

    /* 보고싶어요 로직 구현 */
    const handleWish = async () => {
        if (contentDetail?.wishlistCheck) {
            try {
                await unwishContent(contentDetail.contentInfo.id);
                setContentDetail(prev => ({
                    ...prev,
                    wishlistCheck: false
                }));
                toast("위시리스트에서 제거되었습니다.");
            } catch (error) {
                toast(error.response.data);
            }
        } else {
            try {
                await wishContent(contentDetail.contentInfo.id)
                setContentDetail(prev => ({
                    ...prev,
                    wishlistCheck: true
                }));
                toast("위시리스트에 추가되었습니다.");
            } catch (error) {
                toast(error.response.data);
            }
        }
    };

    /* 컬렉션 관련 상태 */
    const [collectionList, setCollectionList] = useState(null);
    useEffect(() => {
        console.log(collectionList);
    }, [collectionList]);

    const [collectionListModal, setCollectionListModal] = useState();
    const openCollectionListModal = () => {
        setCollectionListModal(true);
        document.body.style.overflow = 'hidden';
    };
    const closeCollectionListModal = () => {
        setCollectionListModal(false);
        document.body.style.overflow = 'auto';
    };

    const [selectedCollections, setSelectedCollections] = useState([]);
    useEffect(() => {
        console.log(selectedCollections);
    }, [selectedCollections]);

    const handleCollectionSelect = async (collectionId) => {
        if (selectedCollections.some(id => id === collectionId)) {
            setSelectedCollections(prev => prev.filter(id => id !== collectionId));
        } else {
            setSelectedCollections(prev => [...prev, collectionId]);
        }
    };

    /* 컬렉션 추가 */
    const handleCollectionClick = async () => {
        openCollectionListModal();
        setSelectedCollections([]);
        try {
            const response = await getCollectionToAdd(id, 1);
            setCollectionList(response.data);
        } catch (error) {
            toast("컬렉션 정보를 불러오지 못했습니다.");
        }
    };

    const handleCollectionAdd = async () => {
        if (selectedCollections.length === 0) {
            toast("하나 이상의 컬렉션을 선택해주세요.");
            return;
        }
        try {
            await addCollectionItem(id, selectedCollections);
            toast("컬렉션에 추가되었습니다.");
            closeCollectionListModal();
        } catch (error) {
            toast("컬렉션에 추가하지 못했습니다.");
        }
    };

    const collectionListRef = useRef(null);
    const fetchNextPage = async () => {
        try {
            const nextPage = collectionList.page.number + 2;
            const response = await getCollectionToAdd(id, nextPage);
            setCollectionList(prev => ({content: [...prev.content, ...response.data.content], page: response.data.page}));
        } catch (error) {
            console.error('Error fetching next collection page:', error);
        }
    };

    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                if (collectionList?.page.number + 1 < collectionList?.page.totalPages) {
                    fetchNextPage();
                }
            }
        });
        if (collectionListRef.current) {
            observer.observe(collectionListRef.current);
        }
        return () => {
            if (collectionListRef.current) {
                observer.unobserve(collectionListRef.current);
            }
        };
    }, [collectionListRef, collectionList, contentDetail]);

    /* 외부서비스 로고 */
    const serviceLogos = {
        NETFLIX: `${process.env.PUBLIC_URL}/externalLogo/netflix.png`,
        DISNEY_PLUS: `${process.env.PUBLIC_URL}/externalLogo/disney.png`,
        WAVVE: `${process.env.PUBLIC_URL}/externalLogo/wavve.png`,
        WATCHA: `${process.env.PUBLIC_URL}/externalLogo/watcha.png`,
        TVING: `${process.env.PUBLIC_URL}/externalLogo/tving.png`,
        TVN: `${process.env.PUBLIC_URL}/externalLogo/tvn.png`,
        APPLE_TV_PLUS: `${process.env.PUBLIC_URL}/externalLogo/apple-tv-plus.png`,
        COUPANG_PLAY: `${process.env.PUBLIC_URL}/externalLogo/coupang-play.png`,
        ALADIN: `${process.env.PUBLIC_URL}/externalLogo/aladin.png`,
        YES24: `${process.env.PUBLIC_URL}/externalLogo/yes24.png`,
        KYOBO: `${process.env.PUBLIC_URL}/externalLogo/kyobo.png`,
        NAVER: `${process.env.PUBLIC_URL}/externalLogo/naverWebtoon.png`,
        KAKAO: `${process.env.PUBLIC_URL}/externalLogo/kakaoWebtoon.png`,
    };

    /* 갤러리 모달창 */
    const [galleryModal, setGalleryModal] = useState(false);
    /* 모달 슬라이드 시작 인덱스 */
    const [galleryModalStartIndex, setGalleryModalStartIndex] = useState(0);
    const openGalleryModal = (index) => {
        setGalleryModalStartIndex(index);
        setGalleryModal(true);
        document.body.style.overflow = 'hidden';
    };
    const closeGalleryModal = () => {
        setGalleryModal(false);
        document.body.style.overflow = 'auto';
    };

    /* 컨텐츠 디테일 가져오기 */
    useEffect(() => {
        const fetchContentDetail = async () => {
            try {
                const response = await getContentDetail(id);
                setContentDetail(response.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchContentDetail();
    }, [id]);

    /* 컨텐츠 크레딧 가져오기 */
    useEffect(() => {
        const fetchContentCredit = async () => {
            try {
                const response = await getContentCredit(id, 1);
                setContentCredit(response.data);
            } catch (error) {
                console.error('Error fetching content credit:', error);
            }
        };
        fetchContentCredit();
    }, [id]);

    /* 스와이퍼 레퍼런스 */
    const swiperRef = useRef(null);
    const loadMoreRef = useRef(null);

    /* 컨텐츠 크레딧 페이지 더보기 */
    const handleNextPage = async () => {
        if (contentCredit.page.number + 2 <= contentCredit.page.totalPages) {
            const fetchNextPage = async () => {
                try {
                    const nextPage = contentCredit.page.number + 2;
                    const response = await getContentCredit(id, nextPage);
                    setContentCredit(prev => ({content: [...prev.content, ...response.data.content], page: response.data.page}));
                } catch (error) {
                    console.error('Error fetching next credit page:', error);
                }
            };
            fetchNextPage();
        }
    };

    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                handleNextPage();
            }
        });
        if (loadMoreRef.current) {
            observer.observe(loadMoreRef.current);
        }
        return () => {
            if (loadMoreRef.current) {
                observer.unobserve(loadMoreRef.current);
            }
        };
    }, [loadMoreRef, contentCredit, handleNextPage]);

    /* 컨텐츠 정보를 가져온 후 */
    useEffect(() => {
        console.log(contentDetail);
        setScore(contentDetail?.myRating || 0);

        if (contentDetail && contentDetail.ratingDistribution.some(rating => rating.height === undefined)) {
            const max = Math.max(...contentDetail.ratingDistribution.map(rating => rating.scoreCount));

            contentDetail.ratingDistribution = contentDetail.ratingDistribution.map(rating => ({
                ...rating,
                isMax: rating.scoreCount === max
            }));

            const updatedDistribution = contentDetail.ratingDistribution.map(rating => ({
                ...rating,
                height: (rating.scoreCount / max) * 100
            }));

            setContentDetail(prev => ({
                ...prev,
                ratingDistribution: updatedDistribution
            }));
        }
    }, [contentDetail]);

    /* 코멘트 초기값 설정 */
    useEffect(() => {
        if (contentDetail?.myComment && textRef.current) {
            textRef.current.value = contentDetail.myComment.text;
        }
    }, [contentDetail, myCommentModal]);

    /* 컨텐츠 크레딧 청크 처리 */
    useEffect(() => {
        console.log(contentCredit)
        const chunked = [];
        for (let i = 0; i < contentCredit?.content.length; i += 3) {
            chunked.push(contentCredit.content.slice(i, i+3))
        }
        setChunkedcontentCredit(chunked)
    }, [contentCredit]);


    return (notFound ? <NotFound /> :
        <div className="detail">
            {/*상세페이지 배너 섹션*/}
            <section className="detail-banner">
                {/*상세페이지 포스터*/}
                <div className="banner-post">
                    <img src={contentDetail?.gallery && contentDetail?.gallery.length > 0 ? contentDetail?.gallery[0].imageUrl : `${process.env.PUBLIC_URL}/basic-bg.jpg`} alt=""/>
                </div>

                {/*포스터 설명*/}
                <div className="post-dec container">
                    <ul key={contentDetail?.contentInfo.id}>
                        <li>{contentDetail?.contentInfo.title}</li>
                        <li><span>{contentDetail?.contentInfo.releaseDate}</span> &middot; <span>{contentDetail?.contentInfo.genres?.map(genre => genre).join('/')}</span> &middot; <span>{contentDetail?.contentInfo.nation}</span></li>
                        <li><span>{contentDetail?.contentInfo.duration}</span> &middot; <span>{contentDetail?.contentInfo.age}</span></li>
                    </ul>
                </div>
            </section>

            {/*컨텐츠 정보 및 평가 섹션*/}
            <section className="detail-content detail-content-bg">
                <div className="detail-content-inner container">
                    {/*컨텐츠 포스터*/}
                    <div className="detail-content-inner-poster">
                        <div className="poster-top">
                            <img src={contentDetail?.contentInfo.poster} alt=""/>
                        </div>
                        <ul className="poster-bottom">
                            <li>별점 그래프</li>
                            <li>평균 <i className="bi bi-star-fill"></i> {(contentDetail?.contentInfo?.ratingAvg / 2).toFixed(1)}
                                <span className="poster-bottom-num"> &#40;{contentDetail?.ratingCount}명&#41;</span>
                            </li>
                            {/*별점 그래프 들어갈 자리*/}
                            {/* 별점 그래프 */}
                            <li className="rating-graph">
                                {
                                    contentDetail?.ratingDistribution?.map(
                                        (rating, index) => (
                                            <div key={index} className="rating-graph-wrap">
                                                <p className={rating.isMax ? "rating-graph-each rating-graph-each-max" : "rating-graph-each"} style={{height:`${rating.height*0.8}px`}}></p>
                                                <p className="rating-graph-text">{rating.score/2}</p>
                                            </div>
                                        )
                                    )
                                }
                            </li>

                        </ul>
                    </div>
                    {/*컨텐츠 정보*/}
                    <div className="detail-content-inner-info">
                        <div className="info-top">
                            {/* 별점체크 */}
                            <div className="info-top-left">
                                {[...Array(5)].map((_, i) => (
                                    <span
                                        key={i}
                                        className="star"
                                        onMouseMove={(e) => {
                                            const rect = e.currentTarget.getBoundingClientRect();
                                            const isHalf = e.clientX - rect.left < rect.width / 2;
                                            setHoverScore(isHalf ? (i * 2 + 1) : (i + 1) * 2);
                                        }}
                                        onMouseLeave={() => setHoverScore(0)}
                                        onClick={userInfo ?
                                            async () => {
                                                const newScore = hoverScore === score ? 0 : hoverScore;
                                                setScore(newScore);
                                                setContentDetail(prev => ({
                                                    ...prev,
                                                    myRating: newScore
                                                }));
                                                if (newScore === 0) {
                                                    await handleScoreDelete();
                                                } else {
                                                    const data = { score: Number(newScore) };
                                                    await postContentRating(id, data);
                                                }
                                            }
                                            :
                                            () => openLogin()
                                        }>
                                        {/*기본 별*/}
                                        <i className="bi bi-star-fill star-base"></i>
                                        {/*반 별*/}
                                        <i className="bi bi-star-fill star-fill" style={{width: (i + 1) * 2 <= (hoverScore || score) ? "100%" : i * 2 + 1 === (hoverScore || score) ? "50%" : "0%",}}></i>
                                    </span>
                                ))}
                                <p className="star-text">평가하기</p>
                            </div>

                            {/*평균 별점 표시*/}
                            <div className="info-top-center">
                                <p className="average-rating">{(contentDetail?.contentInfo?.ratingAvg / 2).toFixed(1)}</p>
                                <p className="average-rating-text">평균 별점 <span className="average-rating-num">&#40;{contentDetail?.ratingCount}명&#41;</span></p>
                            </div>

                            {/*위시,컬렉션 추가 및 코멘트 달기*/}
                            <ul className="info-top-right">
                                <li>
                                    <button onClick={async () => {
                                        userInfo ? handleWish() : openLogin();
                                    }}>
                                        <img src={contentDetail?.wishlistCheck ? `${process.env.PUBLIC_URL}/icon/bookmark-plus-fill.svg` : `${process.env.PUBLIC_URL}/icon/plus.svg`} alt=""/>
                                        <p className={contentDetail?.wishlistCheck ? "wish-btn" : ""}>보고싶어요</p>
                                    </button>
                                </li>
                                <li>
                                    <button onClick={async () => {
                                        userInfo ? openMyComment() : openLogin();
                                    }}>
                                        <img src={`${process.env.PUBLIC_URL}/icon/pencil.svg`} alt=""/>
                                        <p>코멘트</p>
                                    </button>
                                </li>
                                <li>
                                    <button onClick={async () => {
                                        userInfo ? handleCollectionClick() : openLogin();
                                    }}>
                                        <img src={`${process.env.PUBLIC_URL}/icon/plus-square.svg`} alt=""/>
                                        <p>컬렉션</p>
                                    </button>
                                </li>
                            </ul>
                        </div>
                        {/*내가 쓴 코멘트*/}
                        {contentDetail?.myComment && (
                            <div className="info-middle">
                                <p className="my-comment-title">내가 쓴 코멘트</p>
                                <div className="my-comment-content">
                                    <div className="my-comment-content-image"><img src={contentDetail?.myComment.user.profile || `${process.env.PUBLIC_URL}/user.png`} alt=""/></div>
                                    <p>{contentDetail?.myComment.text}</p>
                                    <div className="my-comment-btn">
                                        <button onClick={handleDeleteClick}>
                                            <i className="bi bi-trash"></i>
                                            <p>삭제</p>
                                        </button>
                                        <button onClick={openMyComment}>
                                            <i className="bi bi-pencil"></i>
                                            <p>수정</p>
                                        </button>

                                    </div>
                                </div>
                            </div>
                        )}
                        {/*컨텐츠 정보 설명*/}
                        <div className="info-bottom">
                            <p>{contentDetail?.contentInfo.description}</p>
                        </div>
                    </div>
                </div>

                {/*마이코멘트 모달창*/}
                {myCommentModal && (
                    <div className="comment-modal-overlay" onClick={closeMyComment}>
                        <div className="comment-modal-content" onClick={(e) => e.stopPropagation()}>
                            <div className="comment-content-top">
                                <p className="comment-modal-title">{contentDetail?.contentInfo?.title}</p>
                                <button className="comment-close-button" onClick={closeMyComment}><img src={`${process.env.PUBLIC_URL}/icon/x-lg.svg`} alt=""/></button>
                            </div>
                            <textarea rows="15" placeholder="작품에 대한 코멘트를 남겨주세요." maxLength={1000} ref={textRef}></textarea>
                            <div className="comment-content-bottom">
                                <button className="comment-content-btn"
                                        onClick={handleMyCommentSubmit}
                                        >{contentDetail?.myComment ? "수정" : "저장"}</button>
                            </div>
                        </div>
                    </div>
                )}

                {/*마이코멘트 삭제 확인 모달창*/}
                {deleteConfirmModal && (
                    <div className="confirm-modal-overlay" onClick={() => setDeleteConfirmModal(false)}>
                        <div className="confirm-modal-content" onClick={(e) => e.stopPropagation()}>
                            <p>알림</p>
                            <p>삭제하시겠습니까?</p>
                            <div className="confirm-btn-group">
                                <button  onClick={handleDeleteConfirm}>확인</button>
                                <button onClick={() => setDeleteConfirmModal(false)}>취소</button>
                            </div>
                        </div>
                    </div>
                )}

                {/*컬렉션 이미지 추가 모달창*/}
                {collectionListModal && (
                    <div className="collection-list-modal-overlay" onClick={closeCollectionListModal}>
                        <div className="collection-list-modal-content" onClick={(e) => e.stopPropagation()}>
                            <div className="collection-list-modal-content-top">
                                <button className="collection-list-modal-close-button" onClick={closeCollectionListModal}>
                                    <i className="bi bi-x-lg"></i>
                                </button>
                                <p className="collection-list-modal-title">컬렉션에 추가</p>
                                <button className="collection-list-modal-content-btn" onClick={() => handleCollectionAdd()}>
                                    추가
                                </button>
                            </div>

                            <div className="collection-list-modal-content-bottom">
                                {collectionList?.content?.map(item =>
                                    <div className="form-check" key={item.collection.id}>
                                        <input className="form-check-input" type="checkbox" value="" onChange={() => handleCollectionSelect(item.collection.id)} id={`checkDefault-${item.collection.id}`} checked={item.included || selectedCollections.includes(item.collection.id)} disabled={item.included}/>
                                        <label className="form-check-label" htmlFor={`checkDefault-${item.collection.id}`}>
                                            <div className="collection-list-modal-search-results">
                                                <div className="collection-list-modal-search-results-img">
                                                    <img src={item.collection.poster.length > 0 ? item.collection.poster : `${process.env.PUBLIC_URL}/basic-bg.jpg`} alt=""/>
                                                </div>
                                                <div className="collection-list-modal-search-results-info">
                                                    <p className="collection-list-modal-search-results-info-top">{item.collection.title}</p>
                                                    <p className="collection-list-modal-search-results-info-bottom">{item.collection.contentCount}개 작품</p>
                                                </div>
                                            </div>
                                        </label>
                                    </div>
                                )}
                                <div ref={collectionListRef} hidden={collectionList?.page.number + 2 > collectionList?.page.totalPages}></div>
                            </div>
                        </div>
                    </div>
                )}
            </section>

            {/*외부서비스링크 섹션*/}
            {contentDetail?.externalServices?.length > 0 &&(
                <section className="detail-content container">
                    <div className="external-services">
                        <p className="detail-category">감상 가능한 곳</p>
                        {contentDetail?.externalServices?.map(service => (
                            <a key={service.id} href={service.href} target="_blank" rel="noopener noreferrer">
                                <img src={serviceLogos[service.type] || `${process.env.PUBLIC_URL}/images/default-logo.png`} alt={service.type}/>
                            </a>
                        ))}
                    </div>
                </section>
            )}

            {/*코멘트 섹션*/}
            <section className="detail-content container">
                <div className="comment-text">
                    <p className="detail-category">코멘트 <span className="comment-count">{contentDetail?.commentCount}</span> </p>
                    <NavLink to={`/content/${contentDetail?.contentInfo.id}/comment`}>더보기</NavLink>
                </div>
                {contentDetail && contentDetail.comments && contentDetail.comments.length > 0 ? (
                    <div className="comment-inner">
                        {contentDetail.comments
                            .slice(0, window.innerWidth <= 480 ? 1 : 8)
                            .map(c => <CommentCard key={c.id} clamp={true} comment={c} userInfo={userInfo} openLogin={openLogin} />)}
                    </div>

                ) : (
                    <p>코멘트가 없습니다 :)</p>
                )}
            </section>

            {/*크레딧 섹션*/}
             <section className="detail-content container">
                 {/*컨텐츠 타입*/}
                 <p className="detail-category">
                     {contentDetail?.contentInfo?.contentType === '영화' && '출연/제작'}
                     {contentDetail?.contentInfo?.contentType === '시리즈' && '출연/제작'}
                     {contentDetail?.contentInfo?.contentType === '책' && '저자/역자'}
                     {contentDetail?.contentInfo?.contentType === '웹툰' && '작가'}
                     {contentDetail?.contentInfo?.contentType === '음반' && '참여'}
                 </p>

                 {/*크레딧 정보 리스트*/}
                 {contentCredit?.content.length > 0 ? (
                    <div className="credit-list">
                        <div className="credit-wrapper">
                            <Swiper
                                ref={swiperRef}
                                modules={[Navigation]}
                                spaceBetween={20}
                                slidesPerView={1}   // ✅ 슬라이드 하나 안에 그리드 넣기
                                slidesPerGroup={1}
                                navigation={{
                                    prevEl: ".credit-prev",
                                    nextEl: ".credit-next",
                                }}
                                breakpoints={{
                                    480: {slidesPerView: 1 , slidesPerGroup: 1 },
                                    768: {slidesPerView: 2,  slidesPerGroup: 2 },
                                    1020: {slidesPerView: 4,  slidesPerGroup: 4 },
                                }}
                                className="credit-swiper"
                            >
                                {chunkedContentCredit.map((chunkedCredit, outerIndex) => (
                                    <SwiperSlide className="credit-grid" key={outerIndex}>
                                        {chunkedCredit.map((credit, innerIndex) => (
                                            <div key={credit.id}>
                                                <CreditOrPersonCard data={credit} type={"credit"} />
                                                {outerIndex === chunkedContentCredit.length -1 && innerIndex === chunkedCredit.length - 1 && contentCredit?.page.number + 1 < contentCredit?.page.totalPages && <div ref={loadMoreRef}></div>}
                                            </div>
                                        ))}
                                    </SwiperSlide>
                                ))}
                            </Swiper>

                        </div>

                        <div className={`credit-prev`}><img src={`${process.env.PUBLIC_URL}/icon/arrow-left.svg`} alt=""/></div>
                        <div className={`credit-next`}><img src={`${process.env.PUBLIC_URL}/icon/arrow-right.svg`} alt=""/></div>

                    </div>

                    ) : (
                        <p>크레딧 정보가 없습니다 :)</p>
                    )}

             </section>

            {/*갤러리 섹션*/}
            {contentDetail?.gallery?.length > 0 && (
                <section className="detail-content detail-gallery container">
                    <div className="detail-gallery-inner">
                        <p className="detail-category">갤러리</p>
                        <div className="gallery-wrapper">
                            <Swiper
                                modules={[Navigation]}
                                spaceBetween={16}
                                slidesPerView={1}
                                navigation={{
                                    prevEl: ".gallery-wrapper .gallery-prev",
                                    nextEl: ".gallery-wrapper .gallery-next",
                                }}
                                breakpoints={{
                                    480: {slidesPerView: 1},
                                    760: {slidesPerView: 2},
                                    1020: {slidesPerView: 3},
                                }}
                                className="gallery-swiper">

                                {contentDetail?.gallery?.map((gallery, idx) => (
                                    <SwiperSlide className="swiper-slide" key={gallery.id}>
                                        <div className="slide-image" onClick={() => openGalleryModal(idx)} style={{cursor: 'pointer'}}>
                                            <img src={gallery.imageUrl} alt=""/>
                                        </div>
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                            <div className="gallery-prev"><img src={`${process.env.PUBLIC_URL}/icon/arrow-left.svg`} alt=""/></div>
                            <div className="gallery-next"><img src={`${process.env.PUBLIC_URL}/icon/arrow-right.svg`} alt=""/></div>
                        </div>
                    </div>

                    {/* 모달 */}
                    {galleryModal && (
                        <div className="gallery-modal-overlay" onClick={closeGalleryModal}>
                            <div className="gallery-modal-content" onClick={(e) => e.stopPropagation()}>
                                <Swiper
                                    modules={[Navigation, Pagination]}
                                    initialSlide={galleryModalStartIndex}
                                    navigation
                                    spaceBetween={10}
                                    slidesPerView={1}
                                    className="gallery-modal-swiper">

                                    {contentDetail?.gallery?.map((gallery) => (
                                        <SwiperSlide key={gallery.id}>
                                            <div className="modal-slide-wrapper">
                                                <img
                                                    src={gallery.imageUrl}
                                                    alt="갤러리 이미지"
                                                    style={{ width: "100%", height: "auto" }}/>
                                            </div>
                                        </SwiperSlide>
                                    ))}
                                </Swiper>
                            </div>
                        </div>
                    )}
                </section>
            )}

            {/*동영상 섹션*/}
            {contentDetail?.videos?.length > 0 && (
                <section className="detail-content detail-video container">
                    <div className="detail-video-inner">
                        <p className="detail-category">동영상</p>
                        <div className="video-wrapper">
                            <Swiper
                                modules={[Navigation]}
                                spaceBetween={16}
                                slidesPerView={1}
                                navigation={{
                                    prevEl: ".video-wrapper .video-prev",
                                    nextEl: ".video-wrapper .video-next",
                                }}
                                breakpoints={{
                                    480: {slidesPerView: 1},
                                    760: {slidesPerView: 2},
                                    1020: {slidesPerView: 3},
                                }}
                                className="video-swiper">
                                {contentDetail?.videos?.map(videos => (
                                    <SwiperSlide className="swiper-slide" key={videos.id}>
                                        <a className="slide-image" href={videos.url} target="_blank" rel="noopener noreferrer">
                                            <img src={videos.image} alt=""/>
                                            <p>{videos.title}</p>
                                        </a>
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                            <div className="video-prev"><img src={`${process.env.PUBLIC_URL}/icon/arrow-left.svg`} alt=""/></div>
                            <div className="video-next"><img src={`${process.env.PUBLIC_URL}/icon/arrow-right.svg`} alt=""/></div>
                        </div>
                    </div>
                </section>
            )}
        </div>
    )
}
export default DetailPage;