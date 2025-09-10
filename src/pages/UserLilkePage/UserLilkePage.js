import { React, useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { getUserLikePerson } from '../../API/UserApi';
import "./UserLilkePage.css"

function UserLikePage() {
    const [notFound, setNotFound] = useState(false);

    const [searchParams] = new useSearchParams();
    const type = searchParams.get("type") || "person";

    const { id } = useParams();
    const [personLikes, setPersonLikes] = useState({});

    const [activeId, setActiveId] = useState("rating-page-tab1");


    useEffect(() => {
        try {
            const fetchData = async () => {
                const res = await getUserLikePerson(id, 1);
                setPersonLikes(res.data);
            }
            fetchData();
        } catch (error) {
            setNotFound(true);
        }
    }, [id]);

    useEffect(() => {
        console.log(personLikes);
    }, [personLikes]);

    return (
        <div className="user-like-page container">유저가 좋아요한 {type} 페이지 - 준비중
            <div className="user-like-page-wrap-page-wrap">
                <h1>좋아요</h1>
                {/*<div className="user-like-page-wrap-tab-title">
                    <div className={`comment-page-tab-btn ${activeId=== "movie" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="movie">인물</div>
                    <div className={`comment-page-tab-btn ${activeId === "series" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="series">컬렉션</div>
                    <div className={`comment-page-tab-btn ${activeId === "book" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="book">코멘트</div>

                    <span
                        className="comment-tab-indicator"
                        style={{
                            width: "20%",
                            transform: `translateX(${["movie","series","book","webtoon","record"].indexOf(contentType) * 100}%)`
                        }}
                    />
                </div>*/}
            </div>
        </div>
    );
}

export default UserLikePage;