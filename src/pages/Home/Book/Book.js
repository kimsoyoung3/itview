import React, { useEffect, useState } from "react";
import { getContentsByGenre, getGenresByContentType } from "../../../API/HomeApi";
import "./Book.css"
import { toast } from "react-toastify";
import {NavLink} from "react-router-dom";

const Book = () => {

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
                setGenre(await getGenresByContentType('book').then(res => res.data));
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
                setContents(await getContentsByGenre('book', select?.first).then(res => res.data));
            } catch (error) {
                toast("데이터를 불러오지 못했습니다.");
            }
        };
        fetchContents();
    }, [select]);

    return (
        <div className="book-page container">
            <div className="book-page-wrap">
                <div className="book-page-tab-btn">
                    {genre?.map(item =>(
                        <button onClick={() => setSelect(item)} key={item.id}>{item.second}</button>
                    ))}
                </div>

                <div className="book-page-tab-content">
                    {contents?.content?.map(item => (
                        <NavLink to={`/content/${item?.id}`} className="book-content-card">
                            <div className="book-content-card-poster">
                                <img src={item.poster} alt=""/>
                            </div>
                            <p className="book-content-card-title">{item.title}</p>
                            <p className="book-content-card-info"><span>{item.nation}</span> &middot; <span>{(new Date(item.releaseDate).getFullYear())}</span></p>
                            <p className="book-content-card-avg"><i className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}</p>
                        </NavLink>
                    ))}
                </div>

                <div className="book-page-tab-more-btn">
                    <button>더보기</button>
                </div>
            </div>
        </div>
    );
}

export default Book;