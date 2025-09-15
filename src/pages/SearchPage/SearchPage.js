import React from "react";
import "./SearchPage.css"

const SearchPage = () => {

    return (
        <div className="search-page">
            <div className="search-result">
                <p>검색결과입니다.</p>
            </div>

            <div className="search-page-wrap container">
                <div className="search-page-tab-title-wrap">
                    <div className="search-page-tab-title">
                        콘텐츠
                    </div>
                    <div className="search-page-tab-title">
                        인물
                    </div>
                    <div className="search-page-tab-title">
                        컬렉션
                    </div>
                    <div className="search-page-tab-title">
                        유저
                    </div>

                </div>
            </div>
        </div>
    );

};
export default SearchPage;