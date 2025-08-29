import React, {useEffect, useRef, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CommentPage.css";
import {useParams} from "react-router-dom";
import CommentCard from "../../components/CommentCard/CommentCard";
import {getContentCommentsPaged} from "../../API/ContentApi";

const CommentPage = ({userInfo, openLogin}) => {
    /*URL에서 :id 가져오기*/
    const { id } = useParams();
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState({});
    const [order, setOrder] = useState("new");

    /*페이지 로드 시 코멘트 불러오기*/
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

    /*comments 변경 시 콘솔에 출력*/
    useEffect(() => {
        console.log(comments)
    }, [comments]);

    /*다음 페이지 불러오기*/
    const handleNextPage = async () => {
        if (page.number < page.totalPages - 1) {
            const response = await getContentCommentsPaged(id, order, page.number+2);
            setComments((prev) => ([...prev, ...response.data.content]))
            setPage(response.data.page);
        }
    }

    /*페이지 변경 시 콘솔에 출력*/
    useEffect(() => {
        console.log(page)
    }, [page]);

    /*정렬 방식 변경*/
    const handleOrderChange = (e) => {
        /*new, old, rating, like, reply*/
        setOrder(e.target.value);
    };

    /*정렬 방식 변경 시 코멘트 다시 불러오기*/
    useEffect(() => {
        const fetchComments = async () => {
            const response = await getContentCommentsPaged(id, order, 1);
            setComments(response.data.content);
            setPage(response.data.page)
        }

        fetchComments();
    }, [order]);

    /*코멘트 더보기 감지*/
    const loadMoreCommentsRef = useRef(null);

    /*감지 후 다음 페이지 로드*/
    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                handleNextPage();
            }
        }, {
            threshold: 0.1
        });
        if (loadMoreCommentsRef.current) {
            observer.observe(loadMoreCommentsRef.current);
        }
        return () => {
            if (loadMoreCommentsRef.current) {
                observer.unobserve(loadMoreCommentsRef.current);
            }
        };
    }, [loadMoreCommentsRef, page]);

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
            <div ref={loadMoreCommentsRef} style={{ height: '20px' }}></div>
        </div>
    )
};
export default CommentPage;