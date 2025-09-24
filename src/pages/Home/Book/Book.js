import React, { useEffect, useState } from "react";
import { getGenresByContentType } from "../../../API/HomeApi";
import "./Book.css"

const Book = () => {

    const [genre, setGenre] = useState(null);
    useEffect(() => {
        console.log(genre);
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

    return (
        <div className="book-page container">
            <div className="book-page-wrap">
                <div className="book-page-tab-btn">
                    {genre?.map(item =>(
                        <button key={item.id}>{item.second}</button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Book;