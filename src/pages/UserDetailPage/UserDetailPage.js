import React, { useEffect, useRef, useState } from "react";
import {NavLink, useParams} from "react-router-dom";
import "./UserDetailPage.css"
import {deleteUser, followUser, getUserDetail, link, unfollowUser, unlink, updateUserProfile} from "../../API/UserApi";
import { toast } from "react-toastify";
import NotFound from "../NotFound/NotFound";

const UserDetailPage = ({ userInfo, openLogin }) => {
    const [notFound, setNotFound] = useState(false);

    const { id } = useParams();

    const [userInfoData, setUserInfoData] = useState(null);
    useEffect(() => {
        setUserInfoData(userInfo)
    }, [userInfo])

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
                setNotFound(true);
            }
        };
        fetchUserDetail();
    }, [id]);

    useEffect(() => {
        if (userDetail) {
            console.log(userDetail);
        }
    }, [userDetail]);

    const [setting, setSetting] =useState()
    const openSetting = () => setSetting(true);
    const closeSetting = () => setSetting(false);

    const [myProfileEditModal, setMyProfileEditModal] = useState();
    const openMyProfileEdit = () => setMyProfileEditModal(true);
    const closeMyProfileEdit = () => setMyProfileEditModal(false);

    const nameRef = useRef(null);
    const introductionRef = useRef(null);

    useEffect(() => {
        if (nameRef.current && introductionRef.current && userDetail) {
            nameRef.current.value = userDetail?.userProfile.nickname;
            introductionRef.current.value = userDetail?.userProfile.introduction;
        }
    }, [myProfileEditModal, userDetail]);

    const handleImageChange = (e) => {
        console.log("이미지 변경");
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                const imageUrl = reader.result;
                document.querySelector(".my-profile-edit-image img").src = imageUrl;
            };
            reader.readAsDataURL(file);
        }
    }

    const handleProfileUpdate = async (e) => {
        e.preventDefault();

        const formData = new FormData();

        const nickname = nameRef.current.value;
        const introduction = introductionRef.current.value;
        const profile = e.target.form[0].files[0];

        formData.append("id", userDetail?.userProfile.id);
        formData.append("nickname", nickname);
        formData.append("introduction", introduction);
        if (profile) {
            formData.append("profile", profile);
        }

        console.log(nickname, introduction);

        try {
            const res = await updateUserProfile(formData);
            if (res.status === 200) {
                toast("프로필이 수정되었습니다.");
                closeMyProfileEdit();
                setUserDetail(res.data);
            }
        } catch (error) {
            toast(error.response.data);
        }
    };

    const handleFollow = async () => {
        if (!userInfo) {
            openLogin();
            return;
        }
        try {
            await followUser(id);
            setUserDetail(prev => ({
                ...prev,
                isFollowed: true,
                followerCount: prev.followerCount + 1
            }))
        } catch (error) {
            toast("팔로우에 실패했습니다.");
        }
    };

    const handleUnfollow = async () => {
        try {
            await unfollowUser(id);
            setUserDetail(prev => ({
                ...prev,
                isFollowed: false,
                followerCount: prev.followerCount - 1
            }))
        } catch (error) {
            toast("팔로우 취소에 실패했습니다.");
        }
    };

    const handleSocialLogin = async (provider) => {
        if (userInfo?.[provider]) {
            await unlink({provider}).then(() => {
                toast("연동이 해제되었습니다.");
                setUserInfoData(prev => ({
                    ...prev,
                    [provider] : false
                }))
            });
        } else {
            link({redirectURL: window.location.href});
            window.location.href = `${process.env.REACT_APP_API_URL}/oauth2/authorization/${provider}`;
        }
    }

    return (notFound ? <NotFound /> :
        <div className="user-detail-page">
            <div className="user-detail-page-wrap container">
                <div className="user-detail-info">
                    <div className="user-detail-info-profile">
                        <img src={userDetail?.userProfile.profile ? userDetail?.userProfile.profile : `${process.env.PUBLIC_URL}/user.png`} alt=""/>
                    </div>
                    <h5 className="user-detail-info-name">{userDetail?.userProfile.nickname}</h5>
                    <div className="user-detail-info-follow">
                        <NavLink to={`/user/${userDetail?.userProfile.id}/follower`}>팔로워<span> {userDetail?.followerCount}</span></NavLink>
                        <NavLink to={`/user/${userDetail?.userProfile.id}/following`}>팔로잉<span> {userDetail?.followingCount}</span></NavLink>
                    </div>
                    <p className="user-detail-info-intro">{userDetail?.userProfile.introduction}</p>
                    <div className="user-detail-info-btn">
                        <div style={{display: userInfo?.userId === userDetail?.userProfile.id ? "block" : "none"}}>
                            <button onClick={openMyProfileEdit}>프로필 수정</button>
                        </div>
                        <div style={{display: userInfo?.userId !== userDetail?.userProfile.id ? "block" : "none"}}>
                            <button className="user-detail-info-edit" style={{display: userDetail?.isFollowed ? "block" : "none"}} onClick={handleUnfollow}>팔로우 취소</button>
                            <button style={{display: !userDetail?.isFollowed ? "block" : "none"}} onClick={handleFollow}>팔로우</button>
                        </div>

                        <div className="user-detail-info-share">
                            <button onClick={() => {
                                const url = "http://localhost:3000/user/" + userDetail?.userProfile.id;
                                navigator.clipboard.writeText(url)
                                    .then(() => {
                                        toast("링크가 복사되었습니다.")
                                    });
                                }
                            }>프로필 공유</button>
                        </div>
                    </div>

                    {/*설정 모달*/}
                    <div className="user-setting">
                        {setting && (
                            <div className="modal-overlay" onClick={closeSetting}>
                                <div className="user-setting-modal-wrap" onClick={(e) => e.stopPropagation()}>
                                    <div className="user-setting-modal-title">
                                        <p>설정</p>
                                        <button onClick={closeSetting}><i className="bi bi-x-lg"></i></button>
                                    </div>
                                    <div className="user-setting-modal-list-wrap">
                                        <p>SNS 로그인 연동</p>

                                        <div className="user-setting-modal-list">
                                            <div className="user-setting-modal-list-content">
                                                <p>카카오</p>
                                                <div className="user-setting-modal-sns-btn">
                                                    <span></span>
                                                    <span className={userInfoData?.kakao ? "user-setting-modal-sns-btn-click" : "" } onClick={() => handleSocialLogin("kakao")}></span>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="user-setting-modal-list">
                                            <div className="user-setting-modal-list-content">
                                                <p>구글</p>
                                                <div className="user-setting-modal-sns-btn">
                                                    <span></span>
                                                    <span className={userInfoData?.google ? "user-setting-modal-sns-btn-click" : "" } onClick={() => handleSocialLogin("google")}></span>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="user-setting-modal-list">
                                            <div className="user-setting-modal-list-content">
                                                <p>네이버</p>
                                                <div className="user-setting-modal-sns-btn">
                                                    <span></span>
                                                    <span className={userInfoData?.naver ? "user-setting-modal-sns-btn-click" : "" } onClick={() => handleSocialLogin("naver")}></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="user-setting-modal-list-wrap">
                                        <div className="user-setting-modal-list">
                                            <div className="user-setting-modal-list-content">
                                                <p onClick={deleteUser}>탈퇴하기</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        <div className="user-setting-btn" hidden={userInfo?.userId !== userDetail?.userProfile.id}>
                            <button onClick={openSetting}>
                                <img src={`${process.env.PUBLIC_URL}/icon/setting.svg`} alt=""/>
                            </button>
                        </div>
                    </div>
                </div>

                <div className="user-detail-info-group">
                    <div className="user-detail-info-group-content">
                        <NavLink to={`/user/${id}/rating`}>
                            <p>{userDetail?.ratingCount}</p>
                            <p>평가</p>
                        </NavLink>
                    </div>

                    <div className="user-detail-info-group-content">
                        <NavLink to={`/user/${id}/comment`}>
                            <p>{userDetail?.commentCount}</p>
                            <p>코멘트</p>
                        </NavLink>
                    </div>

                    <div className="user-detail-info-group-content">
                        <NavLink to={`/user/${id}/collection`}>
                            <p>{userDetail?.collectionCount}</p>
                            <p>컬렉션</p>
                        </NavLink>
                    </div>
                </div>

                {/*마이프로필 수정 모달*/}
                {myProfileEditModal && (
                    <div className="modal-overlay" onClick={closeMyProfileEdit}>
                        <div className="my-profile-edit-modal" onClick={(e) => e.stopPropagation()}>
                            <div className="my-profile-edit-modal-hd">
                                <p>프로필 수정</p>
                                <button className="my-profile-edit-close-btn" onClick={closeMyProfileEdit}><i className="bi bi-x-lg"></i></button>
                            </div>
                            <form action="">
                                <div className="my-profile-edit-image-wrap">
                                    <div className="my-profile-edit-image">
                                        <label htmlFor="image-input">
                                            <img src={userDetail?.userProfile.profile ? userDetail?.userProfile.profile : `${process.env.PUBLIC_URL}/user.png`} alt=""/>
                                            <div className="image-input-btn-box"><img src={`${process.env.PUBLIC_URL}/icon/camera.svg`} className="image-input-btn" alt=""/></div>
                                        </label>
                                        <input id="image-input" type="file" onChange={handleImageChange}/>
                                    </div>

                                    <label className="nickName-input" htmlFor="nickName-input">
                                        <p>닉네임</p>
                                        <input id="nickName-input" type="text" placeholder="닉네임을 입력해주세요." maxLength="20" ref={nameRef}/>
                                    </label>

                                    <label className="introduction-input" htmlFor="nickName-input">
                                        <p>소개</p>
                                        <textarea id="introduction-input" placeholder="소개를 입력해주세요." rows={1} maxLength="60" ref={introductionRef}/>
                                    </label>

                                    <button className="form-btn" onClick={handleProfileUpdate}>확인</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                <div className="user-detail-collection">
                    <p>보관함</p>
                    <div className="user-detail-collection-wrap">
                        <div className="user-detail-collection-content">
                            <NavLink to={`/user/${id}/content/movie`}>
                                <div><i className="bi bi-film"></i></div>
                                <p>영화</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink to={`/user/${id}/content/series`}>
                                <div><i className="bi bi-collection-play"></i></div>
                                <p>시리즈</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink to={`/user/${id}/content/book`}>
                                <div><i className="bi bi-book"></i></div>
                                <p>책</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink to={`/user/${id}/content/webtoon`}>
                                <div><i className="bi bi-columns"></i></div>
                                <p>웹툰</p>
                            </NavLink>
                        </div>

                        <div className="user-detail-collection-content">
                            <NavLink to={`/user/${id}/content/record`}>
                                <div><i className="bi bi-vinyl"></i></div>
                                <p>음반</p>
                            </NavLink>
                        </div>
                    </div>
                </div>

                <div className="user-detail-like">
                    <p>좋아요</p>
                    <NavLink to={`/user/${id}/like?type=person`} className="user-detail-like-list">
                        <div>좋아한 인물 <span>{userDetail?.personLikeCount}</span></div>
                        <button><i className="bi bi-chevron-right"></i></button>
                    </NavLink>

                    <NavLink to={`/user/${id}/like?type=collection`} className="user-detail-like-list">
                        <div>좋아한 컬렉션 <span>{userDetail?.collectionLikeCount}</span></div>
                        <button><i className="bi bi-chevron-right"></i></button>
                    </NavLink>

                    <NavLink to={`/user/${id}/like?type=comment`} className="user-detail-like-list">
                        <div>좋아한 코멘트 <span>{userDetail?.commentLikeCount}</span></div>
                        <i className="bi bi-chevron-right"></i>
                    </NavLink>
                </div>
            </div>
        </div>
    );
};

export default UserDetailPage;
