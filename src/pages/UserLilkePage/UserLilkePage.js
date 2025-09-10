import { React, useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { getUserLikeComment, getUserLikePerson } from '../../API/UserApi';

function UserLikePage() {
    const [notFound, setNotFound] = useState(false);

    const [searchParams] = new useSearchParams();
    const type = searchParams.get("type") || "person";

    const { id } = useParams();
    const [personLikes, setPersonLikes] = useState({});
    const [collectionLikes, setCollectionLikes] = useState({});
    const [commentLikes, setCommentLikes] = useState({});

    useEffect(() => {
        try {
            const fetchData = async () => {
                const personRes = await getUserLikePerson(id, 1);
                setPersonLikes(personRes.data);
                // const collectionRes = await getUserLikeCollection(id, 1);
                // setCollectionLikes(collectionRes.data);
                const commentRes = await getUserLikeComment(id, 1);
                setCommentLikes(commentRes.data);
            }
            fetchData();
        } catch (error) {
            setNotFound(true);
        }
    }, [id]);

    useEffect(() => {
        console.log(personLikes);
    }, [personLikes]);

    useEffect(() => {
        console.log(commentLikes);
    }, [commentLikes]);

    return (
        <div>유저가 좋아요한 {type} 페이지 - 준비중</div>
    );
}

export default UserLikePage;