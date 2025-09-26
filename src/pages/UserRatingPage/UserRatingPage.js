import React, { useEffect, useState } from 'react';
import {NavLink, useParams} from 'react-router-dom';
import { getUserRatingCount } from '../../API/UserApi';
import NotFound from '../NotFound/NotFound';
import "./UserRatingPage.css"
import CollectionCard from "../../components/CollectionCard/CollectionCard";

function NavKink() {
    return null;
}

function UserRatingPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = useState(false);

    const { id } = useParams();

    const [userRatingCount, setUserRatingCount] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getUserRatingCount(id);
                setUserRatingCount(res.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(userRatingCount);
    }, [userRatingCount]);

    return (notFound ? <NotFound /> :
        <div className="user-rating-page">
            <div className="user-rating-page-wrap container">
                <h1>평가</h1>
                <ul className="rating-list">
                    <li><NavLink to={`/user/${id}/content/movie/rating`}>영화 <span>{userRatingCount?.movie}</span></NavLink></li>
                    <li><NavLink to={`/user/${id}/content/series/rating`}>시리즈 <span>{userRatingCount?.series}</span></NavLink></li>
                    <li><NavLink to={`/user/${id}/content/book/rating`}>책 <span>{userRatingCount?.book}</span></NavLink></li>
                    <li><NavLink to={`/user/${id}/content/webtoon/rating`}>웹툰 <span>{userRatingCount?.webtoon}</span></NavLink></li>
                    <li><NavLink to={`/user/${id}/content/record/rating`}>음반 <span>{userRatingCount?.record}</span></NavLink></li>
                </ul>
            </div>
        </div>
    )
}

export default UserRatingPage;