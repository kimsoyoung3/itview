import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserWishlist } from "../../API/UserApi";
import NotFound from "../NotFound/NotFound";
import "./UserContentWishPage.css"
import ContentEach from "../../components/ContentEach/ContentEach";

function UserContentWishPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = React.useState(false);

    const { id, contentType } = useParams();

    const [order, setOrder] = useState("new");

    const [wishlist, setWishlist] = useState([]);

    const domainNameMap = {
        "movie" : "영화",
        "series" : "시리즈",
        "book" : "책",
        "webtoon" : "웹툰",
        "record" : "음반"
    }

    useEffect(() => {
        const fetchWishlist = async () => {
            try {
                const response = await getUserWishlist(id, contentType, 1, order);
                setWishlist(response.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchWishlist();
    }, [id, contentType, order]);

    useEffect(() => {
        console.log(wishlist);
    }, [wishlist]);

    const handleMoreClick = async () => {
        try {
            const response = await getUserWishlist(id, contentType, wishlist.page.number + 2, order);
            setWishlist(prevWishlist => ({
                ...prevWishlist,
                content: [...prevWishlist.content, ...response.data.content],
                page: response.data.page
            }));
        } catch (error) {
            console.error("더보기 실패:", error);
        }
    }

    return (notFound ? <NotFound /> :
        <div className="user-content-wish-page container">
            <div className="user-content-wish-page-wrap">
                <h1>보고싶어요한 {domainNameMap[contentType]}</h1>

                {/*셀렉트 박스*/}
                <div className="user-wish-page-select-box">
                    <select className="form-select user-wish-page-select" aria-label="Default select example"
                            onChange={(e) => setOrder(e.target.value)}>
                        <option selected value="new">담은 순</option>
                        <option value="old">담은 역순</option>
                        <option value="rating_high">평균 별점 높은 순</option>
                        <option value="rating_low">평균 별점 낮은 순</option>
                    </select>
                </div>

                {/*컨텐츠 리스트*/}
                <div className="user-wish-page-content-list">
                    {wishlist?.content?.length > 0 ? (
                        wishlist.content.map(item => (
                            <ContentEach key={item.id} ratingData={{content : item}} ratingType={'avg'}/>
                        ))
                    ) : (
                        <p>위시리스트가 없어용~</p>
                    )}
                </div>
                <div className="wish-page-content-list-btn" style={{display: wishlist?.page?.number + 1 === wishlist?.page?.totalPages ? "none" : ""}}><button onClick={handleMoreClick}>더보기</button></div>
            </div>
        </div>
    );
}

export default UserContentWishPage;