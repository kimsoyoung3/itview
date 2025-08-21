import React, { use, useEffect, useState } from "react";
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

const DetailPage = () => {
    const [contentDetail, setContentDetail] = useState(null);
    const [modalOpen, setModalOpen] = useState(false); // 모달 상태
    const [modalStartIndex, setModalStartIndex] = useState(0); // 모달 슬라이드 시작 인덱스
    const [contentCredit, setContentCredit] = useState(null);

    const [score, setScore] = useState(0);
    const handleScoreSubmit = () => {
        // 별점 제출 로직 구현
        const id = window.location.pathname.split('/').pop();
        const data = { score: Number(score) };
        console.log(data);
        postContentRating(id, data).then(response => {
            console.log('Rating submitted:', response.status);
        });
    };
    const handleScoreDelete = () => {
        // 별점 삭제 로직 구현
        const id = window.location.pathname.split('/').pop();
        const data = { score: Number(score) };
        deleteRating(id, data).then(response => {
            console.log('Rating deleted:', response.status);
        });
        setScore(0); // 입력 필드 초기화
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
                const page = 1; // 기본 1로 지정
                const response = await getContentCredit(id, page);
                setContentCredit(response.data);

            } catch (error) {
                console.error('Error fetching content credit:', error);
            }
        }

        fetchContentCredit();
    }, []);
    

    useEffect(() => {
        console.log(contentDetail)
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
                        <img src={contentDetail?.contentInfo.poster} alt=""/>
                    </div>
                    {/*컨텐츠 정보*/}
                    <div className="detail-content-inner-info">
                        <div className="info-top">
                            {/*별점체크*/}
                            <div className="info-top-left">
                                <input type="number" onChange={(e) => setScore(e.target.value)}/>
                                <button onClick={handleScoreSubmit}>등록</button>
                                <button onClick={handleScoreDelete}>삭제</button>
                            </div>
                            {/*평균 별점 표시*/}
                            <div className="info-top-center"></div>
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

                     {contentCredit?.content.length > 0 ? (
                         contentCredit.content.map((credit) => (
                             <CreditOrPersonCard key={credit.id} type="credit" data={credit}/>
                         ))
                     ) : (
                         <p>크레딧 정보가 없습니다.</p>
                     )}

                 </div>

             </section>

            {/*갤러리*/}
            {contentDetail?.gallery?.length > 0 && (
                <section className="detail-content detail-gallary container">
                    <div className="detail-gallary-inner">
                        <p className="detail-category">갤러리</p>
                        <Swiper
                            modules={[Navigation, Pagination, Autoplay]}
                            spaceBetween={16}
                            slidesPerView={3}
                            navigation
                            breakpoints={{
                                480: {slidesPerView: 1},
                                768: {slidesPerView: 2},
                                1020: {slidesPerView: 3},
                            }}
                            className="gallay-swiper">

                            {contentDetail?.gallery?.map((gallary, idx) => (
                                <SwiperSlide className="swiper-slide" key={gallary.id}>
                                    <div className="slide-image" onClick={() => openModal(idx)} style={{cursor: 'pointer'}}>
                                        <img src={gallary.imageUrl} alt=""/>
                                    </div>
                                </SwiperSlide>
                            ))}


                        </Swiper>
                    </div>

                    {/* 모달 */}
                    {modalOpen && (
                        <div className="gallary-modal-overlay" onClick={closeModal}>
                            <div className="gallary-modal-content" onClick={(e) => e.stopPropagation()}>
                                <Swiper
                                    modules={[Navigation, Pagination]}
                                    initialSlide={modalStartIndex}
                                    navigation
                                    pagination={{ clickable: true }}
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
                        <Swiper
                            modules={[Navigation, Pagination, Autoplay]}
                            spaceBetween={16}
                            slidesPerView={3}
                            navigation
                            breakpoints={{
                                480: {slidesPerView: 1},
                                768: {slidesPerView: 2},
                                1020: {slidesPerView: 3},
                            }}
                            className="gallay-swiper">
                            <div className="swiper-wrapper">
                                {contentDetail?.videos?.map(videos => (
                                    <SwiperSlide className="swiper-slide" key={videos.id}>
                                        <a className="slide-image" href={videos.url} target="_blank" rel="noopener noreferrer">
                                            <img src={videos.image} alt=""/>
                                            <p>{videos.title}</p>
                                        </a>
                                    </SwiperSlide>
                                ))}
                            </div>
                        </Swiper>
                    </div>
                </section>
            )}

        </div>
    )
}
export default DetailPage;