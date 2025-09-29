import React, {useEffect, useRef, useState} from "react";
import "./UserCollectionPage.css"
import NotFound from "../NotFound/NotFound";
import { NavLink, useParams } from "react-router-dom";
import { getUserCollection } from "../../API/UserApi";
import CollectionCard from "../../components/CollectionCard/CollectionCard";
import { toast } from "react-toastify";

const UserCollectionPage = ({userInfo}) => {
    const [notFound, setNotFound] = useState(false);

    const { id } = useParams();

    const [collections, setCollections] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getUserCollection(id, 1);
                setCollections(res.data);
            } catch (e) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(collections);
    }, [collections]);

    const moreRef = useRef();

    const loadMore = async () => {
        console.log("load more");
        if (collections.page.number + 1 < collections.page.totalPages) {
            console.log("load more api");
            try {
                const res = await getUserCollection(id, collections.page.number + 2);
                setCollections({
                    content: [...collections.content, ...res.data.content],
                    page: res.data.page,
                });
            } catch (e) {
                toast("컬렉션을 불러오지 못했습니다.");
            }
        }
    };

    useEffect(() => {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                loadMore();
            }
        }, { threshold: 0.1 });
        if (moreRef.current) {
            observer.observe(moreRef.current);
        }
        return () => {
            if (moreRef.current) {
                observer.unobserve(moreRef.current);
            }
        };
    }, [collections]);

    return( notFound ? <NotFound /> :
        <div className="user-collection-page">
            <div className="user-collection-page-wrap container">
                <div className="user-collection-page-wrap-title">
                    <h1>컬렉션</h1>
                    <NavLink to="/collection/new" hidden={(Number(userInfo?.userId) !== Number(id))}>새 컬렉션</NavLink>
                </div>

                {collections?.content?.length > 0 ? (
                    <div className="user-collection-list">
                        {collections?.content.map(item =>
                            <CollectionCard key={item.id} collectionData={item}/>
                        )}
                        <div ref={moreRef} hidden={collections.page.number + 1 >= collections.page.totalPages} style={{ height: '20px' }}></div>
                    </div>
                ) : (
                    <p className="empty-message">컬렉션이 없습니다 :)</p>
                )}

            </div>
        </div>
    )
};
export default UserCollectionPage;