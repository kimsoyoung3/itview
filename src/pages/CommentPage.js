import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../App.css";
import {useParams} from "react-router-dom";
import CommentCard from "../components/CommentCard";
import {getContentCommentsPaged} from "../API/ContentApi";

const CommentPage = ({userInfo, openLogin}) => {
    const { id } = useParams(); // URL에서 :id 가져오기
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState({});
    const [order, setOrder] = useState("new");

    useEffect(() => {
        const fetchComments = async () => {
            try {
                const response = await getContentCommentsPaged(id, "new", 1);
                setComments(response.data.content);
                setPage(response.data.page);
            } catch (error) {
                console.error(error);
            }
        };

        fetchComments();
    }, [id]);

    useEffect(() => {
        console.log(comments)
    }, [comments]);

    const handleNextPage = async () => {
        if (page.number < page.totalPages - 1) {
            const response = await getContentCommentsPaged(id, order, page.number+2);
            setComments((prev) => ([...prev, ...response.data.content]))
            setPage(response.data.page)
        } else {
            console.log("마지막 페이지")
        }
    }

    useEffect(() => {
        console.log(page)
    }, [page]);

    const handleOrderChange = (e) => {
        setOrder(e.target.value); // new, old, rating, like, reply
    };

    useEffect(() => {
        const fetchComments = async () => {
            const response = await getContentCommentsPaged(id, order, 1);
            setComments(response.data.content);
            setPage(response.data.page)
        }

        fetchComments();
    }, [order]);

    return(
        <div className="comment-page container">
            <h1 className="comment-page-title">코멘트</h1>
            <div className="comment-page-select-wrap">
                <select className="form-select comment-page-select" value={order} onChange={handleOrderChange} aria-label="Default select example">
                    <option selected value="new">최신 순</option>
                    <option value="old">오래된 순</option>
                    <option value="like">좋아요 순</option>
                    <option value="rating">별점 순</option>
                    <option value="reply">댓글 많은 순</option>
                </select>
            </div>

            {comments.length > 0 ? (
                <div className="comment-page-content">
                    {comments.map(c => <CommentCard key={c.id} comment={c} userInfo={userInfo} openLogin={openLogin}/>)}
                </div>
            ) : (
                <p>코멘트가 없습니다.</p>
            )}

            <button onClick={handleNextPage}>다음</button>
        </div>
    )
};
export default CommentPage;