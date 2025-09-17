import 'bootstrap/dist/css/bootstrap.min.css';
import "./ReplyCard.css";
import React, { useEffect, useState, useRef } from 'react';
import { deleteReply, likeReply, unlikeReply, updateReply } from '../../API/ReplyApi';
import { toast } from "react-toastify";
import { NavLink } from 'react-router-dom';

const ReplyCard = ({ reply, userInfo, openLogin }) => {
    const [replyData, setReplyData] = useState(reply);
    const textRef = useRef(null); // 댓글 텍스트 영역 참조

    /* 댓글 수정 모달 */
    const [replyUpdateModal, setReplyUpdateModal] = useState(false);
    const openUpdateReply = () => setReplyUpdateModal(true);
    const closeUpdateReply = () => setReplyUpdateModal(false);

    /* 댓글 삭제 확인 모달 */
    const [replyDeleteConfirmModal, setReplyDeleteConfirmModal] = useState(false);
    const openReplyDeleteConfirm = () => setReplyDeleteConfirmModal(true);
    const closeReplyDeleteConfirmModal = () => setReplyDeleteConfirmModal(false);

    /* reply prop 업데이트 시 state 반영 */
    useEffect(() => {
        setReplyData(reply);
    }, [reply]);

    useEffect(() => {
        if (textRef.current && replyData) {
            textRef.current.value = replyData.text;
        }
    }, [replyData, replyUpdateModal]);

    /* 댓글 수정 */
    const handleReplyUpdate = async () => {
        const text = textRef.current.value;
        if (text) {
            try {
                await updateReply(replyData.id, { text });
                toast("댓글이 수정되었습니다.");
                setReplyData((prev) => ({ ...prev, text }));
                closeUpdateReply();
            } catch (error) {
                toast(error.response?.data || "댓글 수정 실패");
            }
        }
    };

    /* 댓글 삭제 */
    const handleReplyDelete = async () => {
        try {
            await deleteReply(replyData.id);
            toast("댓글이 삭제되었습니다.");
            setReplyData(null);
        } catch (error) {
            toast(error.response?.data || "댓글 삭제 실패");
        }
    };

    /* 좋아요 토글 */
    const handleLike = async () => {
        if (!replyData) return;
        if (replyData.liked) {
            try {
                await unlikeReply(replyData.id);
                setReplyData((prev) => ({ ...prev, liked: false, likeCount: prev.likeCount - 1 }));
            } catch (error) {
                toast(error.response?.data || "좋아요 취소 실패");
            }
        } else {
            try {
                await likeReply(replyData.id);
                setReplyData((prev) => ({ ...prev, liked: true, likeCount: prev.likeCount + 1 }));
            } catch (error) {
                toast(error.response?.data || "좋아요 실패");
            }
        }
    };

    if (!replyData) return null;

    return (
        <div className="reply-card container">
            <div className="reply-card-wrap">

                {/* 좌측: 댓글 정보 */}
                <div className="reply-card-inner-left">
                    <NavLink to={`/user/${replyData.user?.id}`} className="reply-card-info">
                        <div className="reply-card-info-profile">
                            <img src={replyData.user?.profile || `${process.env.PUBLIC_URL}/user.png`} alt="프로필"/>
                        </div>
                        <span>{replyData.user?.nickname}</span>
                        <span>{new Date(replyData.createdAt).toLocaleDateString().slice(0, -1)}</span>
                    </NavLink>
                    <p className="reply-card-text">{replyData.text}</p>

                    {/* 좋아요 */}
                    <div className="reply-card-footer">
                        <button onClick={userInfo ? handleLike : openLogin} className="like-button">
                            <i className={replyData.liked ? "bi bi-heart-fill" : "bi bi-heart"} />
                        </button>
                        <p>좋아요 <span>{replyData.likeCount}</span></p>
                    </div>
                </div>

                {/* 우측: 수정/삭제 버튼 */}
                {userInfo === replyData.user?.id && (
                    <div className="reply-card-inner-right">
                        <button onClick={openReplyDeleteConfirm}><i className="bi bi-trash"></i></button>
                        <button onClick={openUpdateReply}><i className="bi bi-pencil"></i></button>
                    </div>
                )}
            </div>

            {/* 댓글 수정 모달 */}
            {replyUpdateModal && (
                <div className="comment-modal-overlay" onClick={closeUpdateReply}>
                    <div className="comment-modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="comment-content-top">
                            <p className="comment-modal-title">댓글</p>
                            <button className="comment-close-button" onClick={closeUpdateReply}>
                                <img src={`${process.env.PUBLIC_URL}/icon/x-lg.svg`} alt="닫기"/>
                            </button>
                        </div>
                        <textarea
                            rows="15"
                            placeholder="코멘트에 대한 댓글을 남겨주세요."
                            maxLength={1000}
                            ref={textRef}
                        />
                        <div className="comment-content-bottom">
                            <button className="comment-content-btn" onClick={handleReplyUpdate}>수정</button>
                        </div>
                    </div>
                </div>
            )}

            {/* 댓글 삭제 확인 모달 */}
            {replyDeleteConfirmModal && (
                <div className="confirm-modal-overlay" onClick={closeReplyDeleteConfirmModal}>
                    <div className="confirm-modal-content" onClick={(e) => e.stopPropagation()}>
                        <p>알림</p>
                        <p>삭제하시겠습니까?</p>
                        <div className="confirm-btn-group">
                            <button onClick={handleReplyDelete}>확인</button>
                            <button onClick={closeReplyDeleteConfirmModal}>취소</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ReplyCard;
