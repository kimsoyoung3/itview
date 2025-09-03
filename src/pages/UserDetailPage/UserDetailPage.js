import { useEffect, useState } from "react";
import {NavLink, useParams} from "react-router-dom";
import "./UserDetailPage.css"
import { getUserDetail } from "../../API/UserApi";

const UserDetailPage = ({ userInfo, openLogin }) => {

    const { id } = useParams();

    const [userDetail, setUserDetail] = useState(null);

    useEffect(() => {
        const fetchUserDetail = async () => {
            try {
                const res = await getUserDetail(id);
                if (res) {
                    setUserDetail(res.data);
                } else {
                    alert("유저 정보를 불러오지 못했습니다.");
                }
            } catch (error) {
                console.error("Error fetching user detail:", error);
                alert("유저 정보를 불러오지 못했습니다.");
            }
        };
        fetchUserDetail();
    }, [id]);

    useEffect(() => {
        if (userDetail) {
            console.log(userDetail);
        }
    }, [userDetail]);

    return (
        <div className="user-detail-page container">
            <div className="user-detail-page-wrap">
                <div className="user-detail-info">
                    <div className="user-detail-info-profile">
                        <img src={userDetail?.userProfile.profile ? userDetail?.userProfile.profile : "/user.png" } alt=""/>
                    </div>
                    <h5 className="user-detail-info-name">{userDetail?.userProfile.nickname}</h5>
                    <p className="user-detail-info-intro">{userDetail?.userProfile.introduction}</p>
                    <div className="user-detail-info-btn">
                        <div className="user-detail-info-edit">
                            <button>프로필 수정</button>
                        </div>

                        <div className="user-detail-info-share">
                            <button>프로필 공유</button>
                        </div>
                    </div>
                    <div className="user-detail-info-group">
                        <div className="user-detail-info-group-content">
                            <NavLink>
                                <p>{userDetail?.ratingCount}</p>
                                <p>평가</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-info-group-content">
                            <NavLink>
                                <p>{userDetail?.commentCount}</p>
                                <p>코멘트</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-info-group-content">
                            <NavLink>
                                <p>{userDetail?.collectionCount}</p>
                                <p>컬렉션</p>
                            </NavLink>
                        </div>
                    </div>
                </div>

                <div className="user-detail-collection">
                    <p>보관함</p>
                    <div className="user-detail-collection-wrap">
                        <div className="user-detail-collection-content">
                            <NavLink>
                                <div><i className="bi bi-film"></i></div>
                                <p>영화</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink>
                                <div><i className="bi bi-collection-play"></i></div>
                                <p>시리즈</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink>
                                <div><i className="bi bi-book"></i></div>
                                <p>책</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink>
                                <div><i className="bi bi-columns"></i></div>
                                <p>웹툰</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink>
                                <div><i className="bi bi-vinyl"></i></div>
                                <p>음반</p>
                            </NavLink>
                        </div>
                    </div>
                </div>

                <div className="user-detail-like">
                    <p>좋아요</p>
                    <NavLink className="user-detail-like-list">
                        <div>좋아한 인물 <span>{userDetail?.personLikeCount}</span></div>
                        <button><i className="bi bi-chevron-right"></i></button>
                    </NavLink>

                    <NavLink className="user-detail-like-list">
                        <div>좋아한 컬렉션 <span>{userDetail?.collectionLikeCount
                        }</span></div>
                        <button><i className="bi bi-chevron-right"></i></button>
                    </NavLink>

                    <NavLink className="user-detail-like-list">
                        <div>좋아한 코멘트 <span>{userDetail?.commentCount}</span></div>
                        <i className="bi bi-chevron-right"></i>
                    </NavLink>
                </div>
            </div>
        </div>
    );
};

export default UserDetailPage;
