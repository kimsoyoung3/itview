import React, { useEffect, useState } from "react";
import { getUserContentCount } from "../../API/UserApi";
import {NavLink, useParams} from "react-router-dom";
import NotFound from "../NotFound/NotFound";
import "./UserContentPage.css"

function UserContentPage({userInfo, openLogin}) {
    /* 에러 상태 */
    const [notFound, setNotFound] = useState(false);

    /* URL 파라미터 */
    const {id, contentType} = useParams();

    /* 도메인명 매핑 */
    const domainNameMap = {
        "movie": "영화",
        "series": "시리즈",
        "book": "책",
        "webtoon": "웹툰",
        "record": "음반"
    }

    /* 유저 콘텐츠 개수 */
    const [userContentCount, setUserContentCount] = useState(null);

    /* 데이터 불러오기 */
    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getUserContentCount(id, contentType);
                setUserContentCount(res.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id, contentType]);

    useEffect(() => {
        console.log(userContentCount);
    }, [userContentCount]);

    return (notFound ? <NotFound/> :
            <div className="user-content-page">
                <div className="user-content-page-wrap container">
                    <h1>{domainNameMap[contentType]}</h1>
                    <div className="user-content-page-content">
                        <div>
                            <NavLink to={`/user/${id}/content/${contentType}/rating`}>
                                <p>평가</p>
                                <span>{userContentCount?.rating}</span>
                            </NavLink>
                        </div>
                        <div>
                            <NavLink to={`/user/${id}/content/${contentType}/wish`}>
                                <p>보고싶어요</p>
                                <span>{userContentCount?.wishlist}</span>
                            </NavLink>
                        </div>

                    </div>
                </div>
            </div>
    );
}

export default UserContentPage;
