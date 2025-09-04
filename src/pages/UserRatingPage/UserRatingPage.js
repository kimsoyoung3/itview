import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getUserRatingCount } from '../../API/UserApi';
import NotFound from '../NotFound/NotFound';

function UserRatingPage({ userInfo, openLogin }) {
    const [notFound, setNotFound] = useState(false);

    const { id } = useParams();

    const [userRatingCount, setUserRatingCount] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getUserRatingCount(id);
                setUserRatingCount(res.data);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(userRatingCount);
    }, [userRatingCount]);

    return (notFound ? <NotFound /> :
        <div>UserRatingPage</div>
    )
}

export default UserRatingPage;