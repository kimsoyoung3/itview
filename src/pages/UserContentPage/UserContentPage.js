import React, { useEffect, useState } from "react";
import { getUserContentCount } from "../../API/UserApi";
import { useParams } from "react-router-dom";
import NotFound from "../NotFound/NotFound";

function UserContentPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = useState(false);

    const { id, contentType } = useParams();
    
    const [userContentCount, setUserContentCount] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getUserContentCount(id, contentType);
                setUserContentCount(res.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id, contentType]);

    useEffect(() => {
        console.log(userContentCount);
    }, [userContentCount]);

    return (notFound ? <NotFound /> :
        <div>
            <h1>User Content Page</h1>
        </div>
    );
}

export default UserContentPage;
