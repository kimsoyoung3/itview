import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CommentDetailPage.css";
import {getCommentAndContent, getCommentRepliesPaged} from "../../API/CommentApi";
import {useParams} from "react-router-dom";
import CommentCard from "../../components/CommentCard/CommentCard";
import ReplyCard from "../../components/ReplyCard/ReplyCard";


const CommentDetailPage = ({userInfo, openLogin}) => {
    const [comments, setComments] = useState({});
    const [replies, setReplies] = useState([]);

    const { id } = useParams(); // URL에서 :id 가져오기

    useEffect(() => {
        const fetchData = async () => {
            const res = await getCommentAndContent(id)
            setComments(res.data)

            const repliesRes = await getCommentRepliesPaged(id, 1);
            setReplies(repliesRes.data);
        }
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(comments.comment)
    }, [comments]);

    useEffect(() => {
        console.log(replies);
    }, [replies]);

    return(
        <div className="comment-detail-page container">
            <div className="comment-detail-page-content">
                <CommentCard comment={comments?.comment} content={comments?.content} userInfo={userInfo} openLogin={openLogin}/>
                <ReplyCard/>
            </div>
        </div>
    )


}
export default CommentDetailPage;