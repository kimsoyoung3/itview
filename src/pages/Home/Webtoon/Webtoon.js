import React, { useEffect, useState } from "react";
import { getChannelsByContentType, getGenresByContentType } from "../../../API/HomeApi";

const Webtoon = () => {

    const [genre, setGenre] = useState(null);
    useEffect(() => {
        console.log(genre);
    }, [genre]);

    const [channel, setChannel] = useState(null);
    useEffect(() => {
        console.log(channel);
    }, [channel]);

    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    useEffect(() => {
        const fetchDomain = async () => {
            try {
                setGenre(await getGenresByContentType('webtoon').then(res => res.data));
                setChannel(await getChannelsByContentType('webtoon').then(res => res.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchDomain();
    }, []);

    return (
        <div>
            Series
        </div>
    );
}

export default Webtoon;