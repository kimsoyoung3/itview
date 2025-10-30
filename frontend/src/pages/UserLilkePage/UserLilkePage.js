import { React, useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { getUserLikeCollection, getUserLikeComment, getUserLikePerson } from '../../API/UserApi';
import "./UserLilkePage.css"
import CommentCard from "../../components/CommentCard/CommentCard";
import CreditOrPersonCard from "../../components/CreditOrPersonCard/CreditOrPersonCard";
import { toast } from 'react-toastify';
import NotFound from "../NotFound/NotFound";
import CollectionCard from "../../components/CollectionCard/CollectionCard";

function UserLikePage({userInfo, openLogin, onDelete}) {
    /* URL 파라미터 & 초기 상태 설정 */
    const [notFound, setNotFound] = useState(false);
    const [searchParams] = new useSearchParams();
    const [contentType, setContentType] = useState(searchParams.get('type') || 'person');
    const {id} = useParams();

    /* 좋아요 관련 상태 */
    const [personLikes, setPersonLikes] = useState({});
    const [collectionLikes, setCollectionLikes] = useState({});
    const [commentLikes, setCommentLikes] = useState({});

    /* 데이터 불러오기 */
    useEffect(() => {
        try {
            const fetchData = async () => {
                const personRes = await getUserLikePerson(id, 1);
                setPersonLikes(personRes.data);
                const collectionRes = await getUserLikeCollection(id, 1);
                setCollectionLikes(collectionRes.data);
                const commentRes = await getUserLikeComment(id, 1);
                setCommentLikes(commentRes.data);
            }
            fetchData();
        } catch (error) {
            setNotFound(true);
        }
    }, [id]);

    useEffect(() => {
        console.log(personLikes);
    }, [personLikes]);

    useEffect(() => {
        console.log(collectionLikes);
    }, [collectionLikes]);

    useEffect(() => {
        console.log(commentLikes);
    }, [commentLikes]);

    /* 인물 좋아요 더보기 */
    const handlePersonLoadMore = async () => {
        try {
            const nextPage = personLikes.page.number + 2;
            const res = await getUserLikePerson(id, nextPage);
            setPersonLikes(prevState => ({
                ...res.data,
                content: [...prevState.content, ...res.data.content],
                page: res.data.page
            }));
        } catch (error) {
            toast("정보를 불러오지 못했습니다.");
        }
    };

    /* 컬렉션 좋아요 더보기 */
    const handleCollectionLoadMore = async () => {
        try {
            const nextPage = collectionLikes.page.number + 2;
            const res = await getUserLikeCollection(id, nextPage);
            setCollectionLikes(prevState => ({
                ...res.data,
                content: [...prevState.content, ...res.data.content],
                page: res.data.page
            }));
        } catch (error) {
            toast("정보를 불러오지 못했습니다.");
        }
    };

    /* 코멘트 좋아요 더보기 */
    const handleCommentLoadMore = async () => {
        try {
            const nextPage = commentLikes.page.number + 2;
            const res = await getUserLikeComment(id, nextPage);
            setCommentLikes(prevState => ({
                ...res.data,
                content: [...prevState.content, ...res.data.content],
                page: res.data.page
            }));
        } catch (error) {
            toast("정보를 불러오지 못했습니다.");
        }
    };

    return (notFound ? <NotFound/> :
            <>
                <div className="user-like-page">
                    <div className="user-like-page-wrap container">
                        <h1>좋아요</h1>
                        <div className="user-like-title">
                            <div className={`like-page-tab-btn ${contentType === "person" ? "active" : ""}`}
                                 onClick={() => setContentType('person')}>인물
                            </div>
                            <div className={`like-page-tab-btn ${contentType === "collection" ? "active" : ""}`}
                                 onClick={() => setContentType('collection')}>컬렉션
                            </div>
                            <div className={`like-page-tab-btn ${contentType === "comment" ? "active" : ""}`}
                                 onClick={() => setContentType('comment')}>코멘트
                            </div>

                            <span
                                className="like-tab-indicator"
                                style={{
                                    width: `${100 / 3}%`,
                                    transform: `translateX(${["person", "collection", "comment"].indexOf(contentType) * 100}%)`
                                }}
                            />
                        </div>

                        <div className="user-like-content">
                            {contentType === "person" && <div className="like-page-tab1">
                                {personLikes?.content?.length > 0 ? (
                                    <div className="like-page-person-content-wrap">
                                        <div className="like-page-person-content">
                                            {personLikes?.content?.map(item =>
                                                <CreditOrPersonCard key={item.id} data={item} type={"person"}/>
                                            )}
                                        </div>

                                        <div className="like-page-content-btn">
                                            <button
                                                style={{display: personLikes?.page?.number + 1 === personLikes?.page?.totalPages ? "none" : ""}}
                                                onClick={handlePersonLoadMore}>더보기
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <p className="empty-message">좋아요한 인물이 없습니다 :)</p>
                                )}
                            </div>}

                            {contentType === "collection" && <div className="like-page-tab2">
                                {collectionLikes?.content?.length > 0 ? (
                                    <div className="like-page-person-content-wrap">
                                        <div className="like-page-content">
                                            {collectionLikes?.content.map(item =>
                                                <CollectionCard key={item.id} collectionData={item}/>
                                            )}
                                        </div>

                                        <div className="like-page-content-btn">
                                            <button
                                                style={{display: collectionLikes?.page?.number + 1 === collectionLikes?.page?.totalPages ? "none" : ""}}
                                                onClick={handleCollectionLoadMore}>더보기
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <p className="empty-message">좋아요한 컬렉션이 없습니다 :)</p>
                                )}
                            </div>}

                            {contentType === "comment" && <div className="like-page-tab3">
                                {commentLikes?.content?.length > 0 ? (
                                    <div className="like-page-content-wrap">
                                        <div className="like-page-content">
                                            {commentLikes?.content?.map(item =>
                                                <CommentCard key={item.comment.id} comment={item.comment}
                                                             content={item.content} userInfo={userInfo}
                                                             openLogin={openLogin} onDelete={onDelete}/>
                                            )}
                                        </div>

                                        <div className="like-page-content-btn">
                                            <button
                                                style={{display: commentLikes?.page?.number + 1 === commentLikes?.page?.totalPages ? "none" : ""}}
                                                onClick={handleCommentLoadMore}>더보기
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <p className="empty-message">좋아요한 코멘트가 없습니다 :)</p>
                                )}
                            </div>}
                        </div>

                    </div>
                </div>
            </>
    );
}

export default UserLikePage;