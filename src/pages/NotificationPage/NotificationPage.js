import React, { useEffect, useState } from "react";
import "./NotificationPage.css"
import { getFriendNotification, getNotification } from "../../API/UserApi";
import {NavLink, useNavigate, useSearchParams} from "react-router-dom";
import { toast } from "react-toastify";
import Markdown from "react-markdown";

const NotificationPage = ({ userInfo, openLogin }) => {

    const [searchParams] = useSearchParams();
    const type = searchParams.get("type");
    useEffect(() => {
        console.log(type)
        if (type === "friend") {
            setActiveId("notification-tab2");
        } else {
            setActiveId("notification-tab1");
        }
    }, [type]);

    const navigate = useNavigate();
    if (!userInfo) {
        navigate("/");
    }

    const [activeId, setActiveId] = useState("notification-tab1");

    const [notifications, setNotifications] = useState([]);
    useEffect(() => {
        console.log(notifications)
    }, [notifications]);

    useEffect(() => {
        const fetchNotifications = async () => {
            if (type === "friend") {
                const res = await getFriendNotification(1);
                setNotifications(res.data);
            } else {
                const res = await getNotification(1);
                setNotifications(res.data);
            }
        }

        fetchNotifications();
    }, [type]);

    const handleLoadMore = async () => {
        try {
            if (activeId === "notification-tab1") {
                const res = await getNotification(notifications.page.number + 2);
                setNotifications(prev => ({
                    content: [...prev.content, ...res.data.content],
                    page: res.data.page
                }));
            } else if (activeId === "notification-tab2") {
                const res = await getFriendNotification(notifications.page.number + 2);
                setNotifications(prev => ({
                    content: [...prev.content, ...res.data.content],
                    page: res.data.page
                }));
            }
        } catch (e) {
            toast("소식을 불러오는데 실패했습니다.");
        }
    }

    const timeAgo = (dateString) => {
        const date = new Date(dateString);
        const now = new Date();
        const diff = (now - date) / 1000; // 초 단위

        if (diff < 60) return "방금 전";
        if (diff < 3600) return `${Math.floor(diff / 60)}분 전`;
        if (diff < 86400) return `${Math.floor(diff / 3600)}시간 전`;
        if (diff < 604800) return `${Math.floor(diff / 86400)}일 전`;
        if (diff < 2592000) return `${Math.floor(diff / 604800)}주 전`;
        if (diff < 31536000) return `${Math.floor(diff / 2592000)}개월 전`;
        return `${Math.floor(diff / 31536000)}년 전`;
    }

    return (
        <div className="notification-page">
            <div className="notification-page-wrap container">
                <h1>소식</h1>

                <div className="notification-tab-wrap">
                    <div className="notification-tab-title">
                        <div className={`notification-tab-btn ${activeId === "notification-tab1" ? "active" : ""}`}
                             onClick={() => navigate("/notification?type=my")}>내 소식</div>
                        <div className={`notification-tab-btn ${activeId === "notification-tab2" ? "active" : ""}`}
                             onClick={() => navigate("/notification?type=friend")}>친구 소식</div>

                        <span
                            className="notification-tab-indicator"
                            style={{
                                width: activeId === "notification-tab1" ? "50%" : "50%",
                                transform:
                                    activeId === "notification-tab1"
                                        ? "translateX(0%)"
                                        : "translateX(100%)",
                            }}
                        />
                    </div>

                    <div className="notification-tab-content">
                            {notifications?.content?.length > 0 ? (
                                <>
                                <div className="notification-tab-content-list">
                                    {notifications?.content.map((item, index) =>
                                        <div key={index} className="notification-tab-content-wrap">
                                            <div className="notification-tab-content-profile">
                                                <NavLink to={`/user/${item.actorId}`}>
                                                    <img src={item.profile ? item.profile : `${process.env.PUBLIC_URL}/user.png`} alt=""/>
                                                </NavLink>
                                            </div>
                                            <div className="notification-tab-content-title">
                                                <NavLink to={item.link}>
                                                    <Markdown>{item.title}</Markdown>
                                                    <div>{timeAgo(item.createdAt)}</div>
                                                </NavLink>
                                            </div>
                                        </div>
                                    )}
                                </div>
                                <div className="notification-tab-content-btn">
                                    <button onClick={handleLoadMore} hidden={notifications.page.number + 1 >= notifications.page.totalPages}>더보기</button>
                                </div>
                                </>
                            ) : (
                                <p className="empty-message">소식이 없습니다 :)</p>
                            )}
                    </div>
                </div>
            </div>
        </div>
    );

};
export default NotificationPage;