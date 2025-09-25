import React, { useEffect, useState } from "react";
import { getChannelsByContentType, getContentsByChannel, getContentsByGenre, getGenresByContentType } from "../../../API/HomeApi";
import "./Webtoon.css"
import { toast } from "react-toastify";
import {NavLink} from "react-router-dom";

const Webtoon = () => {

    const [genre, setGenre] = useState(null);
    useEffect(() => {
        console.log(genre);
        setSelect(genre?.[0]);
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

    const [select, setSelect] = useState(null);
    useEffect(() => {
        if (!select) return;
        console.log(select);
        if (channel?.includes(select)) {
            const fetchContents = async () => {
                try {
                    setContents(await getContentsByChannel('webtoon', select?.first).then(res => res.data));
                } catch (error) {
                    toast("데이터를 불러오지 못했습니다.");
                }
            };
            fetchContents();
        } else {
            const fetchContents = async () => {
                try {
                    setContents(await getContentsByGenre('webtoon', select?.first).then(res => res.data));
                } catch (error) {
                    toast("데이터를 불러오지 못했습니다.");
                }
            };
            fetchContents();
        }
    }, [select, channel]);

    return (
        <div className="webtoon-page container">
            <div className="webtoon-page-wrap">
                <div className="webtoon-page-tab-btn-wrap">
                    <div className="webtoon-page-tab-btn">
                        {genre?.map(item =>(
                            <button className={select?.first === item.first ? "webtoon-page-tab-btn-click" : ""} onClick={() => setSelect(item)} key={item.id}>{item.second}</button>
                        ))}
                    </div>
                    <div className="webtoon-page-tab-btn">
                        {channel?.map(item =>(
                            <button onClick={() => setSelect(item)} key={item.id}>{item.second}</button>
                        ))}
                    </div>
                </div>

                <div className="webtoon-page-tab-content">
                    {contents?.content?.map(item => (
                        <NavLink to={`/content/${item?.id}`} className="webtoon-content-card">
                            <div className="webtoon-content-card-poster">
                                <img src={item.poster} alt=""/>
                            </div>
                            <p className="webtoon-content-card-title">{item.title}</p>
                            <p className="webtoon-content-card-info"><span>{item.nation}</span> &middot; <span>{(new Date(item.releaseDate).getFullYear())}</span></p>
                            <p className="webtoon-content-card-avg"><i className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}</p>
                        </NavLink>
                    ))}
                </div>

                <div className="webtoon-page-tab-more-btn">
                    <button>더보기</button>
                </div>
            </div>
        </div>
    );
}

export default Webtoon;