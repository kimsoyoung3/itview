import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css";
import {NavLink} from "react-router-dom"; // CSS 따로 관리

const DetailPage = () => {
    return (
        <div className="detail">
            <div className="detail-banner">
                <div className="banner-post">
                    <img src="/image.jpg" alt=""/>
                </div>

                <div className="post-dec container">
                    <ul className="">
                        <li>영화 제목</li>
                        <li>영화 설명 칸</li>
                        <li><span>개봉년도</span> &middot; <span>장르</span> &middot; <span>국가</span></li>
                        <li><span>영화시간</span >&middot; <span>나이제한</span></li>
                    </ul>
                </div>

                <NavLink to="/" className=""></NavLink>

            </div>

            <div className="detail-content container"></div>
        </div>
    )
}
export default DetailPage;