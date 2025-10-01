import React, { useEffect, useState } from "react";
import "./NotificationPage.css"
import { getFriendNotification, getNotification } from "../../API/UserApi";
import {NavLink, useNavigate} from "react-router-dom";
import { toast } from "react-toastify";
import Markdown from "react-markdown";

const NotificationPage = ({ userInfo, openLogin }) => {

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
            if (activeId === "notification-tab1") {
                const res = await getNotification(1);
                setNotifications(res.data);
            } else if (activeId === "notification-tab2") {
                const res = await getFriendNotification(1);
                setNotifications(res.data);
            }
        }

        fetchNotifications();
    }, [activeId]);

    const handleLoadMore = async () => {
        try {
            const res = await getNotification(notifications.page.number + 2);
            setNotifications(prev => ({
                content: [...prev.content, ...res.data.content],
                page: res.data.page
            }));
        } catch (e) {
            toast("소식을 불러오는데 실패했습니다.");
        }
    }

    return (
        <div className="notification-page">
            <div className="notification-page-wrap container">
                <h1>소식</h1>

                <div className="notification-tab-wrap">
                    <div className="notification-tab-title">
                        <div className={`notification-tab-btn ${activeId === "notification-tab1" ? "active" : ""}`}
                             onClick={() => setActiveId('notification-tab1')}>내 소식</div>
                        <div className={`notification-tab-btn ${activeId === "notification-tab2" ? "active" : ""}`}
                             onClick={() => setActiveId('notification-tab2')}>친구 소식</div>

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
                        {activeId === "notification-tab1" && <div className="notification-tab1 notification-tab">
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
                                                    <div>{new Date(item.createdAt).toLocaleDateString().slice(0, -1)}</div>
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
                        </div>}

                        {activeId === "notification-tab2" && <div className="notification-tab2 notification-tab">
                        </div>}
                    </div>
                </div>
            </div>
        </div>
    );

};
export default NotificationPage;