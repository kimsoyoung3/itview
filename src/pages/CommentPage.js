import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css";
import {useParams} from "react-router-dom";
import CommentCard from "../components/CommentCard";
import {getContentDetail} from "../API/ContentApi";

const CommentPage = (userInfo,openLogin) => {
    const { id } = useParams(); // URL에서 :id 가져오기
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchComments = async () => {
            try {
                const response = await getContentDetail(id); // 혹은 별도 코멘트 API
                setComments(response.data.comments);
            } catch (error) {
                console.error(error);
            }
        };

        fetchComments();
    }, [id]);


    return(
        <div className="comment-page container">
            <h1 className="comment-page-title">코멘트</h1>
            <div className="comment-page-select-wrap">
                <select className="form-select comment-page-select" aria-label="Default select example">
                    <option selected>좋아요 순</option>
                    <option value="1">최신 순</option>
                    <option value="2">오래된 순</option>
                    <option value="3">별점 순</option>
                    <option value="4">댓글 많은 순</option>
                </select>
            </div>

            {comments.length > 0 ? (
                <div className="comment-page-content">
                    {comments.map(c => <CommentCard key={c.id} comment={c}/>)}
                </div>
            ) : (
                <p>댓글이 없습니다.</p>
            )}
        </div>
    )
};
export default CommentPage;