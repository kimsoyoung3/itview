import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserContentRating } from "../../API/UserApi";
import NotFound from "../NotFound/NotFound";
import "./UserContentRatingPage.css"
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import { Navigation } from 'swiper/modules';
import { toast } from "react-toastify";

const UserContentRatingPage = ({userInfo}) => {
    const [notFound, setNotFound] = useState(false);

    const { id, contentType } = useParams();

    const [ratings, setRatings] = useState([]);
    const [order, setOrder] = useState("new");

    const [activeId, setActiveId] = useState("rating-page-tab1");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getUserContentRating(id, contentType, 1, order);
                setRatings(response.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id, contentType, order]);

    useEffect(() => {
        console.log(ratings);
    }, [ratings]);

    const handleMoreClick = async () => {
        try {
            const response = await getUserContentRating(id, contentType, ratings.page.number + 2, order);
            setRatings((prev) => ({
                ...prev,
                content: [...prev.content, ...response.data.content],
                page: response.data.page
            }));
        } catch (error) {
            toast("에러 발생");
        }
    };

    return (notFound ? <NotFound /> :
        <div className="user-content-rating-page container">
            <div className="user-content-rating-page-wrap">
                <h1>평가한 작품들</h1>
                <div className="rating-wrap">
                    <div className="rating-wrap-title">
                        <p className={`rating-page-tab-btn ${activeId === "rating-page-tab1" ? "active" : ""}`}
                           onClick={(e) => setActiveId(e.target.id)} id="rating-page-tab1">전체</p>
                        <p className={`rating-page-tab-btn ${activeId === "rating-page-tab2" ? "active" : ""}`}
                           onClick={(e) => setActiveId(e.target.id)} id="rating-page-tab2">별점순</p>
                    </div>

                    {/*평가 페이지 전체순*/}
                    {activeId === "rating-page-tab1" && <div className="rating-page-tab1">
                        {/*셀렉트박스*/}
                        <div className="user-content-rating-page-select-box">
                            <select className="form-select user-content-rating-page-select"  aria-label="Default select example" onChange={(e) => setOrder(e.target.value)} value={order}>
                                <option selected value="new">담은 순</option>
                                <option value="old">담은 역순</option>
                                <option value="my_score_high">{userInfo === id ? "나의 별점 높은 순" : "이 회원의 별점 높은 순"}</option>
                                <option value="my_score_low">{userInfo === id ? "나의 별점 낮은 순" : "이 회원의 별점 낮은 순"}</option>
                                <option value="avg_score_high">평균 별점 높은 순</option>
                                <option value="avg_score_low">평균 별점 낮은 순</option>
                            </select>
                        </div>

                        {/*컨텐츠 리스트*/}
                        <div className="user-content-rating-page-content-list">
                            {ratings?.content?.map((item) => (
                                <div key={item.content.id} className="user-content-rating-page-content">
                                    {/* 포스터 이미지 */}
                                    <div className="rating-page-content-poster">
                                        <img
                                            src={item.content.poster}
                                            alt={item.content.title}/>
                                    </div>
                                    <p>{item.content.title}</p>
                                    <p>평가함 <i className="bi bi-star-fill"/> {item.score / 2}</p>
                                </div>
                            ))}
                        </div>
                        <div><button onClick={handleMoreClick} style={{display: ratings?.page?.number + 1 === ratings?.page?.totalPages ? "none" : "block"}}>더보기</button></div>
                    </div>}

                    {activeId === "rating-page-tab2" && <div className="rating-page-tab2">
                        {[
                            5, 4.5, 4, 3.5, 3, 2.5, 2, 1.5, 1, 0.5
                        ].map((score) => {
                            // 해당 점수 작품만 필터링
                            const items = ratings?.content?.filter((item) => item.score / 2 === score) || [];

                            return (
                                <div key={score} className="rating-page-rating-list">
                                    <p>{score}점 평가한 작품들</p>

                                    <div className="rating-items">
                                        {items.length > 0 ? (
                                            <div className="rating-wrapper">
                                                <Swiper
                                                    modules={Navigation}
                                                    navigation={{
                                                        prevEl: ".rating-wrapper .rating-prev",
                                                        nextEl: ".rating-wrapper .rating-next",
                                                    }}
                                                    spaceBetween={16}
                                                    slidesPerView={5} // 한 화면에 보일 아이템 수
                                                    breakpoints={{
                                                        640: { slidesPerView: 2 },
                                                        768: { slidesPerView: 3 },
                                                        1024: { slidesPerView: 4 },
                                                        1280: { slidesPerView: 10 },
                                                    }}
                                                >
                                                    {items.map((item) => (
                                                        <SwiperSlide key={item.content.id} className="rating-slide-image">
                                                            <div className="user-content-rating-page-content">
                                                                <div className="rating-page-content-poster">
                                                                    <img
                                                                        src={item.content.poster}
                                                                        alt={item.content.title}
                                                                    />
                                                                </div>
                                                                <p className="title">{item.content.title}</p>
                                                                <p>
                                                                    평가함 <i className="bi bi-star-fill" /> {item.score}
                                                                </p>
                                                            </div>
                                                        </SwiperSlide>
                                                    ))}
                                                </Swiper>

                                                <div className="rating-prev"><img src="/icon/arrow-left-555.svg" alt=""/></div>
                                                <div className="rating-next"><img src="/icon/arrow-right-555.svg" alt=""/></div>
                                            </div>
                                        ) : (
                                            <p className="rating-text">해당 점수 작품이 없습니다 :)</p>
                                        )}
                                    </div>
                                </div>
                            );
                        })}
                    </div>}

                </div>
            </div>
        </div>
    );
};

export default UserContentRatingPage;
