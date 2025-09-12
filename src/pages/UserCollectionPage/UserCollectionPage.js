import React, {useEffect, useState} from "react";
import "./UserCollectionPage.css"
import NotFound from "../NotFound/NotFound";
import { NavLink, useParams } from "react-router-dom";
import { getUserCollection } from "../../API/UserApi";

const UserCollectionPage = () => {
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

    return( notFound ? <NotFound /> :
        <div className="user-collection-page container">
            <div className="user-collection-page-wrap">
                <div className="user-collection-page-wrap-title">
                    <h1>컬렉션</h1>
                    <NavLink to="/collection/new">새 컬렉션</NavLink>
                </div>
            </div>
        </div>
    )

};
export default UserCollectionPage;