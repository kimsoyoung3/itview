import 'bootstrap/dist/css/bootstrap.min.css';
import "./ReplyCard.css";
import { likeReply, unlikeReply } from '../../API/CommentApi';
import { useEffect, useState } from 'react';

const ReplyCard = ({reply, userInfo, openLogin}) => {

    const [replyData, setReplyData] = useState(reply);

    useEffect(() => {
        setReplyData(reply);
    }, [reply]);

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
        <div className="reply-card container">
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
    );

};
export default ReplyCard;