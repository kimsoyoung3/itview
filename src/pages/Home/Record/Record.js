import React, { useEffect, useState } from "react";
import { getGenresByContentType } from "../../../API/HomeApi";
import "./Record.css"

const Record = () => {

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
                setGenre(await getGenresByContentType('record').then(res => res.data));
            } catch (error) {
                console.error('Error fetching home contents:', error);
            }
        };
        fetchDomain();
    }, []);

    return (
        <div className="record-page container">
            <div className="record-page-wrap">
                <div className="record-page-tab-btn">
                    {genre?.map(item =>(
                        <button key={item.id}>{item.second}</button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Record;