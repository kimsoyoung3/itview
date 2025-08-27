import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";

import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "../css/CollectionSwiper.css"

export default function ContentSwiper() {
    return (
        <Swiper
            modules={[Navigation, Pagination, Autoplay]}
            spaceBetween={20}
            slidesPerView={2}
            navigation
            breakpoints={{
                480: { slidesPerView: 2 },
                768: { slidesPerView: 4 },
                1020: { slidesPerView: 5 },
            }}
            className="content-swiper">

            <SwiperSlide className="swiper-slide">
                <div className="slide-image collection-slide-image">
                    <div>
                        <img src="/image.jpg" alt=""/>
                    </div>
                    <div>
                        <img src="/image.jpg" alt=""/>
                    </div>
                    <div>
                        <img src="/image.jpg" alt=""/>
                    </div>
                    <div>
                        <img src="/image.jpg" alt=""/>
                    </div>
                </div>
                <div className="collection-slide-contentType">
                    <p className="title">제목</p>
                    <p>좋아요 <span>0</span></p>
                </div>
            </SwiperSlide>

        </Swiper>
    );
}
