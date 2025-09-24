import React, { useEffect, useState } from "react";
import { getChannelsByContentType, getGenresByContentType } from "../../../API/HomeApi";
import "./Series.css"

const Series = () => {

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
                setGenre(await getGenresByContentType('series').then(res => res.data));
                setChannel(await getChannelsByContentType('series').then(res => res.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchDomain();
    }, []);

    return (
        <div className="series-page container">
            <div className="series-page-wrap">
                <div className="series-page-tab-btn-wrap">
                    <div className="series-page-tab-btn">
                        {genre?.map(item =>(
                            <button key={item.id}>{item.second}</button>
                        ))}
                    </div>
                    <div className="series-page-tab-btn">
                        {channel?.map(item =>(
                            <button key={item.id}>{item.second}</button>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Series;