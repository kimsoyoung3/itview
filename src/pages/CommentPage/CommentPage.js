import React, {useEffect, useRef, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CommentPage.css";
import {useParams} from "react-router-dom";
import CommentCard from "../../components/CommentCard/CommentCard";
import {getContentCommentsPaged, getContentTitle} from "../../API/ContentApi";
import NotFound from "../NotFound/NotFound";
import CustomSelect from "../../components/CustomSelect/CustomSelect";

const CommentPage = ({userInfo, openLogin}) => {
    const [notFound, setNotFound] = useState(false);

    /*URL에서 :id 가져오기*/
    const { id } = useParams();
    const [title, setTitle] = useState("");
    const [comments, setComments] = useState([]);
    const [page, setPage] = useState({});

    const [order, setOrder] = useState("new");

    const options = [
        { value: "new", label: "최신 순" },
        { value: "old", label: "오래된 순" },
        { value: "like", label: "좋아요 순" },
        { value: "rating", label: "별점 순" },
        { value: "reply", label: "댓글 많은 순" }
    ];

    /*페이지 로드 시 코멘트 불러오기*/
    useEffect(() => {
        const fetchComments = async () => {
            try {
                const response = await getContentCommentsPaged(id, "new", 1);
                setComments(response.data.content);
                setPage(response.data.page);
                const titleResponse = await getContentTitle(id);
                setTitle(titleResponse.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchComments();
    }, [id]);

    /*comments 변경 시 콘솔에 출력*/
    useEffect(() => {
        console.log(comments)
    }, [comments]);

    const onDelete = async () => {
        window.location.reload();
    }

    /*다음 페이지 불러오기*/
    const handleNextPage = async () => {
        console.log("감지")
        if (page.number < page.totalPages - 1) {
            const response = await getContentCommentsPaged(id, order, page.number+2);
            setComments((prev) => ([...prev, ...response.data.content]))
            setPage(response.data.page);
        }
    };

    /*페이지 변경 시 콘솔에 출력*/
    useEffect(() => {
        console.log(page)
    }, [page]);

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

    return(notFound ? <NotFound /> :
        <div className="comment-page">
            <div className="comment-page-wrap container">
                <h1 className="comment-page-title">코멘트</h1>
                <div className="comment-page-select-wrap">
                    <CustomSelect value={order} onChange={setOrder} options={options} />
                </div>

                {comments.length > 0 ? (
                    <div className="comment-page-content">
                        {comments.map((c, index) => <CommentCard key={c.id} comment={c} content={{title}} userInfo={userInfo} openLogin={openLogin} onDelete={() => onDelete(index)}/>)}
                    </div>
                ) : (
                    <p className="empty-message">코멘트가 없습니다 :)</p>
                )}
                <div ref={loadMoreCommentsRef} style={{ height: '20px' }}></div>
            </div>
        </div>
    )
};
export default CommentPage;