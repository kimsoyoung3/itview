import React, { use, useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css";
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css';
import { getContentDetail } from "../API/ContentApi";
import {Autoplay, Navigation, Pagination} from "swiper/modules";
import {Swiper, SwiperSlide} from "swiper/react";

const DetailPage = () => {
    const [contentDetail, setContentDetail] = useState(null);
    const [modalOpen, setModalOpen] = useState(false); // 모달 상태
    const [modalStartIndex, setModalStartIndex] = useState(0); // 모달 슬라이드 시작 인덱스

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

    useEffect(() => {
        console.log(contentDetail)
    }, [contentDetail]);


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
                        <li>{contentDetail?.contentInfo.description}</li>
                        <li><span>{contentDetail?.contentInfo.releaseDate}</span> &middot; <span>{contentDetail?.contentInfo.genres?.map(genre => genre).join('/')}</span> &middot; <span>국가</span></li>
                        <li><span>{contentDetail?.contentInfo.duration}</span >&middot; <span>{contentDetail?.contentInfo.age}</span></li>
                    </ul>
                </div>

                {/*외부서비스링크*/}
                <div className="external container">
                    <div className="external-services">
                        {contentDetail?.externalServices?.map(service => (
                            <a key={service.id} href={service.href} target="_blank" rel="noopener noreferrer">
                                <img src={serviceLogos[service.type] || '/images/default-logo.png'} alt={service.type}/>
                            </a>
                        ))}
                    </div>
                </div>
            </section>

            {/*컨텐츠 정보 및 평가*/}
            <section className="detail-content">
                <div className="detail-content-inner container">
                    <div className="detail-content-inner-poster">
                        <img src={contentDetail?.contentInfo.poster} alt=""/>
                    </div>
                    <div className="detail-content-inner-info">
                        <div className="info-top">
                            <div className="info-top-left"></div>
                            <div className="info-top-center"></div>
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
                        <div className="info-middle"></div>
                        <div className="info-bottom">
                            <p>영화설명칸</p>
                        </div>
                    </div>
                </div>
            </section>

            <section className="detail-gallary container">
                <div className="detail-gallary-inner">
                    <p>갤러리</p>
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
                        <div
                            className="gallary-modal-content"
                            onClick={(e) => e.stopPropagation()}
                        >
                            <button className="modal-close" onClick={closeModal}>
                                ✕
                            </button>
                            <Swiper
                                modules={[Navigation, Pagination]}
                                initialSlide={modalStartIndex}
                                navigation
                                pagination={{ clickable: true }}
                                spaceBetween={10}
                                slidesPerView={1}
                                className="gallary-modal-swiper"
                            >
                                {contentDetail?.gallery?.map((gallery) => (
                                    <SwiperSlide key={gallery.id}>
                                        <img
                                            src={gallery.imageUrl}
                                            alt="갤러리 이미지"
                                            style={{ width: "100%", height: "auto" }}
                                        />
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                        </div>
                    </div>
                )}
            </section>

            <section className="detail-video container">
                <div className="detail-video-inner">
                    <p>동영상</p>
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
        </div>
    )
}
export default DetailPage;