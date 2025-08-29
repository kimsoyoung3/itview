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
    const [page, setPage] = useState({});

    /*URL에서 :id 가져오기*/
    const { id } = useParams();

    useEffect(() => {
        const fetchData = async () => {
            const res = await getCommentAndContent(id)
            setComments(res.data)

            const repliesRes = await getCommentRepliesPaged(id, 1);
            setReplies(repliesRes.data.content);
            setPage(repliesRes.data.page);
        }
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(comments.comment)
    }, [comments]);

    useEffect(() => {
        console.log(replies);
    }, [replies]);

    const newReply = (data) => {
        setReplies((prev) => ([data, ...prev]));
    }

    const loadMoreRepliesRef = React.useRef();

    const handleNextPage = async () => {
        if (page.number < page.totalPages - 1) {
            const response = await getCommentRepliesPaged(id, page.number+2);
            setReplies((prev) => ([...prev, ...response.data.content]))
            setPage(response.data.page);
        }
    }

    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                handleNextPage();
            }
        }, {
            threshold: 0.1
        });
        if (loadMoreRepliesRef.current) {
            observer.observe(loadMoreRepliesRef.current);
        }
        return () => {
            if (loadMoreRepliesRef.current) {
                observer.unobserve(loadMoreRepliesRef.current);
            }
        };
    }, [loadMoreRepliesRef, page]);

    return(
        <div className="comment-detail-page container">
            <div className="comment-detail-page-content">
                <CommentCard comment={comments?.comment} content={comments?.content} userInfo={userInfo} openLogin={openLogin} newReply={newReply}/>

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