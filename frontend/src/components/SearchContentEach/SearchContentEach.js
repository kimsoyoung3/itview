import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./SearchContentEach.css";
import { NavLink } from "react-router-dom";

const SearchContentEach = ({ searchData }) => {

    return (
        <div className="search-content-each">

            {/* 포스터 이미지 */}
            <div className="search-content-each-post">
                <NavLink to={`/content/${searchData?.id}`}>
                    <img
                        src={searchData?.poster}
                        alt={searchData?.title}
                    />
                </NavLink>
            </div>

            {/* 콘텐츠 제목 및 평점 */}
            <div className="search-page-content-each-info">
                <div>
                    <div className="search-content-each-info-title">{searchData?.title}</div>
                    <div><span>{(new Date(searchData?.releaseDate).getFullYear())}</span><span>{searchData?.nation}</span></div>
                </div>
            </div>
        </div>
    );
};

export default SearchContentEach;
