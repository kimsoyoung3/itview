import React, { useEffect, useState } from "react";
import "./SearchContentDetailPage.css"
import { useSearchParams } from "react-router-dom";
import NotFound from "../NotFound/NotFound";
import { searchContentsDetail } from "../../API/SearchApi";

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

    return (notFound ? <NotFound /> :
        <div className="search-content-detail-page">
            <div className="search-result">
                <p className="container"> 검색결과</p>
            </div>

            <div className="search-content-detail-page-wrap container">
                <p>영화</p>

                <div className="search-content-list-wrap">

                </div>
            </div>
        </div>
    );

};
export default SearchContentDetailPage;