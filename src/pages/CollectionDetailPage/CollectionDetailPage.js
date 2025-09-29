import React, {useEffect, useRef, useState} from "react";
import { NavLink, useParams } from "react-router-dom";
import { deleteCollection, getCollectionDetail, getCollectionItems, getCollectionReplies, insertReply, likeCollection, unlikeCollection } from "../../API/CollectionApi";
import NotFound from "../NotFound/NotFound";
import "./CollectionDetailPage.css"
import {toast} from "react-toastify";
import ContentEach from "../../components/ContentEach/ContentEach";
import ReplyCard from "../../components/ReplyCard/ReplyCard";

const CollectionDetailPage = ({userInfo, openLogin}) => {
    const { id } = useParams();
    const [notFound, setNotFound] = useState(false);

    const [collection, setCollection] = useState({});
    const [items, setItems] = useState({});
    const [replies, setReplies] = useState({});

    const [deleteCollectionModal, setDeleteCollectionModal] = useState()

    const openDeleteCollectionModal = () => setDeleteCollectionModal(true)
    const closeDeleteCollectionModal = () => setDeleteCollectionModal(false)

    const [edit, setEdit] = useState(false);
    const menuRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setEdit(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const detailRes = await getCollectionDetail(id);
                setCollection(detailRes.data);
                const itemsRes = await getCollectionItems(id, 1);
                setItems(itemsRes.data);
                const repliesRes = await getCollectionReplies(id, 1);
                setReplies(repliesRes.data);
            } catch (e) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(collection);
    }, [collection]);

    useEffect(() => {
        console.log(items);
    }, [items]);

    useEffect(() => {
        console.log(replies);
    }, [replies]);

    const handleCollectionDelete = async () => {
        try {
            await deleteCollection(id);
            toast("컬렉션이 삭제되었습니다.");
            window.location.replace(`/user/${userInfo}/collection`);
        } catch (e) {
            toast("컬렉션 삭제에 실패했습니다.");
        }
    }

    const handleLike = async () => {
        if (!collection.liked) {
            try {
                await likeCollection(id);
                setCollection(prev => ({...prev, liked: true, likeCount: prev.likeCount + 1}));
            } catch (e) {
                toast("좋아요 등록에 실패했습니다.");
            }
        } else {
            try {
                await unlikeCollection(id);
                setCollection(prev => ({...prev, liked: false, likeCount: prev.likeCount - 1}));
            } catch (e) {
                toast("좋아요 취소에 실패했습니다.");
            }
        }
    }

    const handleLoadMoreItem = async () => {
        if (items.page.number < items.page.totalPages - 1) {
            try {
                const res = await getCollectionItems(id, items.page.number + 2);
                setItems(prev => ({
                    content: [...prev.content, ...res.data.content],
                    page: res.data.page
                }));
            } catch (e) {
                toast("작품을 불러오는데 실패했습니다.");
            }
        }
    }

    const handleLoadMoreReply = async () => {
        if (replies.page.number < replies.page.totalPages - 1) {
            try {
                const res = await getCollectionReplies(id, replies.page.number + 2);
                setReplies(prev => ({
                    content: [...prev.content, ...res.data.content],
                    page: res.data.page
                }));
            } catch (e) {
                toast("댓글을 불러오는데 실패했습니다.");
            }
        }
    }

    const replyRef = useRef();
    const handleReplySubmit = async () => {
        if (replyRef.current.value.trim() !== "") {
            try {
                const res = await insertReply(id, {text: replyRef.current.value});
                replyRef.current.value = "";
                toast("댓글이 등록되었습니다.");
                setReplies(prev => ({
                    ...prev,
                    content: [res.data, ...prev.content]
                }));
            } catch (e) {
                toast("댓글 등록에 실패했습니다.");
            }
        }
    }

    /*댓글 탭*/
    const replySectionRef = useRef(null);

    const scrollToReply= () => {
        replySectionRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    return(notFound ? <NotFound /> :
        <div className="user-collection-detail-page container">
            <div className="user-collection-detail-page-wrap">
                {/* 썸네일 이미지와 그림자 */}
                <section className="user-collection-detail-bg">

                    {collection?.poster?.slice(0, 5).map((poster, index, arr) => (
                        <div
                            key={index}
                            className="user-collection-detail-stacked-poster-wrapper"
                            style={{
                                left: `${index * 15}%`, // 겹치는 위치 조정
                                zIndex: arr.length - index,
                            }}
                        >
                            <img src={poster} alt={`포스터 ${index}`} />
                        </div>
                    ))}

                    <div className="user-collection-detail-bg-shadow"></div>

                    {/* 사용자 프로필 */}
                    <NavLink to={`/user/${collection?.user?.id}`} className="user-collection-detail-profile">
                        <div className="user-collection-detail-profile-image">
                            <img
                                src={collection?.user?.profile ? collection.user.profile : `${process.env.PUBLIC_URL}/user.png`}
                                alt="사용자 프로필"
                            />
                        </div>
                        <p>{collection?.user?.nickname}</p>
                    </NavLink>

                    {/*수정&삭제 버튼*/}
                    <div className="user-collection-detail-edit-box" ref={menuRef} hidden={userInfo?.userId !== collection?.user?.id}>
                        {edit && (
                            <div>
                                <NavLink to={`/collection/${collection.id}/edit`}>수정</NavLink>
                                <button onClick={openDeleteCollectionModal}>삭제</button>
                            </div>
                        )}

                        <button onClick={() => setEdit(prev => !prev)} className="user-collection-detail-edit-btn">
                            <i className="bi bi-three-dots"></i>
                        </button>
                    </div>

                    {/* 댓글 삭제 확인 모달 */}
                    {deleteCollectionModal && (
                        <div className="confirm-modal-overlay" onClick={closeDeleteCollectionModal}>
                            <div className="confirm-modal-content" onClick={e => e.stopPropagation()}>
                                <p>알림</p>
                                <p>삭제하시겠습니까?</p>
                                <div className="confirm-btn-group">
                                    <button onClick={handleCollectionDelete}>확인</button>
                                    <button onClick={closeDeleteCollectionModal}>취소</button>
                                </div>
                            </div>
                        </div>
                    )}

                </section>

                {/*제목 및 좋아요, 댓글, 업데이트시간*/}
                <section className="user-collection-detail-info">
                    <h1>{collection?.title}</h1>
                    <p>{collection?.description}</p>
                    <p>
                        <span>좋아요 {collection?.likeCount}</span>
                        <span id="reply-tab">댓글 {collection?.replyCount}</span>
                        <span>{collection?.updatedAt?.substring(0, 10)}</span>
                    </p>
                </section>

                {/*좋아요&댓글&공유 버튼*/}
                <section className="user-collection-detail-btn-list">
                    <div className="user-collection-detail-btn-list-wrap">
                        <div className="user-collection-detail-btn-list-content">
                            <button onClick={handleLike}>
                                <i className={`bi ${collection.liked ? 'bi-heart-fill' : 'bi-heart'}`}/>
                                <span>좋아요</span>
                            </button>
                        </div>
                        <div className="user-collection-detail-btn-list-content">
                            <button onClick={scrollToReply}>
                                <i className="bi bi-chat-square"/>
                                <span>댓글</span>
                            </button>
                        </div>
                        <div className="user-collection-detail-btn-list-content">
                            <button onClick={() => {
                                navigator.clipboard.writeText(window.location.href);
                                toast("URL이 복사되었습니다!");
                            }}>
                                <i className="bi bi-share"/>
                                <span>공유</span>
                            </button>
                        </div>
                    </div>
                </section>

                {/*작품컨텐츠 리스트*/}
                <section className="user-collection-detail-content">

                    <div className="user-collection-detail-content-wrap">
                        <h1>작품들 <span>{collection?.contentCount}</span></h1>

                        <div className="user-collection-detail-content-list">
                            {items?.content?.length > 0 ? (
                                items?.content.map(item =>
                                    <ContentEach key={item.id} ratingData={{content : item}} ratingType={'avg'} clamp={true}/>
                                )
                            ) : (
                                <p className="empty-message">작품이 없습니다 :)</p>
                            )}
                        </div>

                        <div className="user-collection-detail-content-list-btn-box">
                            <button className="user-collection-detail-content-list-btn" onClick={handleLoadMoreItem} hidden={items?.page?.number + 2 > items?.page?.totalPages}>더보기</button>
                        </div>

                    </div>
                </section>

                {/*댓글*/}
                <section className="user-collection-detail-reply" ref={replySectionRef}>
                    <div className="user-collection-detail-reply-wrap">
                        <h1>댓글 <span>{collection?.replyCount}</span></h1>

                        {replies?.content?.length > 0 && (
                            <div className="user-collection-detail-reply-content-wrap">
                                {replies?.content.map((reply) => (
                                    <ReplyCard key={reply.id} reply={reply} userInfo={userInfo} openLogin={openLogin}/>
                                ))}

                                <div className="user-collection-detail-reply-btn">
                                    <button onClick={handleLoadMoreReply} hidden={replies?.page?.number + 1 >= replies?.page?.totalPages}>더보기</button>
                                </div>
                            </div>
                        )}

                        <div className="user-collection-detail-reply-input">
                            <input type="text" maxLength={200} placeholder="댓글을 입력해주세요." ref={replyRef}/>
                            <button onClick={handleReplySubmit}>등록</button>
                        </div>

                    </div>
                </section>
            </div>
        </div>
    )

};
export default CollectionDetailPage;

