import React, {useEffect, useState} from "react";
import "./UserCommentPage.css"
import { useParams } from "react-router-dom";
import { getUserComment } from "../../API/UserApi";
import CommentCard from "../../components/CommentCard/CommentCard";
import NotFound from "../NotFound/NotFound";
import CustomSelect from "../../components/CustomSelect/CustomSelect";

function UserCommentPage({userInfo, openLogin}) {
    /* 선택된 콘텐츠 타입 & 정렬 옵션 */
    const [contentType, setContentType] = useState("movie");
    const [order, setOrder] = useState("recent");

    /* URL 파라미터 */
    const {id} = useParams();

    /* 에러 상태 */
    const [notFound, setNotFound] = useState(false);

    /* 댓글 리스트 */
    const [commentList, setCommentList] = useState({});

    /* 정렬 옵션 */
    const options = [
        {value: "recent", label: "작성 순"},
        {value: "like", label: "좋아요 순"},
        {value: "reply", label: "댓글 순"},
        {value: "rating", label: Number(userInfo?.userId) === Number(id) ? "나의 별점 낮은 순" : "이 회원의 별점 낮은 순"},
        {value: "new", label: "신작 순"},
    ];

    /* 데이터 불러오기 */
    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = {}
                res["movie"] = (await getUserComment(id, "movie", 1, order)).data;
                res["series"] = (await getUserComment(id, "series", 1, order)).data;
                res["book"] = (await getUserComment(id, "book", 1, order)).data;
                res["webtoon"] = (await getUserComment(id, "webtoon", 1, order)).data;
                res["record"] = (await getUserComment(id, "record", 1, order)).data;
                setCommentList(res);
            } catch (error) {
                setNotFound(true);
            }
        }
        fetchData();
    }, [id, order]);

    useEffect(() => {
        console.log(commentList);
    }, [commentList]);

    /* 더 불러오기 */
    const handleClickMore = async () => {
        if (commentList[contentType]?.page?.number < commentList[contentType]?.page?.totalPages - 1) {
            const response = await getUserComment(id, contentType, commentList[contentType]?.page?.number + 2, order);
            setCommentList((prev) => ({
                ...prev,
                [contentType]: {
                    content: [...prev[contentType].content, ...response.data.content],
                    page: response.data.page
                }
            }));
        }
    }

    /* 댓글 삭제 후 목록 갱신 */
    const onDelete = async () => {
        let newCommentList = [];
        for (let i = 0; i < commentList[contentType].page.number + 1; i++) {
            const res = (await getUserComment(id, contentType, i + 1, order));
            newCommentList = [...newCommentList, ...res.data.content];
        }
        setCommentList((prev) => ({
            ...prev,
            [contentType]: {
                content: newCommentList,
                page: prev[contentType].page
            }
        }));
    }

    return (notFound ? <NotFound/> :
            <div className="user-comment-page">
                <div className="user-comment-page-wrap container">
                    <h1>코멘트</h1>
                    <div>
                        {/*탭 버튼*/}
                        <div className="user-comment-tab-title">
                            <div className={`comment-page-tab-btn ${contentType === "movie" ? "active" : ""}`}
                                 onClick={() => setContentType('movie')}>영화
                            </div>
                            <div className={`comment-page-tab-btn ${contentType === "series" ? "active" : ""}`}
                                 onClick={() => setContentType('series')}>시리즈
                            </div>
                            <div className={`comment-page-tab-btn ${contentType === "book" ? "active" : ""}`}
                                 onClick={() => setContentType('book')}>책
                            </div>
                            <div className={`comment-page-tab-btn ${contentType === "webtoon" ? "active" : ""}`}
                                 onClick={() => setContentType('webtoon')}>웹툰
                            </div>
                            <div className={`comment-page-tab-btn ${contentType === "record" ? "active" : ""}`}
                                 onClick={() => setContentType('record')}>음반
                            </div>

                            <span
                                className="comment-tab-indicator"
                                style={{
                                    width: "20%",
                                    transform: `translateX(${["movie", "series", "book", "webtoon", "record"].indexOf(contentType) * 100}%)`
                                }}
                            />
                        </div>

                        {/*셀렉트 박스*/}
                        <div className="user-comment-page-select-box">
                            <CustomSelect value={order} onChange={setOrder} options={options}/>
                        </div>
                    </div>

                    {/*탭 내용*/}
                    <div className="user-comment-tab-content">

                        {contentType === "movie" && <div className="comment-page-tab comment-page-tab1">
                            {commentList[contentType]?.content?.length > 0 ? (
                                <div className="comment-page-content">
                                    {commentList[contentType]?.content?.map((c) => (
                                        <CommentCard key={c.comment.id} comment={c.comment} content={c.content}
                                                     userInfo={userInfo} openLogin={openLogin} onDelete={onDelete}/>))}
                                </div>
                            ) : (
                                <p>코멘트가 없습니다 :)</p>
                            )}
                        </div>}

                        {contentType === "series" && <div className="comment-page-tab comment-page-tab2">
                            {commentList[contentType]?.content?.length > 0 ? (
                                <div className="comment-page-content">
                                    {commentList[contentType]?.content?.map((c) => (
                                        <CommentCard key={c.comment.id} comment={c.comment} content={c.content}
                                                     userInfo={userInfo} openLogin={openLogin} onDelete={onDelete}/>))}
                                </div>
                            ) : (
                                <p>코멘트가 없습니다 :)</p>
                            )}
                        </div>}


                        {contentType === "book" && <div className="comment-page-tab comment-page-tab3">
                            {commentList[contentType]?.content?.length > 0 ? (
                                <div className="comment-page-content">
                                    {commentList[contentType]?.content?.map((c) => (
                                        <CommentCard key={c.comment.id} comment={c.comment} content={c.content}
                                                     userInfo={userInfo} openLogin={openLogin} onDelete={onDelete}/>))}
                                </div>
                            ) : (
                                <p>코멘트가 없습니다 :)</p>
                            )}
                        </div>}


                        {contentType === "webtoon" && <div className="comment-page-tab comment-page-tab4">
                            {commentList[contentType]?.content?.length > 0 ? (
                                <div className="comment-page-content">
                                    {commentList[contentType]?.content?.map((c) => (
                                        <CommentCard key={c.comment.id} comment={c.comment} content={c.content}
                                                     userInfo={userInfo} openLogin={openLogin} onDelete={onDelete}/>))}
                                </div>
                            ) : (
                                <p>코멘트가 없습니다 :)</p>
                            )}
                        </div>}


                        {contentType === "record" && <div className="comment-page-tab comment-page-tab5">
                            {commentList[contentType]?.content?.length > 0 ? (
                                <div className="comment-page-content">
                                    {commentList[contentType]?.content?.map((c) => (
                                        <CommentCard key={c.comment.id} comment={c.comment} content={c.content}
                                                     userInfo={userInfo} openLogin={openLogin} onDelete={onDelete}/>))}
                                </div>
                            ) : (
                                <p>코멘트가 없습니다 :)</p>
                            )}
                        </div>}

                        {commentList[contentType]?.content?.length > 0 && (
                            <div className="comment-page-tab-btn-box">
                                <button className="comment-page-tab-content-btn" onClick={handleClickMore}
                                        style={{display: commentList[contentType]?.page?.number + 1 === commentList[contentType]?.page?.totalPages ? "none" : ""}}>더보기
                                </button>
                            </div>
                        )}


                    </div>
                </div>
            </div>
    );
}

export default UserCommentPage;