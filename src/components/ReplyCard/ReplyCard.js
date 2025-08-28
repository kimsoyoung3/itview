import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./ReplyCard.css";
import {likeComment} from "../../API/CommentApi"; // CSS 따로 관리

const ReplyCard = ({reply}) => {

    const [replyData, setReplyData] = useState({})

    useEffect(() => {
        setReplyData(reply)
    }, [reply]);

    useEffect(() => {
        console.log(replyData)
    }, [replyData]);

    return(
            <div className="reply-card container">
                <div className="reply-card-info">
                    <div className="reply-card-info-profile"><img src={reply?.user.profile || "/user.png"} alt=""/></div>
                    <span>{reply?.user.nickname}</span>
                    <span>{new Date(reply?.createdAt).toLocaleDateString().slice(0, -1)}</span>
                </div>
                <p className="reply-card-text">{reply?.text}</p>
                <div className="reply-card-footer">
                    <button>
                        <i className={reply?.liked ? "bi bi-hand-thumbs-up-fill" : "bi bi-hand-thumbs-up"}/>
                    </button>
                    <p>좋아요 <span>{reply?.likeCount}</span></p>
                </div>
            </div>
    )

};
export default ReplyCard;