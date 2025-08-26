import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css"; // CSS 따로 관리

const CommentLarge = ({comment, content}) => {
    if (!comment) return null;


    return (
        <div className="comment-card">
            {/* 헤더 */}
            <div className="comment-header">
                <div className="comment-header-left">
                    <img src={comment.user?.profile || '/user.png'} className="comment-profile" alt=""/>
                    <span className="comment-nickname">{comment.user.nickname}</span>
                    <span className="comment-date">{new Date(comment.createdAt).toLocaleDateString().slice(0, -1)}</span>
                </div>
                <div className="comment-header-right">
                    <i className="bi bi-star-fill"/><span>{comment.rating/2}</span>
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
                <p>{comment.text}</p>
            </div>

            {/* 푸터 */}
            <div className="comment-footer">
                <div className="comment-footer-top">
                    <p>좋아요<span>{comment.likeCount}</span></p>
                    <p>댓글<span>{comment.replyCount}</span></p>
                </div>
                <div className="comment-footer-bottom">
                    <button><i className="bi bi-hand-thumbs-up"/></button>
                    <button><i className="bi bi-chat-square"/></button>
                    <button><i className="bi bi-share"/></button>
                </div>
            </div>
        </div>
    );
};

export default CommentLarge;
