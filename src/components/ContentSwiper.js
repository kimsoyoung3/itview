import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";

import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "../css/ContentSwiper.css"

export default function ContentSwiper() {
    return (
        <Swiper
            modules={[Navigation, Pagination, Autoplay]}
            spaceBetween={16}
            slidesPerView={2}
            navigation
            breakpoints={{
                480: { slidesPerView: 2 },
                768: { slidesPerView: 4 },
                1020: { slidesPerView: 5 },
            }}
            className="content-swiper">

            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="ps-2 pe-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>
            <SwiperSlide className="swiper-slide">
                <div className="slide-image">
                    <img src="/image.jpg" alt=""/>
                </div>
                <div className="slide-contentType">
                    <p className="title">제목</p>
                    <span>개봉년도</span><span className="p-lg-2">&middot;</span><span>국가</span>
                </div>
            </SwiperSlide>


        </Swiper>
    );
}
