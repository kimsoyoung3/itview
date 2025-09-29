import React, { useEffect, useState } from "react";
import "./NotificationPage.css"
import { getNotification } from "../../API/UserApi";

const NotificationPage = ({ userInfo, openLogin }) => {

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
        <div className="notification-page">소식페이지</div>
    );

};
export default NotificationPage;