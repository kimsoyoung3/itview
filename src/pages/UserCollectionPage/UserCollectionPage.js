import React, {useState} from "react";
import "./UserCollectionPage.css"
import NotFound from "../NotFound/NotFound";
import { NavLink } from "react-router-dom";

const UserCollectionPage = () => {
    const [notFound, setNotFound] = useState(false);

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