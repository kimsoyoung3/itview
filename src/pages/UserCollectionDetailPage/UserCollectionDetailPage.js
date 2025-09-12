import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCollectionDetail } from "../../API/CollectionApi";
import NotFound from "../NotFound/NotFound";
import CollectionCard from "../../components/CollectionCard/CollectionCard";

const UserCollectionDetailPage = () => {
    const { id } = useParams();
    const [collection, setCollection] = useState({});
    const [notFound, setNotFound] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getCollectionDetail(id);
                setCollection(res.data);
            } catch (e) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(collection);
    }, [collection]);

    return(notFound ? <NotFound /> :
        <div className="user-collection-detail-page">
            <div className="user-collection-detail-page-wrap">
                <h1>{collection?.title}</h1>
                <div className="user-collection-detail-page-wrap-content">
                    {collection?.content?.map(item => (
                        <CollectionCard key={item.id} collectionData={item} />
                    ))}
                </div>
            </div>
        </div>
    )

};
export default UserCollectionDetailPage;

