import { useEffect, useState } from "react";
import { getUserFollowers, getUserFollowings } from "../../API/UserApi";
import { useParams } from "react-router-dom";

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

    return (
        <div>
            <h1>{type === "follower" ? "팔로워 페이지" : "팔로잉 페이지"}</h1>
        </div>
    );
};

export default UserFollowPage;
