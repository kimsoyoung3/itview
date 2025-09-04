import React, { useEffect, useState } from "react";
import { getUserContentCount } from "../../API/UserApi";
import {NavLink, useParams} from "react-router-dom";
import NotFound from "../NotFound/NotFound";
import "./UserContentPage.css"

function UserContentPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = useState(false);

    const { id, contentType } = useParams();

    const domainNameMap = {
        "movie" : "영화",
        "series" : "시리즈",
        "book" : "책",
        "webtoon" : "웹툰",
        "record" : "음반"
    }
    
    const [userContentCount, setUserContentCount] = useState(null);

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

    return (notFound ? <NotFound /> :
        <div className="user-content-page container">
            <div className="user-content-page-wrap">
                <h1>{domainNameMap[contentType]}</h1>
                <div className="user-content-page-content">
                    <div>
                        <NavLink>
                            <p>평가</p>
                            <span>{userContentCount?.rating}</span>
                        </NavLink>
                    </div>
                    <div>
                        <NavLink>
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
