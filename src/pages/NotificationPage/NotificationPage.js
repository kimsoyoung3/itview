import React, { useEffect, useState } from "react";
import "./NotificationPage.css"
import { getNotification } from "../../API/UserApi";
import {NavLink} from "react-router-dom";

const NotificationPage = ({ userInfo, openLogin }) => {

    const [activeId, setActiveId] = useState("notification-tab1");

    const [notifications, setNotifications] = useState([]);
    useEffect(() => {
        console.log(notifications)
    }, [notifications]);

    useEffect(() => {
        const fetchNotifications = async () => {
            const res = await getNotification();
            setNotifications(res.data);
        }

        fetchNotifications();
    }, []);

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
                                <div className="notification-tab-content-list">
                                    {notifications?.content.map(item =>
                                        <div className="notification-tab-content-wrap">
                                            <div className="notification-tab-content-profile">
                                                <NavLink to={`/user/${item.actorId}`}>
                                                    <img src={item.profile ? item.profile : `${process.env.PUBLIC_URL}/user.png`} alt=""/>
                                                </NavLink>
                                            </div>
                                            <div className="notification-tab-content-title">
                                                <NavLink to={item.link}>
                                                    <div>{item.title}</div>
                                                    <div>{new Date(item.createdAt).toLocaleDateString().slice(0, -1)}</div>
                                                </NavLink>
                                            </div>
                                        </div>
                                    )}

                                    <div className="notification-tab-content-btn">
                                        <button>더보기</button>
                                    </div>
                                </div>
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