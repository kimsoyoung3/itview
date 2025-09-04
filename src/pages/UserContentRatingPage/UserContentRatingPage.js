import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserContentRating } from "../../API/UserApi";
import NotFound from "../NotFound/NotFound";

const UserContentRatingPage = () => {
    const [notFound, setNotFound] = useState(false);

    const { id, contentType } = useParams();

    const [ratings, setRatings] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getUserContentRating(id, contentType);
                setRatings(response.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id, contentType]);

    useEffect(() => {
        console.log(ratings);
    }, [ratings]);

    return (notFound ? <NotFound /> :
        <div>
            <h1>User Content Rating Page</h1>
        </div>
    );
};

export default UserContentRatingPage;
