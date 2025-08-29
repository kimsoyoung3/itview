import 'bootstrap/dist/css/bootstrap.min.css';
import "./ReplyCard.css";
import React, { useEffect, useState } from 'react';
import { deleteReply, likeReply, unlikeReply, updateReply } from '../../API/ReplyApi';
import {toast} from "react-toastify";

const ReplyCard = ({reply, userInfo, openLogin}) => {
    const [replyData, setReplyData] = useState(reply);

    const textRef = React.useRef(null); //댓글 텍스트 영역 참조

    /*댓글 수정 모달창*/
    const [replyUpdateModal, setReplyUpdateModal] = useState()

    const openUpdateReply = ()=> setReplyUpdateModal(true)
    const closeUpdateReply = ()=> setReplyUpdateModal(false)

    /*댓글 삭제 확인 모달창*/
    const [replyDeleteConfirmModal, setReplyDeleteConfirmModal] = useState();

    const closeReplyDeleteConfirmModal = () => setReplyDeleteConfirmModal(false);

    const handleReplyDeleteConfirm = () => {
        setReplyDeleteConfirmModal(true)
    };

    useEffect(() => {
        setReplyData(reply);
    }, [reply]);

    useEffect(() => {
        if (textRef.current && replyData) {
            textRef.current.value = replyData?.text;
        }
    }, [replyData, replyUpdateModal]);

    const handleReplyUpdate = async () => {
        const text = textRef.current.value;
        if (text) {
            try {
                const response = await updateReply(replyData.id, { text });
                if (response.status === 200) {
                    toast("댓글이 수정되었습니다.");

                    setReplyData((prev) => ({ ...prev, text }));
                    closeUpdateReply();
                } else {
                    console.error("Failed to update reply");
                }
            } catch (error) {
                console.error("Error updating reply:", error);
            }
        }
    };

    const handleReplyDelete = async () => {
        try {
            const response = await deleteReply(replyData.id);
            if (response.status === 200) {
                toast("댓글이 삭제되었습니다.",{

                });

                setReplyData(null);
            } else {
                console.error("Failed to delete reply");
            }
        } catch (error) {
            console.error("Error deleting reply:", error);
        }
    };

    const handleLike = () => {
        if (replyData?.liked) {
            unlikeReply(replyData.id);
            setReplyData((prev) => ({ ...prev, liked: false, likeCount: prev.likeCount - 1 }));
        } else {
            likeReply(replyData.id);
            setReplyData((prev) => ({ ...prev, liked: true, likeCount: prev.likeCount + 1 }));
        }
    };

    return(
        replyData && (
        <div className="reply-card container">
            <div className="reply-card-wrap">
                <div  className="reply-card-inner-left">
                    <div className="reply-card-info">
                        <div className="reply-card-info-profile"><img src={replyData?.user.profile || "/user.png"} alt=""/></div>
                        <span>{replyData?.user.nickname}</span>
                        <span>{new Date(replyData?.createdAt).toLocaleDateString().slice(0, -1)}</span>
                    </div>
                    <p className="reply-card-text">{replyData?.text}</p>
                    <div className="reply-card-footer">
                        <button onClick={userInfo ? handleLike : openLogin} className="like-button">
                            <i className={replyData?.liked ? "bi bi-hand-thumbs-up-fill" : "bi bi-hand-thumbs-up"}/>
                        </button>
                        <p>좋아요 <span>{replyData?.likeCount}</span></p>
                    </div>
                </div>
                {userInfo === replyData?.user.id &&
                <div  className="reply-card-inner-right">
                    <button onClick={handleReplyDeleteConfirm}>
                        <i className="bi bi-trash"></i>
                    </button>
                    <button onClick={openUpdateReply}>
                        <i className="bi bi-pencil"></i>
                    </button>
                </div>
                }
            </div>

            {/*댓글 수정 모달창*/}
            {replyUpdateModal && (
                <div className="comment-modal-overlay" onClick={closeUpdateReply}>
                    <div className="comment-modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="comment-content-top">
                            <p className="comment-modal-title">댓글</p>
                            <button className="comment-close-button" onClick={closeUpdateReply}><img src="/icon/x-lg.svg" alt=""/></button>
                        </div>
                        <textarea rows="15" placeholder="코멘트에 대한 댓글을 남겨주세요." maxLength={1000} ref={textRef}></textarea>
                        <div className="comment-content-bottom">
                            <button className="comment-content-btn" onClick={handleReplyUpdate}>수정</button>
                        </div>
                    </div>
                </div>
            )}

            {/*코멘트 삭제 확인 모달창*/}
            {replyDeleteConfirmModal && (
                <div className="confirm-modal-overlay" onClick={closeReplyDeleteConfirmModal}>
                    <div className="confirm-modal-content">
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
    ))
};
export default ReplyCard;