import React, { useEffect, useState } from "react";
import "./SearchContentDetailPage.css"
import { useSearchParams } from "react-router-dom";
import NotFound from "../NotFound/NotFound";
import { searchContentsDetail } from "../../API/SearchApi";
import SearchContentEach from "../../components/SearchContentEach/SearchContentEach";
import { toast } from "react-toastify";

const SearchContentDetailPage = () => {
    const [notFound, setNotFound] = useState(false);

    const [searchParams] = useSearchParams();
    const type = searchParams.get("type");
    const keyword = searchParams.get("keyword");

    const [data, setData] = useState(null);

    useEffect(() => {
        try {
            const fetchData = async () => {
                const response = await searchContentsDetail(type, keyword, 1);
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

    const handleMoreClick = async () => {
        try {
            const response = await searchContentsDetail(type, keyword, data.page.number + 2);
            setData(prevData => ({
                ...response.data,
                content: [...prevData.content, ...response.data.content],
                page: response.data.page
            }));
        } catch (error) {
            toast("데이터를 불러오는데 실패했습니다.");
        }
    }

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
                <p className="container"> 검색결과</p>
            </div>

            <div className="search-content-detail-page-wrap container">
                <p>{domainNameMap[type]}</p>

                <div className="search-content-list-wrap">
                    {data?.content.map(item =>
                        <div className="search-content-list" key={item.id}>
                            <SearchContentEach searchData={item}/>
                        </div>
                    )}
                </div>

                <div className="search-content-more-btn"><button onClick={handleMoreClick} hidden={data?.page.number + 2 > data?.page.totalPages}>더보기</button></div>

            </div>
        </div>
    );

};
export default SearchContentDetailPage;