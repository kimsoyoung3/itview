import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";

import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "./CollectionSwiper.css";

export default function ContentSwiper() {
    return (
        <Swiper
            modules={[Navigation, Pagination, Autoplay]}
            spaceBetween={20} // 슬라이드 간 간격
            slidesPerView={2} // 기본 한 화면에 보여줄 슬라이드 수
            navigation // 좌우 화살표
            breakpoints={{ // 반응형
                480: { slidesPerView: 2 },
                768: { slidesPerView: 4 },
                1020: { slidesPerView: 5 },
            }}
            className="content-swiper"
        >

            {/* 슬라이드 아이템 */}
            <SwiperSlide className="swiper-slide">

                {/* 이미지 그리드 */}
                <div className="slide-image collection-slide-image">
                    <div><img src={`${process.env.PUBLIC_URL}/image.jpg`} alt="이미지1"/></div>
                    <div><img src={`${process.env.PUBLIC_URL}/image.jpg`} alt="이미지2"/></div>
                    <div><img src={`${process.env.PUBLIC_URL}/image.jpg`} alt="이미지3"/></div>
                    <div><img src={`${process.env.PUBLIC_URL}/image.jpg`} alt="이미지4"/></div>
                </div>

                {/* 컬렉션 정보 */}
                <div className="collection-slide-contentType">
                    <p className="title">제목</p>
                    <p>좋아요 <span>0</span></p>
                </div>

            </SwiperSlide>

        </Swiper>
    );
}
