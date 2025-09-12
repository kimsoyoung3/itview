import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./ContentEach.css";
import { NavLink } from "react-router-dom";

const ContentEach = ({ ratingData, ratingType, clamp }) => {

    return (
        <div className="content-each">

            {/* 포스터 이미지 */}
            <div className="content-each-post">
                <NavLink to={`/content/${ratingData?.content.id}`}>
                    <img
                        src={ratingData?.content.poster}
                        alt={ratingData?.content.title}
                    />
                </NavLink>
            </div>

            {/* 콘텐츠 제목 및 평점 */}
            <div className="content-each-info">
                <p className="content-each-info-title">{ratingData?.content.title}</p>
                <p className={
                    ratingType === "user"
                        ? "content-each-rating-text content-each-rating-text-user"
                        : "content-each-rating-text content-each-rating-text-avg"
                }>
                    {ratingType === 'user' ? '평가함' : '평균'}
                    <i className="bi bi-star-fill"/>
                    {ratingType === 'user'
                        ? ratingData?.score / 2
                        : (ratingData?.content.ratingAvg / 2).toFixed(1)
                    }
                </p>
                <p className={clamp ? "content-each-content-type" : "content-each-content-type-none"}>{ratingData?.content.contentType}</p>
            </div>

        </div>
    );
};

export default ContentEach;
