import React, {useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./DetailPage.css";
import "../../components/ContentSwiper/ContentSwiper.css";
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css';
import { getContentCredit, deleteRating, getContentComment, getContentDetail, postContentComment, postContentRating } from "../../API/ContentApi";
import {Navigation, Pagination} from "swiper/modules";
import {Swiper, SwiperSlide} from "swiper/react";
import CreditOrPersonCard from "../../components/CreditOrPersonCard/CreditOrPersonCard";
import CommentCard from "../../components/CommentCard/CommentCard";
import {NavLink} from "react-router-dom";
import { deleteComment, updateComment } from "../../API/CommentApi";
import {toast} from "react-toastify";

const DetailPage = ({userInfo, openLogin}) => {
    const [contentDetail, setContentDetail] = useState(null);
    const [contentCredit, setContentCredit] = useState([]);

    const [score, setScore] = useState(0);
    /*마우스 올릴 때 임시 점수*/
    const [hoverScore, setHoverScore] = useState(0);

    /*코멘트 텍스트 영역 참조*/
    const textRef = React.useRef(null);

    /*코멘트 모달*/

    const [myCommentModal, setMyCommentModal] = useState();
    const openMyComment = () => setMyCommentModal(true);
    const closeMyComment = () => setMyCommentModal(false);

    /*코멘트 작성*/
    const handleCommentPost = async () => {
        const res = await postContentComment(window.location.pathname.split('/').pop(), { text: textRef.current.value })
        if (res.status === 200) {
            closeMyComment();
            getContentComment(window.location.pathname.split('/').pop()).then(response => {
                setContentDetail(prev => ({
                    ...prev,
                    myComment: response.data
                }));
            });
        }
    };

    /*코멘트 수정*/
    const handleCommentUpdate = async () => {
        const res = await updateComment(contentDetail?.myComment.id, { text: textRef.current.value })
        if (res.status === 200) {
            closeMyComment();
            getContentComment(window.location.pathname.split('/').pop()).then(response => {
                setContentDetail(prev => ({
                    ...prev,
                    myComment: response.data
                }));
            });
        }
        console.log(res);
    };

    const handleMyCommentSubmit = () => {
        if (contentDetail?.myComment) {
            handleCommentUpdate();
        } else {
            handleCommentPost();
        }
        toast("코멘트를 저장했습니다.")
        closeMyComment();
    };

    /*마이코멘트 삭제 확인 모달창*/
    const [deleteConfirmModal, setDeleteConfirmModal] = useState(false);

    const handleDeleteClick = () => {
        setDeleteConfirmModal(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            await deleteComment(contentDetail?.myComment.id);
            setDeleteConfirmModal(false); // ✅ 모달 닫기만
            getContentComment(window.location.pathname.split('/').pop()).then(response => {
                setContentDetail(prev => ({
                    ...prev,
                    myComment: response.data
                }));
            });
        } catch (error) {
            console.error("삭제 중 오류:", error);
        }
    };

    /*별점 삭제 로직 구현*/
    const handleScoreDelete = () => {
        const id = window.location.pathname.split('/').pop();
        deleteRating(id).then(response => {
            console.log('Rating deleted:', response.status);
        });
    };

    /*외부서비스 로고*/
    const serviceLogos = {
        NETFLIX: '/externalLogo/netflix.png',
        DISNEY_PLUS: '/externalLogo/disney.png',
        WAVE: '/externalLogo/wave.png',
        WATCHA: '/externalLogo/watcha.png',
        TVING: '/externalLogo/tving.png',
        TVN: '/externalLogo/tvn.png',
        APPLE_TV_PLUS: '/externalLogo/apple-tv-plus.png',

        ALADIN: '/externalLogo/aladin.png',
        YES24: '/externalLogo/yes24.png',
        KYOBO: '/externalLogo/kyobo.png',

        NAVER: '/externalLogo/naverWebtoon.png',
        KAKAO: '/externalLogo/kakaoWebtoon.png',
    };

    /*갤러리 모달창*/
    const [galleryModal, setGalleryModal] = useState(false);
    /*모달 슬라이드 시작 인덱스*/
    const [galleryModalStartIndex, setGalleryModalStartIndex] = useState(0);

    const openGalleryModal = (index) => {
        setGalleryModalStartIndex(index);
        setGalleryModal(true);
    };

    const closeGalleryModal = () => {
        setGalleryModal(false);
    };

    /*컨텐츠 디테일*/
    useEffect(() => {
        const fetchContentDetail = async () => {
            try {
                const response = await getContentDetail(window.location.pathname.split('/').pop());
                setContentDetail(response.data);
            } catch (error) {
                console.error('Error fetching content detail:', error);
            }
        }

        fetchContentDetail();
    }, []);

    /*컨텐츠 크레딧*/
    useEffect(() => {
        const fetchContentCredit = async () => {
            try {
                const id = window.location.pathname.split('/').pop();
                const response = await getContentCredit(id, 1);
                var creditInit = [];
                creditInit.push(response.data);
                if (response.data.page.totalPages > 1) {
                    const response = await getContentCredit(id, 2);
                    creditInit.push(response.data);
                }
                setContentCredit(creditInit);
            } catch (error) {
                console.error('Error fetching content credit:', error);
            }
        };
        
        fetchContentCredit();
    }, []); 

    /*스와이퍼 레퍼런스*/
    const swiperRef = React.useRef(null);

    const [swiperPage, setSwiperPage] = useState(1);
    
    /*이전 버튼 핸들러*/
    const handlePrev = () => {
        swiperRef.current.swiper.slidePrev();
        setSwiperPage(prev => prev - 1);
    }

    /*다음 버튼 핸들러*/
    const handleNext = () => {
        swiperRef.current.swiper.slideNext();
        setSwiperPage(prev => prev + 1);
        if (swiperRef.current.swiper.isEnd && swiperRef.current.swiper.activeIndex < contentCredit[0].page.totalPages - 1) {
            const fetchNextPage = async () => {
                console.log('마지막 슬라이드 도달, 다음 페이지 로드 시도');
                try {
                    const id = window.location.pathname.split('/').pop();
                    const nextPage = swiperRef.current.swiper.activeIndex + 2;
                    const response = await getContentCredit(id, nextPage);
                    console.log(response.data)
                    if (response.data.content.length > 0) {
                        setContentCredit(prev => [...prev, response.data]);
                    } else {
                        console.log('더 이상 크레딧이 없습니다.');
                    }
                } catch (error) {
                    console.error('Error fetching next credit page:', error);
                }
            };
            fetchNextPage();
        }
    }

    /*컨텐츠 정보를 가져온 후*/
    useEffect(() => {
        console.log(contentDetail);
        /*내가 준 별점 설정*/
        setScore(contentDetail?.myRating || 0);

        /*별점 그래프 높이 계산*/
        if (contentDetail && contentDetail.ratingDistribution.some(rating => rating.height === undefined)) {
            /*최대 값 찾기*/
            const max = Math.max(...contentDetail.ratingDistribution.map(rating => rating.scoreCount));

            /*최고 점수에 isMax 플래그 추가*/
            contentDetail.ratingDistribution = contentDetail.ratingDistribution.map(rating => ({
                ...rating,
                isMax: rating.scoreCount === max
            }));

            /*각 점수에 대한 높이 계산을 하여 contentDetail에 추가*/
            const updatedDistribution = contentDetail.ratingDistribution.map(rating => ({
                ...rating,
                /*예: 최대 높이를 100으로 설정*/
                height: (rating.scoreCount / max) * 100
            }));

            setContentDetail(prev => ({
                ...prev,
                ratingDistribution: updatedDistribution
            }));
        }
    }, [contentDetail]);

    useEffect(() => {
        if (contentDetail?.myComment && textRef.current) {
            textRef.current.value = contentDetail.myComment.text;
        }
    }, [contentDetail, myCommentModal]);

    useEffect(() => {
        console.log(contentCredit)
    }, [contentCredit]);

    return (
        <div className="detail">
            {/*상세페이지 배너 섹션*/}
            <section className="detail-banner">
                {/*상세페이지 포스터*/}
                <div className="banner-post">
                    <img src={contentDetail?.gallery && contentDetail?.gallery.length > 0 ? contentDetail?.gallery[0].imageUrl : '/basic-bg.jpg' } alt=""/>
                </div>

                {/*포스터 설명*/}
                <div className="post-dec container">
                    <ul key={contentDetail?.contentInfo.id}>
                        <li>{contentDetail?.contentInfo.title}</li>
                        <li><span>{contentDetail?.contentInfo.releaseDate}</span> &middot; <span>{contentDetail?.contentInfo.genres?.map(genre => genre).join('/')}</span> &middot; <span>{contentDetail?.contentInfo.nation}</span></li>
                        <li><span>{contentDetail?.contentInfo.duration}</span >&middot; <span>{contentDetail?.contentInfo.age}</span></li>
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
                            <li>평균 <i className="bi bi-star-fill"></i> {contentDetail?.contentInfo?.ratingAvg?.toFixed(1)}
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
                                                if (newScore === 0) {
                                                    await handleScoreDelete();
                                                } else {
                                                    const id = window.location.pathname.split('/').pop();
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
                                <p className="average-rating">{contentDetail?.contentInfo?.ratingAvg?.toFixed(1)}</p>
                                <p className="average-rating-text">평균 별점 <span className="average-rating-num">&#40;{contentDetail?.ratingCount}명&#41;</span></p>
                            </div>

                            {/*위시,컬렉션 추가 및 코멘트 달기*/}
                            <ul className="info-top-right">
                                <li>
                                    <button>
                                        <img src="/icon/plus.svg" alt=""/>
                                        <p>보고싶어요</p>
                                    </button>
                                </li>
                                <li>
                                    <button onClick={async () => {
                                        userInfo ? openMyComment() : openLogin();
                                    }}>
                                        <img src="/icon/pencil.svg" alt=""/>
                                        <p>코멘트</p>
                                    </button>
                                </li>
                                <li>
                                    <button>
                                        <img src="/icon/plus-square.svg" alt=""/>
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
                                    <div className="my-comment-content-image"><img src={contentDetail?.myComment.user.profile || "/user.png"} alt=""/></div>
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
                                <button className="comment-close-button" onClick={closeMyComment}><img src="/icon/x-lg.svg" alt=""/></button>
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
            </section>

            {/*외부서비스링크 섹션*/}
            {contentDetail?.externalServices?.length > 0 &&(
                <section className="detail-content container">
                    <div className="external-services">
                        <p className="detail-category">감상 가능한 곳</p>
                        {contentDetail?.externalServices?.map(service => (
                            <a key={service.id} href={service.href} target="_blank" rel="noopener noreferrer">
                                <img src={serviceLogos[service.type] || '/images/default-logo.png'} alt={service.type}/>
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
                        {contentDetail.comments.map(c => <CommentCard key={c.id} clamp={true} comment={c} userInfo={userInfo} openLogin={openLogin} />)}
                    </div>

                ) : (
                    <p>코멘트가 없습니다.</p>
                )}
            </section>

            {/*크레딧 섹션*/}
             <section className="detail-content container">
                 {/*컨텐츠 타입*/}
                 <p className="detail-category">
                     {contentDetail?.contentInfo?.contentType === 'MOVIE' && '출연/제작'}
                     {contentDetail?.contentInfo?.contentType === 'SERIES' && '출연/제작'}
                     {contentDetail?.contentInfo?.contentType === 'BOOK' && '저자/역자'}
                     {contentDetail?.contentInfo?.contentType === 'WEBTOON' && '작가'}
                     {contentDetail?.contentInfo?.contentType === 'RECORD' && '참여'}
                 </p>

                 {/*크레딧 정보 리스트*/}
                 {contentCredit[0]?.content.length > 0 ? (
                    <div className="credit-list">
                        <Swiper
                            ref={swiperRef}
                            modules={[Navigation]}
                            spaceBetween={20}
                            allowTouchMove={false}
                            slidesPerView={1}
                            allowTouchMove={false}
                            className="credit-swiper"
                            onNavigationNext={() => console.log('next')}>
                            {contentCredit.map((creditPage, pageIndex) => (
                                <SwiperSlide className="swiper-slide" key={pageIndex}>
                                    <div className="credit-list">
                                        {creditPage.content.map(credit => (
                                            <CreditOrPersonCard key={credit.id} type="credit" data={credit} />
                                        ))}
                                    </div>
                                </SwiperSlide>
                            ))}
                        </Swiper>
                        <div className={`credit-prev ${swiperPage === 1 ? 'disabled' : ''}`} onClick={handlePrev}><img src="/icon/arrow-left.svg" alt=""/></div>
                        <div className={`credit-next ${swiperPage === contentCredit[0].page.totalPages ? 'disabled' : ''}`} onClick={handleNext}><img src="/icon/arrow-right.svg" alt=""/></div>
                    </div>

                    ) : (
                        <p>크레딧 정보가 없습니다.</p>
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
                                slidesPerView={3}
                                navigation={{
                                    prevEl: ".gallery-wrapper .gallery-prev",
                                    nextEl: ".gallery-wrapper .gallery-next",
                                }}
                                breakpoints={{
                                    480: {slidesPerView: 1},
                                    768: {slidesPerView: 2},
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
                            <div className="gallery-prev"><img src="/icon/arrow-left.svg" alt=""/></div>
                            <div className="gallery-next"><img src="/icon/arrow-right.svg" alt=""/></div>
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
                                slidesPerView={3}
                                navigation={{
                                    prevEl: ".video-wrapper .video-prev",
                                    nextEl: ".video-wrapper .video-next",
                                }}
                                breakpoints={{
                                    480: {slidesPerView: 1},
                                    768: {slidesPerView: 2},
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
                            <div className="video-prev"><img src="/icon/arrow-left.svg" alt=""/></div>
                            <div className="video-next"><img src="/icon/arrow-right.svg" alt=""/></div>
                        </div>
                    </div>
                </section>
            )}

        </div>
    )
}
export default DetailPage;