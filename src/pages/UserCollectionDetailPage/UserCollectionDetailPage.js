import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCollectionDetail, getCollectionItems, likeCollection, unlikeCollection } from "../../API/CollectionApi";
import NotFound from "../NotFound/NotFound";
import "./UserCollectionDetailPage.css"
import {toast} from "react-toastify";

const UserCollectionDetailPage = () => {
    const { id } = useParams();
    const [notFound, setNotFound] = useState(false);
    const [edit, setEdit] = useState(false);

    const [collection, setCollection] = useState({});
    const [items, setItems] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                const detailRes = await getCollectionDetail(id);
                setCollection(detailRes.data);
                const itemsRes = await getCollectionItems(id, 1);
                setItems(itemsRes.data);
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

    const handleLike = async () => {
        if (!collection.liked) {
            try {
                await likeCollection(id);
                setCollection(prev => ({...prev, liked: true, likeCount: prev.likeCount + 1}));
                toast("좋아요!");
            } catch (e) {
                toast("좋아요 실패!");
            }
        } else {
            try {
                await unlikeCollection(id);
                setCollection(prev => ({...prev, liked: false, likeCount: prev.likeCount - 1}));
                toast("좋아요 취소!");
            } catch (e) {
                toast("좋아요 취소 실패!");
            }
        }
    }

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
                    <div className="user-collection-detail-profile">
                        <div className="user-collection-detail-profile-image">
                            <img
                                src={collection?.user?.profile ? collection.user.profile : '/user.png'}
                                alt="사용자 프로필"
                            />
                        </div>
                        <p>{collection?.user?.nickname}</p>
                    </div>

                    {/*수정&삭제 버튼*/}
                    <div className="user-collection-detail-edit-box">
                        {edit && (
                            <div>
                                <button>수정</button>
                                <button>삭제</button>
                            </div>
                        )}

                        <button onClick={() => setEdit(prev => !prev)} className="user-collection-detail-edit-btn">
                            <i className="bi bi-three-dots"></i>
                        </button>
                    </div>

                </section>

                {/*제목 및 좋아요, 댓글, 업데이트시간*/}
                <section className="user-collection-detail-info">
                    <h1>{collection?.title}</h1>
                    <p>
                        <span>좋아요 {collection?.likeCount}</span>
                        <span> &#124; 댓글 {collection?.replyCount}</span>
                        <span> &#124; {collection?.updatedAt?.substring(0, 10)}</span>
                    </p>
                </section>

                <section className="user-collection-detail-btn-list">
                    <div className="user-collection-detail-btn-list-wrap">
                        <div className="user-collection-detail-btn-list-content">
                            <button onClick={handleLike}>
                                <i className={`bi ${collection.liked ? 'bi-heart-fill' : 'bi-heart'}`}/>
                                <span>좋아요</span>
                            </button>
                        </div>
                        <div className="user-collection-detail-btn-list-content">
                            <button>
                                <i className="bi bi-chat-square"/>
                                <span>댓글</span>
                            </button>
                        </div>
                        <div className="user-collection-detail-btn-list-content">
                            <button>
                                <i className="bi bi-share"/>
                                <span>공유</span>
                            </button>
                        </div>
                    </div>
                </section>

            </div>
        </div>
    )

};
export default UserCollectionDetailPage;

