import React, { useEffect, useState, useRef } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CommentCard.css";
import {
    deleteComment,
    likeComment,
    unlikeComment,
    postReply,
    updateComment
} from "../../API/CommentApi";
import { NavLink, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const CommentCard = ({ comment, content, userInfo, openLogin, newReply, onDelete, clamp = false }) => {
    const [commentData, setCommentData] = useState(comment);
    const [contentData, setContentData] = useState(content);

    const [commentModal, setCommentModal] = useState(false); /* 코멘트 수정 모달 */
    const [replyModal, setReplyModal] = useState(false);     /* 댓글 작성 모달 */
    const [deleteCommentModal, setDeleteCommentModal] = useState(false); /* 코멘트 삭제 확인 모달 */

    const commentTextRef = useRef(null); /* 코멘트 수정 textarea */
    const replyTextRef = useRef(null);   /* 댓글 textarea */

    const navigate = useNavigate();

    /* prop 변경 시 state 업데이트 */
    useEffect(() => {
        setCommentData(comment);
        setContentData(content);
    }, [comment, content]);

    /* 코멘트 수정 모달 열 때 기존 코멘트 내용 세팅 */
    useEffect(() => {
        if (commentTextRef.current && commentData) {
            commentTextRef.current.value = commentData.text;
        }
    }, [commentData, commentModal]);

    /* 모달 관련 함수 */
    const openComment = () => setCommentModal(true);
    const closeComment = () => setCommentModal(false);

    const openReply = () => setReplyModal(true);
    const closeReply = () => setReplyModal(false);

    const closeDeleteCommentModal = () => setDeleteCommentModal(false);
    const handleDeleteCommentClick = () => setDeleteCommentModal(true);

    /* 코멘트 수정 */
    const handleCommentUpdate = async () => {
        const text = commentTextRef.current.value;
        if (!text) return;

        try {
            await updateComment(commentData.id, { text });
            toast("코멘트가 수정되었습니다.");
            setCommentData(prev => ({ ...prev, text }));
            closeComment();
        } catch (error) {
            toast(error.response?.data || "코멘트 수정 실패");
        }
    };

    /* 코멘트 삭제 */
    const handleDeleteComment = async () => {
        try {
            const response = await deleteComment(commentData.id);
            if (response.status === 200) onDelete();
            else console.error("Failed to delete comment");
        } catch (error) {
            console.error("Error deleting comment:", error);
        }
        closeDeleteCommentModal();
    };

    /* 댓글 좋아요/취소 */
    const handleLikeComment = async (commentId) => {
        if (!userInfo) return openLogin();

        try {
            if (commentData.liked) {
                await unlikeComment(commentId);
                setCommentData(prev => ({ ...prev, liked: false, likeCount: prev.likeCount - 1 }));
            } else {
                await likeComment(commentId);
                setCommentData(prev => ({ ...prev, liked: true, likeCount: prev.likeCount + 1 }));
            }
        } catch (error) {
            toast(error.response?.data || "좋아요 처리 실패");
        }
    };

    /* 댓글 작성 */
    const handleReplySubmit = async () => {
        const text = replyTextRef.current.value;
        if (!text) return;

        try {
            const res = await postReply(commentData.id, { text });
            toast("댓글이 등록되었습니다.");
            if (newReply) newReply(res.data);
        } catch (error) {
            toast(error.response?.data || "댓글 등록 실패");
        }
        closeReply();
    };

    if (!commentData) return null;

    return (
        <div className="comment-card">

            {/* 헤더 */}
            <NavLink to={`/user/${commentData.user?.id}`} className="comment-card-header">
                <div className="comment-card-header-left">
                    <div className="comment-card-profile">
                        <img src={commentData.user?.profile || `${process.env.PUBLIC_URL}/user.png`} alt="프로필"/>
                    </div>
                    <span className="comment-card-nickname">{commentData.user?.nickname}</span>
                    <span className="comment-card-date">
                        {new Date(commentData.createdAt).toLocaleDateString().slice(0, -1)}
                    </span>
                </div>
                {commentData.rating &&
                    <div className="comment-card-header-right">
                        <i className="bi bi-star-fill"/>
                        <span>{commentData.rating / 2}</span>
                    </div>
                }
            </NavLink>

            {/* 내용 */}
            <div className="comment-card-content">
                {contentData?.id && (
                    <div className="comment-card-content-wrap">
                        <div className="comment-card-content-left">
                            <NavLink to={`/content/${contentData.id}`}>
                                <img src={contentData.poster} alt="포스터"/>
                            </NavLink>
                        </div>
                        <ul className="comment-card-content-right m-0 p-0">
                            <li>{contentData.title}</li>
                            <li>{contentData.contentType} &middot; <span>{contentData.releaseDate}</span></li>
                            <li>평균 <i className="bi bi-star-fill"/>
                                {contentData.ratingAvg ? (contentData.ratingAvg / 2).toFixed(1) : 0}
                            </li>
                        </ul>
                    </div>
                )}
                <NavLink to={`/comment/${commentData.id}`}>
                    <p className={clamp ? "clamp-4" : ""}>{commentData.text}</p>
                </NavLink>
            </div>

            {/* 푸터 */}
            <div className="comment-card-footer">
                <div className="comment-card-footer-top">
                    <p>좋아요<span>{commentData.likeCount}</span></p>
                    <p>댓글<span>{commentData.replyCount}</span></p>
                </div>
                <div className="comment-card-footer-btn">
                    <div>
                        <button onClick={() => handleLikeComment(commentData.id)}>
                            <i className={commentData.liked ? "bi bi-heart-fill" : "bi bi-heart"}/>
                        </button>
                        <button onClick={userInfo ? openReply : openLogin}><i className="bi bi-chat-square"/></button>
                        <button onClick={() => {
                            navigator.clipboard.writeText(`http://localhost:3000/comment/${commentData.id}`)
                                .then(() => toast("링크가 복사되었습니다."));
                        }}>
                            <i className="bi bi-share"/>
                        </button>
                    </div>
                    {userInfo === commentData.user?.id && !clamp && (
                        <div>
                            <button onClick={handleDeleteCommentClick}><i className="bi bi-trash"/></button>
                            <button onClick={openComment}><i className="bi bi-pencil"/></button>
                        </div>
                    )}
                </div>
            </div>

            {/* 코멘트 수정 모달 */}
            {commentModal && (
                <div className="comment-modal-overlay" onClick={closeComment}>
                    <div className="comment-modal-content" onClick={e => e.stopPropagation()}>
                        <div className="comment-content-top">
                            <p className="comment-modal-title">{content.title}</p>
                            <button className="comment-close-button" onClick={closeComment}>
                                <img src={`${process.env.PUBLIC_URL}/icon/x-lg.svg`} alt="닫기"/>
                            </button>
                        </div>
                        <textarea
                            rows="15"
                            placeholder="작품에 대한 코멘트를 남겨주세요."
                            maxLength={1000}
                            ref={commentTextRef}
                        />
                        <div className="comment-content-bottom">
                            <button className="comment-content-btn" onClick={handleCommentUpdate}>수정</button>
                        </div>
                    </div>
                </div>
            )}

            {/* 코멘트 삭제 확인 모달 */}
            {deleteCommentModal && (
                <div className="confirm-modal-overlay" onClick={closeDeleteCommentModal}>
                    <div className="confirm-modal-content" onClick={e => e.stopPropagation()}>
                        <p>알림</p>
                        <p>삭제하시겠습니까?</p>
                        <div className="confirm-btn-group">
                            <button onClick={handleDeleteComment}>확인</button>
                            <button onClick={closeDeleteCommentModal}>취소</button>
                        </div>
                    </div>
                </div>
            )}

            {/* 댓글 작성 모달 */}
            {replyModal && (
                <div className="comment-modal-overlay" onClick={closeReply}>
                    <div className="comment-modal-content" onClick={e => e.stopPropagation()}>
                        <div className="comment-content-top">
                            <p className="comment-modal-title">댓글</p>
                            <button className="comment-close-button" onClick={closeReply}>
                                <img src={`${process.env.PUBLIC_URL}/icon/x-lg.svg`} alt="닫기"/>
                            </button>
                        </div>
                        <textarea
                            rows="15"
                            placeholder="코멘트에 대한 댓글을 남겨주세요."
                            maxLength={1000}
                            ref={replyTextRef}
                        />
                        <div className="comment-content-bottom">
                            <button className="comment-content-btn" onClick={handleReplySubmit}>저장</button>
                        </div>
                    </div>
                </div>
            )}

        </div>
    );
};

export default CommentCard;
