import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserWishlist } from "../../API/UserApi";
import NotFound from "../NotFound/NotFound";

function UserContentWishPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = React.useState(false);

    const { id, contentType } = useParams();

    const [wishlist, setWishlist] = useState([]);

    useEffect(() => {
        const fetchWishlist = async () => {
            try {
                const response = await getUserWishlist(id, contentType, 1, "new");
                setWishlist(response.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchWishlist();
    }, [id, contentType]);

    useEffect(() => {
        console.log(wishlist);
    }, [wishlist]);

    return (notFound ? <NotFound /> :
        <div className="user-content-wish-page container">
            <div className="user-content-wish-page-wrap">
                <h1>보고싶어요</h1>
                <p>준비중입니다.</p>
            </div>
        </div>
    );
}

export default UserContentWishPage;