import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css"; // CSS 따로 관리

const DetailPage = () => {
    return (
        <div className="detail-banner">
            <div className="banner-post">
                <img src="" alt=""/>
            </div>

            <ul className="post-dec">
                <li>영화 제목</li>
                <li>영화 설명 칸</li>
                <li><span>개봉년도</span>&middot;<span>장르</span>&middot;<span>국가</span></li>
                <li></li>
            </ul>
        </div>
    )
}
export default DetailPage;