import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
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
        <div className="user-detail-page">
            <h1>User Detail Page</h1>
            {/* Render user information here */}
        </div>
    );
};

export default UserDetailPage;
