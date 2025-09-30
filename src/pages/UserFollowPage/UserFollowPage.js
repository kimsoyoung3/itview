import React, { useEffect, useState } from "react";
import { followUser, getUserFollowers, getUserFollowings, unfollowUser } from "../../API/UserApi";
import {NavLink, useParams} from "react-router-dom";
import "./UserFollowPage.css"
import { toast } from "react-toastify";

const UserFollowPage = ({userInfo, openLogin, type}) => {

    const {id} = useParams();

    const [followData, setFollowData] = useState([]);
    useEffect(() => {
        console.log(followData);
    }, [followData]);

    useEffect(() => {
        const fetchFollowData = async () => {
            try {
                if (type === "follower") {
                    const res = await getUserFollowers(id, 1);
                    setFollowData(res.data);
                }
                else if (type === "following") {
                    const res = await getUserFollowings(id, 1);
                    setFollowData(res.data);
                }
            } catch (error) {
                console.error("팔로우 데이터를 가져오는 중 오류 발생:", error);
            }
        };
        fetchFollowData();
    }, [id, type]);

    const handleFollow = async (e, isFollowed, targetUserId, index) => {
        e.preventDefault();
        if (!userInfo) {
            openLogin();
            return;
        }
        if (isFollowed) {
            try {
                await unfollowUser(targetUserId);
                const updatedFollowData = { ...followData };
                updatedFollowData.content[index].isFollowed = false;
                setFollowData(updatedFollowData);
            } catch (error) {
                toast("팔로우 취소 중 오류가 발생했습니다.");
            }
        } else {
            try {
                await followUser(targetUserId);
                const updatedFollowData = { ...followData };
                updatedFollowData.content[index].isFollowed = true;
                setFollowData(updatedFollowData);
            } catch (error) {
                toast("팔로우 중 오류가 발생했습니다.");
            }
        }
    }

    return (
        <div className="user-follow-page">
            <div className="user-follow-page-wrap container">
                <h1>{type === "follower" ? "팔로워" : "팔로잉 중"}</h1>

                {followData?.content?.length > 0 ?(
                    <div className="user-follow-content-list">
                        {followData?.content?.map((item, index) =>
                            <NavLink key={item.userProfile.id} to={`/user/${item?.userProfile?.id}`} className="user-follow-content-wrap">
                                <div className="user-follow-profile">
                                    <img src={item.userProfile.profile ? item.userProfile.profile : `${process.env.PUBLIC_URL}/user.png`} alt=""/>
                                </div>
                                <div className="user-follow-info">
                                    <div className="user-follow-info-wrap">
                                        <p>{item.userProfile.nickname}</p>
                                        <p>
                                            {item.userProfile.introduction ? (
                                                item.userProfile.introduction
                                            ) : (
                                                <>
                                                    {item.ratingCount > 0 && <>평가 {item.ratingCount} </>}
                                                    {item.commentCount > 0 && <>&middot; 댓글 {item.commentCount} </>}
                                                    {item.collectionCount > 0 && <>&middot; 컬렉션 {item.collectionCount}</>}
                                                </>
                                            )}
                                        </p>
                                    </div>

                                    <div className="user-follow-info-btn">
                                        <button onClick={(e) => handleFollow(e, item.isFollowed, item.userProfile.id, index)}>{item.isFollowed ? "언팔로우" : "팔로우"}</button>
                                    </div>
                                </div>



                            </NavLink>
                        )}
                    </div>
                ) : (
                    <p className="empty-message">{type === "follower" ? "팔로워 검색 결과가 없습니다 :)" : "팔로잉 검색 결과가 없습니다 :) "}</p>
                )}

            </div>
        </div>
    );
};

export default UserFollowPage;
