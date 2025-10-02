import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CommentDetailPage.css";
import {getCommentAndContent, getCommentRepliesPaged} from "../../API/CommentApi";
import {useNavigate, useParams} from "react-router-dom";
import CommentCard from "../../components/CommentCard/CommentCard";
import ReplyCard from "../../components/ReplyCard/ReplyCard";
import NotFound from "../NotFound/NotFound";


const CommentDetailPage = ({userInfo, openLogin}) => {
    /* 상태 변수 */
    const [notFound, setNotFound] = useState(false);
    const [comment, setComment] = useState({});
    const [replies, setReplies] = useState([]);
    const [page, setPage] = useState({});

    /* 라우터 변수 */
    const navigate = useNavigate();

    /*URL에서 :id 가져오기*/
    const { id } = useParams();

    /* 데이터 불러오기 */
    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getCommentAndContent(id)
                setComment(res.data)
    
                const repliesRes = await getCommentRepliesPaged(id, 1);
                setReplies(repliesRes.data.content);
                setPage(repliesRes.data.page);
            } catch (error) {
                setNotFound(true);
            }
        }
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(comment.comment)
    }, [comment]);

    useEffect(() => {
        console.log(replies);
    }, [replies]);

    /* 댓글 삭제 후 이동 */
    const onDelete = async () => {
        navigate(`/content/${comment.content.id}`);
    }

    /* 새 댓글 추가 */
    const newReply = (data) => {
        setReplies((prev) => ([data, ...prev]));
    }

    /* 더보기 기능: 페이지네이션 */
    const loadMoreRepliesRef = React.useRef();

    const handleNextPage = async () => {
        if (page.number < page.totalPages - 1) {
            const response = await getCommentRepliesPaged(id, page.number+2);
            setReplies((prev) => ([...prev, ...response.data.content]))
            setPage(response.data.page);
        }
    }

    /* 더보기 자동 로드 옵저버 */
    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                handleNextPage();
            }
        }, { threshold: 0.1 });

        if (loadMoreRepliesRef.current) {
            observer.observe(loadMoreRepliesRef.current);
        }

        return () => {
            if (loadMoreRepliesRef.current) {
                observer.unobserve(loadMoreRepliesRef.current);
            }
        };
    }, [loadMoreRepliesRef, page]);

    return(notFound ? <NotFound /> :
        <div className="comment-detail-page">
            <div className="comment-detail-page-content container">
                <h1 className="mobile-text">코멘트</h1>
                <CommentCard comment={comment?.comment} content={comment?.content} userInfo={userInfo} openLogin={openLogin} newReply={newReply} onDelete={onDelete}/>

                {replies.length > 0 ? (
                    <div>
                        {replies.map(r => <ReplyCard key={r.id} reply={r} userInfo={userInfo} openLogin={openLogin}/>)}
                    </div>
                ) : (
                    <p>등록된 댓글이 없습니다.</p>
                )}
                <div ref={loadMoreRepliesRef} style={{ height: '20px' }}></div>
            </div>
        </div>
    )
}
export default CommentDetailPage;