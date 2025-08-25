import React, { useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css";
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css';
import { deleteRating, getContentDetail, postContentRating } from "../API/ContentApi";
import {Autoplay, Navigation, Pagination} from "swiper/modules";
import {Swiper, SwiperSlide} from "swiper/react";
import CreditOrPersonCard from "../components/CreditOrPersonCard";
import {getContentCredit} from "../API/ContentApi";

const DetailPage = ({userInfo, openLogin}) => {
    const [contentDetail, setContentDetail] = useState(null);
    const [modalOpen, setModalOpen] = useState(false); // 모달 상태
    const [modalStartIndex, setModalStartIndex] = useState(0); // 모달 슬라이드 시작 인덱스
    const [contentCredit, setContentCredit] = useState(null);

    const [score, setScore] = useState(0);
    const [hoverScore, setHoverScore] = useState(0); // 마우스 올릴 때 임시 점수


    const handleScoreDelete = () => {
        // 별점 삭제 로직 구현
        const id = window.location.pathname.split('/').pop();
        deleteRating(id).then(response => {
            console.log('Rating deleted:', response.status);
        });
    };


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

    useEffect(() => {
        // fetchContentDetail ...
    }, []);

    const openModal = (index) => {
        setModalStartIndex(index);
        setModalOpen(true);
    };

    const closeModal = () => {
        setModalOpen(false);
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
                let page = 1;
                const allPages = [];

                while (true) {
                    const response = await getContentCredit(id, page);
                    const data = response.data;

                    allPages.push(data);

                    // 마지막 페이지라면 종료
                    if (data.last || data.content.length === 0) break;

                    page++;
                }

                setContentCredit({ pages: allPages });
            } catch (error) {
                console.error('Error fetching content credit:', error);
            }
        };

        fetchContentCredit();
    }, []);

    // 컨텐츠 정보를 가져온 후
    useEffect(() => {
        console.log(contentDetail);
        // 내가 준 별점 설정
        setScore(contentDetail?.myRating || 0);

        // 별점 그래프 높이 계산
        if (contentDetail && contentDetail.ratingDistribution.some(rating => rating.height === undefined)) {
            // 최대 값 찾기
            const max = Math.max(...contentDetail.ratingDistribution.map(rating => rating.scoreCount));

            // 최고 점수에 isMax 플래그 추가
            contentDetail.ratingDistribution = contentDetail.ratingDistribution.map(rating => ({
                ...rating,
                isMax: rating.scoreCount === max
            }));

            // 각 점수에 대한 높이 계산을 하여 contentDetail에 추가
            const updatedDistribution = contentDetail.ratingDistribution.map(rating => ({
                ...rating,
                height: (rating.scoreCount / max) * 100 // 예: 최대 높이를 100으로 설정
            }));

            setContentDetail(prev => ({
                ...prev,
                ratingDistribution: updatedDistribution
            }));
        }
    }, [contentDetail]);

    useEffect(() => {
        console.log(contentCredit)
    }, [contentCredit]);



    return (
        <div className="detail">
            {/*상세페이지 배너*/}
            <section className="detail-banner">
                {/*상세페이지 포스터*/}
                <div className="banner-post">
                    <img src={contentDetail?.contentInfo.poster} alt=""/>
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

            {/*컨텐츠 정보 및 평가*/}
            <section className="detail-content detail-content-bg">
                <div className="detail-content-inner container">
                    {/*컨텐츠 포스터*/}
                    <div className="detail-content-inner-poster">
                        <div className="poster-top">
                            <img src={contentDetail?.contentInfo.poster} alt=""/>
                        </div>
                        <ul className="poster-bottom">
                            <li>별점 그래프</li>
                            <li>평균 <i className="bi bi-star-fill"></i> {contentDetail?.contentInfo?.ratingAvg.toFixed(1)}
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
                                <p className="average-rating">{contentDetail?.contentInfo?.ratingAvg.toFixed(1)}</p>
                                <p className="average-rating-text">평균 별점 <span className="average-rating-num">&#40;{contentDetail?.ratingCount}명&#41;</span></p>
                            </div>

                            {/*위시,컬렉션 추가 및 코멘트 달기*/}
                            <ul className="info-top-right">
                                <li>
                                    <i className="bi bi-plus-lg"></i>
                                    <p>보고싶어요</p>
                                </li>
                                <li>
                                    <i className="bi bi-pencil-fill"></i>
                                    <p>코멘트</p>
                                </li>
                                <li>
                                    <i className="bi bi-plus-square-fill"></i>
                                    <p>컬렉션</p>
                                </li>
                            </ul>
                        </div>
                        {/*내가 쓴 코멘트*/}
                        <div className="info-middle">
                            <p className="my-coment">내가 쓴 코멘트</p>
                            {/*코멘트가 있으면 나오게 하기*/}
                        </div>
                        {/*컨텐츠 정보 설명*/}
                        <div className="info-bottom">
                            <p>{contentDetail?.contentInfo.description}</p>
                        </div>
                    </div>
                </div>
            </section>

            {/*외부 서비스 링크*/}
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

            {/*크레딧*/}
             <section className="detail-content container">
                 <p className="detail-category">
                     {contentDetail?.contentInfo?.contentType === 'MOVIE' && '출연/제작'}
                     {contentDetail?.contentInfo?.contentType === 'SERIES' && '출연/제작'}
                     {contentDetail?.contentInfo?.contentType === 'BOOK' && '저자/역자'}
                     {contentDetail?.contentInfo?.contentType === 'WEBTOON' && '작가'}
                     {contentDetail?.contentInfo?.contentType === 'RECORD' && '참여'}
                 </p>

                 <div className="credit-list">

                     {contentCredit?.pages?.length > 0 ? (
                         <Swiper
                             modules={[Navigation]}
                             navigation={{
                                 prevEl: ".credit-prev",
                                 nextEl: ".credit-next",
                             }}
                             spaceBetween={20}
                             slidesPerView={1}
                             className="credit-swiper"
                         >
                             {contentCredit.pages
                                 .filter(pageData => pageData.content && pageData.content.length > 0) // ✅ 빈 페이지 제외
                                 .map((pageData, pageIndex) => (
                                     <SwiperSlide key={pageIndex}>
                                         <div className="credit-list">
                                             {pageData.content.map((credit) => (
                                                 <CreditOrPersonCard key={credit.id} type="credit" data={credit} />
                                             ))}
                                         </div>
                                     </SwiperSlide>
                                 ))}
                         </Swiper>

                     ) : (
                         <p>크레딧 정보가 없습니다.</p>
                     )}
                     <div className="credit-prev"><img src="/arrow-left.svg" alt=""/></div>
                     <div className="credit-next"><img src="/arrow-right.svg" alt=""/></div>

                 </div>

             </section>

            {/*갤러리*/}
            {contentDetail?.gallery?.length > 0 && (
                <section className="detail-content detail-gallary container">
                    <div className="detail-gallary-inner">
                        <p className="detail-category">갤러리</p>
                        <div className="gallary-wrapper">
                            <Swiper
                                modules={[Navigation, Autoplay]}
                                spaceBetween={16}
                                slidesPerView={3}
                                navigation={{
                                    prevEl: ".gallary-wrapper .gallary-prev",
                                    nextEl: ".gallary-wrapper .gallary-next",
                                }}
                                breakpoints={{
                                    480: {slidesPerView: 1},
                                    768: {slidesPerView: 2},
                                    1020: {slidesPerView: 3},
                                }}
                                className="gallary-swiper">

                                {contentDetail?.gallery?.map((gallary, idx) => (
                                    <SwiperSlide className="swiper-slide" key={gallary.id}>
                                        <div className="slide-image" onClick={() => openModal(idx)} style={{cursor: 'pointer'}}>
                                            <img src={gallary.imageUrl} alt=""/>
                                        </div>
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                            <div className="gallary-prev"><img src="/arrow-left.svg" alt=""/></div>
                            <div className="gallary-next"><img src="/arrow-right.svg" alt=""/></div>
                        </div>
                    </div>

                    {/* 모달 */}
                    {modalOpen && (
                        <div className="gallary-modal-overlay" onClick={closeModal}>
                            <div className="gallary-modal-content" onClick={(e) => e.stopPropagation()}>
                                <Swiper
                                    modules={[Navigation, Pagination]}
                                    initialSlide={modalStartIndex}
                                    navigation
                                    spaceBetween={10}
                                    slidesPerView={1}
                                    className="gallary-modal-swiper">

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

            {/*동영상*/}
            {contentDetail?.videos?.length > 0 && (
                <section className="detail-content detail-video container">
                    <div className="detail-video-inner">
                        <p className="detail-category">동영상</p>
                        <div className="video-wrapper">
                            <Swiper
                                modules={[Navigation, Autoplay]}
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
                            <div className="video-prev"><img src="/arrow-left.svg" alt=""/></div>
                            <div className="video-next"><img src="/arrow-right.svg" alt=""/></div>
                        </div>
                    </div>
                </section>
            )}

        </div>
    )
}
export default DetailPage;