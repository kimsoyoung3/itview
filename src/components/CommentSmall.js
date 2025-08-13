import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css"; // CSS 따로 관리

const CommentLarge = () => {
    return (
        <div className="comment-card container-sm">
            {/* 헤더 */}
            <div className="comment-header">
                <div className="comment-header-left">
                    <img src="/logo.svg"
                        className="comment-profile"
                    />
                    <span className="comment-nickname">닉네임</span>
                    <span className="comment-date">작성날짜</span>
                </div>
                <div className="comment-header-right">
                    <i className="bi bi-star-fill"/><span>0.0</span>
                </div>
            </div>

            {/* 내용 */}
            <div className="comment-content">
                <div className="comment-content-top">
                    <div className="comment-content-left">
                        <img src="/logo.svg" alt=""/>
                    </div>
                    <ul className="comment-content-right m-0 p-0">
                        <li>제목</li>
                        <li>컨텐츠 타입 &middot; <span>개봉 날짜</span></li>
                        <li>평균 별점</li>
                    </ul>

                </div>
                <p>컨텐츠 내용을 입력하세요</p>
            </div>

            {/* 푸터 */}
            <div className="comment-footer">
                <div className="comment-footer-top">
                    <p>좋아요<span>0</span></p>
                    <p>댓글<span>0</span></p>
                </div>
                <div className="comment-footer-bottom">
                    <span><i className="bi bi-hand-thumbs-up"/></span>
                    <span><i className="bi bi-chat-square"/></span>
                    <span><i className="bi bi-share"/></span>
                </div>
            </div>
        </div>
    );
};

export default CommentLarge;
