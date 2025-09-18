import React, { useEffect, useState } from "react";
import "./SearchContentDetailPage.css"
import { useSearchParams } from "react-router-dom";
import NotFound from "../NotFound/NotFound";
import { searchContentsDetail } from "../../API/SearchApi";
import SearchContentEach from "../../components/SearchContentEach/SearchContentEach";

const SearchContentDetailPage = () => {
    const [notFound, setNotFound] = useState(false);

    const [searchParams] = useSearchParams();
    const type = searchParams.get("type");
    const keyword = searchParams.get("keyword");

    const [data, setData] = useState(null);

    useEffect(() => {
        try {
            const fetchData = async () => {
                const response = await searchContentsDetail(type, keyword);
                setData(response.data);
            };
            fetchData();
        } catch (error) {
            setNotFound(true);
        }
    }, [type, keyword]);

    useEffect(() => {
            console.log(data);
    }, [data]);

    const domainNameMap = {
        "movie" : "영화",
        "series" : "시리즈",
        "book" : "책",
        "webtoon" : "웹툰",
        "record" : "음반"
    }

    return (notFound ? <NotFound /> :
        <div className="search-content-detail-page">
            <div className="search-result">
                <p className="container">"{keyword}" 검색결과</p>
            </div>

            <div className="search-content-detail-page-wrap container">
                <p>{domainNameMap[type]}</p>

                <div className="search-content-list-wrap">
                    {data?.content.map(item =>
                        <div className="search-content-list">
                            <SearchContentEach key={item.id} searchData={item}/>
                        </div>
                    )}
                </div>

                <div className="search-content-more-btn"><button>더보기</button></div>

            </div>
        </div>
    );

};
export default SearchContentDetailPage;