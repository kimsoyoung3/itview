import React, { useEffect, useState } from "react";
import { getContentsByGenre, getGenresByContentType } from "../../../API/HomeApi";
import "./Record.css"
import { toast } from "react-toastify";
import {NavLink} from "react-router-dom";

const Record = () => {

    const [genre, setGenre] = useState(null);
    useEffect(() => {
        console.log(genre);
        setSelect(genre?.[0]);
    }, [genre]);

    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    useEffect(() => {
        const fetchDomain = async () => {
            try {
                setGenre(await getGenresByContentType('record').then(res => res.data));
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
        const fetchContents = async () => {
            try {
                setContents(await getContentsByGenre('record', select?.first, 1).then(res => res.data));
            } catch (error) {
                toast("데이터를 불러오지 못했습니다.");
            }
        };
        fetchContents();
    }, [select]);

    const handleMoreClick = async() => {
        try {
            const res = await getContentsByGenre('record', select?.first, contents?.page.number + 2)
            setContents({
                content: [...contents.content, ...res.data.content],
                page: res.data.page
            });
        } catch (error) {
            toast("데이터를 불러오지 못했습니다.");
        }
    };

    return (
        <div className="record-page">
            <div className="record-page-wrap container">
                <div className="record-page-tab-btn">
                    {genre?.map(item =>(
                        <button className={select?.first === item.first ? "record-page-tab-btn-click" : ""} onClick={() => setSelect(item)} key={item.id}>{item.second}</button>
                    ))}
                </div>

                <div className="record-page-tab-content">
                    {contents?.content?.map(item => (
                        <NavLink to={`/content/${item?.id}`} className="record-content-card">
                            <div className="record-content-card-poster">
                                <img src={item.poster} alt=""/>
                            </div>
                            <p className="record-content-card-title">{item.title}</p>
                            <p className="record-content-card-info"><span>{item.nation}</span> &middot; <span>{(new Date(item.releaseDate).getFullYear())}</span></p>
                            <p className="record-content-card-avg"><i className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}</p>
                        </NavLink>
                    ))}
                </div>

                <div className="record-page-tab-more-btn">
                    <button onClick={handleMoreClick} hidden={contents?.page.number + 1 >= contents?.page.totalPages}>더보기</button>
                </div>
            </div>
        </div>
    );
}

export default Record;