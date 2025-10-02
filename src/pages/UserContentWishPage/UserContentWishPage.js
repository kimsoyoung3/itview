import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserWishlist } from "../../API/UserApi";
import NotFound from "../NotFound/NotFound";
import "./UserContentWishPage.css"
import ContentEach from "../../components/ContentEach/ContentEach";
import CustomSelect from "../../components/CustomSelect/CustomSelect";

function UserContentWishPage({userInfo, openLogin}) {
    /* 에러 상태 */
    const [notFound, setNotFound] = useState(false);

    /* URL 파라미터 */
    const {id, contentType} = useParams();

    /* 정렬 옵션 상태 */
    const [order, setOrder] = useState("new");

    /* 위시리스트 데이터 */
    const [wishlist, setWishlist] = useState([]);

    /* 도메인명 매핑 */
    const domainNameMap = {
        "movie": "영화",
        "series": "시리즈",
        "book": "책",
        "webtoon": "웹툰",
        "record": "음반"
    }

    /* 정렬 옵션 리스트 */
    const options = [
        {value: "new", label: "담은 순"},
        {value: "old", label: "담은 역순"},
        {value: "rating_high", label: "평균 별점 높은 순"},
        {value: "rating_low", label: "평균 별점 낮은 순"}
    ];

    /* 위시리스트 데이터 불러오기 */
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

    /* 더 불러오기 */
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

    return (notFound ? <NotFound/> :
            <div className="user-content-wish-page">
                <div className="user-content-wish-page-wrap container">
                    <h1>보고싶어요한 {domainNameMap[contentType]}</h1>

                    {/*셀렉트 박스*/}
                    <div className="user-wish-page-select-box">
                        <CustomSelect value={order} onChange={setOrder} options={options}/>
                    </div>

                    {/*컨텐츠 리스트*/}
                    {wishlist?.content?.length > 0 ? (
                        <div>
                            <div className="user-wish-page-content-list">
                                {wishlist.content.map(item => (
                                    <ContentEach key={item.id} ratingData={{content: item}} ratingType={'avg'}/>
                                ))}
                            </div>

                            <div className="wish-page-content-list-btn"
                                 style={{display: wishlist?.page?.number + 1 === wishlist?.page?.totalPages ? "none" : ""}}>
                                <button onClick={handleMoreClick}>더보기</button>
                            </div>

                        </div>
                    ) : (
                        <p className="empty-message">보고싶어요한 작품이 없습니다 :)</p>
                    )}
                </div>
            </div>
    );
}

export default UserContentWishPage;