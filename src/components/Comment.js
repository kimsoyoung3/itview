import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css"; // CSS 따로 관리
import { likeComment, unlikeComment } from "../API/CommentApi";

const CommentLarge = ({comment, content, userInfo, openLogin}) => {
    const [commentData, setCommentData] = useState(comment);

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

    return (
        <div className="comment-card">
            {/* 헤더 */}
            <div className="comment-header">
                <div className="comment-header-left">
                    <img src={commentData.user?.profile || '/user.png'} className="comment-profile" alt=""/>
                    <span className="comment-nickname">{commentData.user.nickname}</span>
                    <span className="comment-date">{new Date(commentData.createdAt).toLocaleDateString().slice(0, -1)}</span>
                </div>
                <div className="comment-header-right">
                    <i className="bi bi-star-fill"/><span>{commentData.rating/2}</span>
                </div>
            </div>

            {/* 내용 */}
            <div className="comment-content">
                {content && (
                    <div className="comment-content-wrap">
                        <div className="comment-content-left">
                            <img src={content.poster} alt=""/>
                        </div>
                        <ul className="comment-content-right m-0 p-0">
                            <li>{content.title}</li>
                            <li>{content.contentType} &middot; <span>{content.releaseDate}</span></li>
                            <li>{content.ratingAvg}</li>
                        </ul>
                    </div>
                )}
                <p>{commentData.text}</p>
            </div>

            {/* 푸터 */}
            <div className="comment-footer">
                <div className="comment-footer-top">
                    <p>좋아요<span>{commentData.likeCount}</span></p>
                    <p>댓글<span>{commentData.replyCount}</span></p>
                </div>
                <div className="comment-footer-bottom">
                    <button onClick={userInfo ? () => handleLikeComment(commentData.id) : openLogin}>
                        <i className={commentData.liked ? "bi bi-hand-thumbs-up-fill" : "bi bi-hand-thumbs-up"}/>
                    </button>
                    <button><i className="bi bi-chat-square"/></button>
                    <button><i className="bi bi-share"/></button>
                </div>
            </div>
        </div>
    );
};

export default CommentLarge;
