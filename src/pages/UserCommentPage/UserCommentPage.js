import React, {useEffect, useState} from "react";
import "./UserCommentPage.css"
import { useParams } from "react-router-dom";
import { getUserComment } from "../../API/UserApi";
import CommentCard from "../../components/CommentCard/CommentCard";
import NotFound from "../NotFound/NotFound";

function UserCommentPage({ userInfo, openLogin }) {
    const [contentType, setContentType] = useState("movie");

    const { id } = useParams();

    const [notFound, setNotFound] = useState(false);

    const [commentList, setCommentList] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = {}
                res["movie"] = (await getUserComment(id, "movie", 1)).data;
                res["series"] = (await getUserComment(id, "series", 1)).data;
                res["book"] = (await getUserComment(id, "book", 1)).data;
                res["webtoon"] = (await getUserComment(id, "webtoon", 1)).data;
                res["record"] = (await getUserComment(id, "record", 1)).data;
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
                        <div className={`comment-page-tab-btn ${contentType === "movie" ? "active" : ""}`}
                             onClick={(e) => setContentType(e.target.id)} id="movie">영화</div>
                        <div className={`comment-page-tab-btn ${contentType === "series" ? "active" : ""}`}
                             onClick={(e) => setContentType(e.target.id)} id="series">시리즈</div>
                        <div className={`comment-page-tab-btn ${contentType === "book" ? "active" : ""}`}
                             onClick={(e) => setContentType(e.target.id)} id="book">책</div>
                        <div className={`comment-page-tab-btn ${contentType === "webtoon" ? "active" : ""}`}
                             onClick={(e) => setContentType(e.target.id)} id="webtoon">웹툰</div>
                        <div className={`comment-page-tab-btn ${contentType === "record" ? "active" : ""}`}
                             onClick={(e) => setContentType(e.target.id)} id="record">음반</div>

                        <span
                            className="comment-tab-indicator"
                            style={{
                                width: "20%",
                                transform: `translateX(${["movie","series","book","webtoon","record"].indexOf(contentType) * 100}%)`
                            }}
                        />
                    </div>

                    <div className="user-comment-page-select-box">
                        <select className="form-select user-comment-page-select"  aria-label="Default select example">
                            <option selected value="recent">작성 순</option>
                            <option value="like">좋아요 순</option>
                            <option value="reply">댓글 순</option>
                            <option value="rating">{Number(userInfo) === Number(id) ? "나의 별점 높은 순" : "이 회원의 별점 높은 순"}</option>
                            <option value="new">신작 순</option>
                        </select>
                    </div>
                </div>

                <div className="user-comment-tab-content">

                    {contentType === "movie" && <div className="comment-page-tab comment-page-tab1">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}

                    {contentType === "series" && <div className="comment-page-tab comment-page-tab2">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}


                    {contentType === "book" && <div className="comment-page-tab comment-page-tab3">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}


                    {contentType === "webtoon" && <div className="comment-page-tab comment-page-tab4">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}


                    {contentType === "record" && <div className="comment-page-tab comment-page-tab5">
                        {commentList[contentType]?.content?.length > 0 ? (
                            <div className="comment-page-content">
                                {commentList[contentType]?.content?.map((c) => (<CommentCard key={c.comment.id} comment={c.comment} content={c.content} userInfo={userInfo} openLogin={openLogin}/>))}
                            </div>
                        ) : (
                            <p>코멘트가 없습니다 :)</p>
                        )}
                    </div>}

                    {commentList[contentType]?.content?.length > 0 && (
                        <div className="comment-page-tab-btn-box">
                        <button className="comment-page-tab-content-btn">더보기</button>
                        </div>
                    )}


                </div>
            </div>
        </div>
    );
}

export default UserCommentPage;