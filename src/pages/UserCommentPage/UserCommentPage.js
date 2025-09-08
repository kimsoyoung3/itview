import React, {useEffect, useState} from "react";
import "./UserCommentPage.css"
import { useParams } from "react-router-dom";
import { getUserComment } from "../../API/UserApi";

function UserCommentPage({ userInfo, openLogin }) {
    const [contentType, setContentType] = useState("MOVIE");

    const { id } = useParams();

    const [notFound, setNotFound] = useState(false);

    const [commentList, setCommentList] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = {}
                res["MOVIE"] = (await getUserComment(id, "MOVIE", 1)).data;
                res["SERIES"] = (await getUserComment(id, "SERIES", 1)).data;
                res["BOOK"] = (await getUserComment(id, "BOOK", 1)).data;
                res["WEBTOON"] = (await getUserComment(id, "WEBTOON", 1)).data;
                res["RECORD"] = (await getUserComment(id, "RECORD", 1)).data;
                setCommentList(res);
            } catch (error) {
                setNotFound(true);
            }
        }
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(commentList);
    }, [commentList]);

    return (
        <div className="user-comment-page container">
            <div className="user-comment-page-wrap">
                <h1>코멘트</h1>
                <div className="user-comment-tab-title">
                    <div className={`comment-page-tab-btn ${contentType === "MOVIE" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="MOVIE">영화</div>
                    <div className={`comment-page-tab-btn ${contentType === "SERIES" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="SERIES">시리즈</div>
                    <div className={`comment-page-tab-btn ${contentType === "BOOK" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="BOOK">책</div>
                    <div className={`comment-page-tab-btn ${contentType === "WEBTOON" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="WEBTOON">웹툰</div>
                    <div className={`comment-page-tab-btn ${contentType === "RECORD" ? "active" : ""}`}
                         onClick={(e) => setContentType(e.target.id)} id="RECORD">음반</div>

                    <span
                        className="comment-tab-indicator"
                        style={{
                            width: "20%",
                            transform: `translateX(${["MOVIE","SERIES","BOOK","WEBTOON","RECORD"].indexOf(contentType) * 100}%)`
                        }}
                    />
                </div>

                <div className="user-comment-tab-content">

                    {contentType === "MOVIE" && <div className="comment-page-tab1">
                        <p>탭1</p>
                    </div>}

                    {contentType === "SERIES" && <div className="comment-page-tab2">
                        <p>탭2</p>
                    </div>}


                    {contentType === "BOOK" && <div className="comment-page-tab3">
                        <p>탭3</p>
                    </div>}


                    {contentType === "WEBTOON" && <div className="comment-page-tab4">
                        <p>탭4</p>
                    </div>}


                    {contentType === "RECORD" && <div className="comment-page-tab5">
                        <p>탭5</p>
                    </div>}

                </div>
            </div>
        </div>
    );
}

export default UserCommentPage;