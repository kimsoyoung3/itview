import React, {useEffect, useState} from "react";
import "./UserCommentPage.css"
import { useParams } from "react-router-dom";
import { getUserComment } from "../../API/UserApi";
import CommentCard from "../../components/CommentCard/CommentCard";
import NotFound from "../NotFound/NotFound";

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

    return (notFound ? <NotFound /> :
        <div className="user-comment-page container">
            <div className="user-comment-page-wrap">
                <h1>코멘트</h1>
                <div>
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


                    <div className="user-comment-page-select-box">
                        <select className="form-select user-comment-page-select"  aria-label="Default select example">
                            <option selected value="new">담은 순</option>
                            <option value="old">담은 역순</option>
                            <option value="my_score_high">1</option>
                            <option value="my_score_low">2</option>
                            <option value="avg_score_high">평균 별점 높은 순</option>
                            <option value="avg_score_low">평균 별점 낮은 순</option>
                        </select>
                    </div>
                </div>

                <div className="user-comment-tab-content">

                    {contentType === "MOVIE" && <div className="comment-page-tab comment-page-tab1">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}

                    {contentType === "SERIES" && <div className="comment-page-tab comment-page-tab2">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}


                    {contentType === "BOOK" && <div className="comment-page-tab comment-page-tab3">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}


                    {contentType === "WEBTOON" && <div className="comment-page-tab comment-page-tab4">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}


                    {contentType === "RECORD" && <div className="comment-page-tab comment-page-tab5">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}

                </div>
            </div>
        </div>
    );
}

export default UserCommentPage;