import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserWishlist } from "../../API/UserApi";
import NotFound from "../NotFound/NotFound";
import "./UserContentWishPage.css"
import ContentEach from "../../components/ContentEach/ContentEach";

function UserContentWishPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = React.useState(false);

    const { id, contentType } = useParams();

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

    return (/*notFound ? <NotFound /> :*/ /*여기부분 주석 해제하면 에러나더라구여 근데 gpt말로는 백엔드에선 wishlist로 받고 프론트에선 widh로 받아서 값이 달라서 그렇다고 하는데 잘 모르겠어서 남겨요*/
        <div className="user-content-wish-page container">
            <div className="user-content-wish-page-wrap">
                <h1>보고싶어요한 {domainNameMap[contentType]}</h1>

                {/*셀렉트 박스*/}
                <div className="user-wish-page-select-box">
                    <select className="form-select user-wish-page-select" aria-label="Default select example">
                        <option selected value="new">담은 순</option>
                        <option value="old">담은 역순</option>
                        <option value="my_score_high">{Number(userInfo) === Number(id) ? "나의 별점 높은 순" : "이 회원의 별점 높은 순"}</option>
                        <option value="my_score_low">{Number(userInfo) === Number(id) ? "나의 별점 낮은 순" : "이 회원의 별점 낮은 순"}</option>
                        <option value="avg_score_high">평균 별점 높은 순</option>
                        <option value="avg_score_low">평균 별점 낮은 순</option>
                    </select>
                </div>

                {/*컨텐츠 리스트*/}
                <div className="user-wish-page-content-list">
                    {/*여기부분은 컨텐츠 맵 돌리는건데 아무리 해도 안되더라구여.. 버튼이랑 여기부분 클래스네임으로 평가부분이랑 똑같이 10개씩 보이는걸로 해놨어요..
                    평가부분은 레이팅에서 컨텐츠안에 배열에서 컨텐츠를 받아오는데 여기는 위시리스트에서 바로 배열로 컨텐츠 받아오더라구여 근데 안돼여ㅜㅜ 자꾸 초기값 지정하라해서 일단 안건드리고
                    주석처리 해놨어여..*/}
                    {/*{wishlist?.content?.length > 0 ? (
                        wishlist?.content?.map(item => (
                            <ContentEach key={item.id} ratingData={item} ratingType={'user'}/>
                        ))
                    ) : (
                        <p>준비중입니다.</p>
                    )}*/}
                </div>
                <div className="wish-page-content-list-btn"><button>더보기</button></div>
            </div>
        </div>
    );
}

export default UserContentWishPage;