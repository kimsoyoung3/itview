import { React, useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { getUserLikePerson } from '../../API/UserApi';

function UserLikePage() {
    const [notFound, setNotFound] = useState(false);

    const [searchParams] = new useSearchParams();
    const type = searchParams.get("type") || "person";

    const { id } = useParams();
    const [personLikes, setPersonLikes] = useState({});

    useEffect(() => {
        try {
            const fetchData = async () => {
                const res = await getUserLikePerson(id, 1);
                setPersonLikes(res.data);
            }
            fetchData();
        } catch (error) {
            setNotFound(true);
        }
    }, [id]);

    useEffect(() => {
        console.log(personLikes);
    }, [personLikes]);

    return (
        <div>유저가 좋아요한 {type} 페이지 - 준비중</div>
    );
}

export default UserLikePage;