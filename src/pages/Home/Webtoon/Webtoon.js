import React, { useEffect, useState } from "react";
import { getChannelsByContentType, getContentsByChannel, getContentsByGenre, getGenresByContentType } from "../../../API/HomeApi";
import "./Webtoon.css"
import { toast } from "react-toastify";
import {NavLink} from "react-router-dom";

const Webtoon = () => {
    /*장르 상태*/
    const [genre, setGenre] = useState(null);

    /* 장르 변경 시 초기 선택값 세팅 */
    useEffect(() => {
        console.log(genre);
        setSelect(genre?.[0]);
    }, [genre]);

    /* 채널 상태 */
    const [channel, setChannel] = useState(null);
    useEffect(() => {
        console.log(channel);
    }, [channel]);

    /* 컨텐츠 상태 */
    const [contents, setContents] = useState(null);
    useEffect(() => {
        console.log(contents);
    }, [contents]);

    /* 장르 불러오기 */
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

    /* 선택된 장르 */
    const [select, setSelect] = useState(null);

    /* 장르 선택 시 컨텐츠 불러오기 */
    useEffect(() => {
        if (!select) return;
        console.log(select);
        if (channel?.includes(select)) {
            const fetchContents = async () => {
                try {
                    setContents(await getContentsByChannel('webtoon', select?.first, 1).then(res => res.data));
                } catch (error) {
                    toast("데이터를 불러오지 못했습니다.");
                }
            };
            fetchContents();
        } else {
            const fetchContents = async () => {
                try {
                    setContents(await getContentsByGenre('webtoon', select?.first, 1).then(res => res.data));
                } catch (error) {
                    toast("데이터를 불러오지 못했습니다.");
                }
            };
            fetchContents();
        }
    }, [select, channel]);

    /* 더보기 버튼 클릭 */
    const handleMoreClick = async() => {
        if (channel?.includes(select)) {
            try {
                const res = await getContentsByChannel('webtoon', select?.first, contents?.page.number + 2)
                setContents({
                    content: [...contents.content, ...res.data.content],
                    page: res.data.page
                });
            } catch (error) {
                toast("데이터를 불러오지 못했습니다.");
            }
        } else {
            try {
                const res = await getContentsByGenre('webtoon', select?.first, contents?.page.number + 2)
                setContents({
                    content: [...contents.content, ...res.data.content],
                    page: res.data.page
                });
            } catch (error) {
                toast("데이터를 불러오지 못했습니다.");
            }
        }
    };
    
    return (
        <div className="webtoon-page">
            <div className="webtoon-page-wrap container">
                <div className="webtoon-page-tab-btn-wrap">

                    {/*탭 버튼*/}
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

                {/*탭 내용*/}
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

                {/*탭 더보기 버튼*/}
                <div className="webtoon-page-tab-more-btn">
                    <button onClick={handleMoreClick} hidden={contents?.page.number + 1 >= contents?.page.totalPages}>더보기</button>
                </div>
            </div>
        </div>
    );
}

export default Webtoon;