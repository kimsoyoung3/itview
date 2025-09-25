import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";

import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "./ContentSwiper.css";
import {NavLink} from "react-router-dom";

export default function ContentSwiper({ data =[] }) {
    return (
        <Swiper
            modules={[Navigation]}
            spaceBetween={16}
            slidesPerView={2}
            navigation
            breakpoints={{
                480: { slidesPerView: 2 , slidesPerGroup : 2},
                768: { slidesPerView: 4 , slidesPerGroup : 4},
                1020: { slidesPerView: 5 , slidesPerGroup : 5},
            }}
            className="content-swiper"
        >
            {data.map((item, idx) => (
                <SwiperSlide key={item.id || idx} className="swiper-slide">
                    <NavLink to={`/content/${item?.id}`}>
                        <div className="slide-image">
                            <img src={item.poster ? item.poster : `${process.env.PUBLIC_URL}/basic-bg.jpg`} alt="슬라이드 이미지"/>
                        </div>
                        <div className="slide-contentType">
                            <p className="title">{item.title}</p>
                            <span>{(new Date(item.releaseDate).getFullYear())}</span>
                            <span>&middot;</span>
                            <span>{item.nation}</span>
                            <p className="ratingAvg"><i className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}</p>
                        </div>
                    </NavLink>
                </SwiperSlide>
            ))}
        </Swiper>
    );
}
