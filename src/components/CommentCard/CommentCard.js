import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CommentCard.css"; // CSS 따로 관리
import { likeComment, postReply, unlikeComment } from "../../API/CommentApi";
import {NavLink} from "react-router-dom";

const CommentCard = ({comment, content, userInfo, openLogin, newReply, clamp = false}) => {
    const [commentData, setCommentData] = useState(null);

    useEffect(() => {
        setCommentData(comment)
    }, [comment]);

    const [commentModal, setCommentModal] = useState();

    /*코멘트 모달*/
    const openComment = () => setCommentModal(true);
    const closeComment = () => setCommentModal(false);

    const [replyModal, setReplyModal] = useState();
    const textRef = React.useRef(null); //댓글 텍스트 영역 참조

    /*댓글 모달*/
    const openReply = () => setReplyModal(true);
    const closeReply = () => setReplyModal(false);

    /*코멘트 삭제 확인 모달창*/
    const [deleteCommentModal, setDeleteCommentModal] = useState(false);

    const closeDeleteCommentModal = () => setDeleteCommentModal(false)

    const handleDeleteCommentClick = () => {
        setDeleteCommentModal(true);
    };

    if (!comment) return null;

    const handleLikeComment = async (commentId) => {
        if (commentData.liked) {
            const res = await unlikeComment(commentId);
            if (res.status === 200) {
                setCommentData((prev) => ({
                    ...prev,
                    liked: false,
                    likeCount: prev.likeCount - 1,
                }));
            } else {
                console.error("Failed to unlike comment");
            }
        } else {
            const res = await likeComment(commentId);
            if (res.status === 200) {
                setCommentData((prev) => ({
                    ...prev,
                    liked: true,
                    likeCount: prev.likeCount + 1,
                }));
            } else {
                console.error("Failed to like comment");
            }
        }
    };

    const handleReplySubmit = async () => {
        const text = textRef.current.value;
        if (text) {
            const res = await postReply(commentData.id, { text });
            if (res.status === 200) {
                alert("댓글이 등록되었습니다.");
                if (newReply) {
                    newReply(res.data);
                }
            } else {
                alert("댓글 등록에 실패했습니다. 다시 시도해주세요.");
            }
        }
        closeReply();
    };

    return (
        <div className="comment-card container">
            {/* 헤더 */}
            <div className="comment-card-header">
                <div className="comment-card-header-left">
                    <div className="comment-card-profile" ><img src={commentData?.user?.profile || '/user.png'}alt=""/></div>
                    <span className="comment-card-nickname">{commentData?.user.nickname}</span>
                    <span className="comment-card-date">{new Date(commentData?.createdAt).toLocaleDateString().slice(0, -1)}</span>
                </div>
                {commentData?.rating &&
                    <div className="comment-card-header-right">
                        <i className="bi bi-star-fill"/><span>{commentData?.rating/2}</span>
                    </div>
                }
            </div>

            {/* 내용 */}
            <div className="comment-card-content">
                {content && (
                    <div className="comment-card-content-wrap">
                        <div className="comment-card-content-left">
                            <NavLink to={`/content/${content.id}`}><img src={content.poster} alt=""/></NavLink>
                        </div>
                        <ul className="comment-card-content-right m-0 p-0">
                            <li>{content.title}</li>
                            <li>{content.contentType} &middot; <span>{content.releaseDate}</span></li>
                            <li>평균 <i className="bi bi-star-fill"/>{content.ratingAvg.toFixed(1)}</li>
                        </ul>
                    </div>
                )}
                <NavLink to={`/comment/${commentData?.id}`}>
                    <p className={clamp ? "clamp-4" : ""}>{commentData?.text}</p>
                </NavLink>
            </div>

            {/* 푸터 */}
            <div className="comment-card-footer">
                <div className="comment-card-footer-top">
                    <p>좋아요<span>{commentData?.likeCount}</span></p>
                    <p>댓글<span>{commentData?.replyCount}</span></p>
                </div>
                <div className="comment-card-footer-btn">
                    <div>
                        <button onClick={userInfo ? () => handleLikeComment(commentData?.id) : openLogin}>
                            <i className={commentData?.liked ? "bi bi-hand-thumbs-up-fill" : "bi bi-hand-thumbs-up"}/>
                        </button>
                        <button onClick={userInfo ? openReply : openLogin}><i className="bi bi-chat-square"/></button>
                        <button onClick={() => {navigator.clipboard.writeText("http://localhost:3000/comment/" + commentData?.id)}}><i className="bi bi-share"/></button>
                    </div>
                    {userInfo === commentData?.user?.id && clamp === false && (
                    <div>
                        <button onClick={handleDeleteCommentClick}>
                            <i className="bi bi-trash"></i>
                        </button>
                        <button onClick={openComment}>
                            <i className="bi bi-pencil"></i>
                        </button>
                    </div>
                    )}
                </div>
            </div>

            {/*마이코멘트 모달창*/}
            {commentModal && (
                <div className="comment-modal-overlay" onClick={closeComment}>
                    <div className="comment-modal-content">
                        <div className="comment-content-top">
                            <p className="comment-modal-title">{content.title}</p>
                            <button className="comment-close-button" onClick={closeComment}><img src="/x-lg.svg" alt=""/></button>
                        </div>
                        <textarea rows="15" placeholder="작품에 대한 코멘트를 남겨주세요." maxLength={1000} ref={textRef}></textarea>
                        <div className="comment-content-bottom">
                            <button className="comment-content-btn">수정</button>
                        </div>
                    </div>
                </div>
            )}

            {/*코멘트 삭제 확인 모달창*/}
            {deleteCommentModal && (
                <div className="confirm-modal-overlay" onClick={closeDeleteCommentModal}>
                    <div className="confirm-modal-content">
                        <p>알림</p>
                        <p>삭제하시겠습니까?</p>
                        <div className="confirm-btn-group">
                            <button>확인</button>
                            <button  onClick={closeDeleteCommentModal}>취소</button>
                        </div>
                    </div>
                </div>
            )}

            {/*댓글 모달창*/}
            {replyModal && (
                <div className="comment-modal-overlay" onClick={closeReply}>
                    <div className="comment-modal-content">
                        <div className="comment-content-top">
                            <p className="comment-modal-title">댓글</p>
                            <button className="comment-close-button" onClick={closeReply}><img src="/x-lg.svg" alt=""/></button>
                        </div>
                        <textarea rows="15" placeholder="코멘트에 대한 댓글을 남겨주세요." maxLength={1000} ref={textRef}></textarea>
                        <div className="comment-content-bottom">
                            <button className="comment-content-btn"
                                    onClick={handleReplySubmit}>저장</button>
                        </div>
                    </div>
                </div>
            )}



        </div>
    );
};

export default CommentCard;
