import React, { useEffect, useState } from "react";
import { getContentsByGenre, getGenresByContentType } from "../../../API/HomeApi";
import "./Record.css"
import { toast } from "react-toastify";

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
                setContents(await getContentsByGenre('record', select?.first).then(res => res.data));
            } catch (error) {
                toast("데이터를 불러오지 못했습니다.");
            }
        };
        fetchContents();
    }, [select]);

    return (
        <div className="record-page container">
            <div className="record-page-wrap">
                <div className="record-page-tab-btn">
                    {genre?.map(item =>(
                        <button onClick={() => setSelect(item)} key={item.id}>{item.second}</button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Record;