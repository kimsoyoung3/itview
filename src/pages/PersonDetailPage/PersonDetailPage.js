import React, {useEffect, useState} from "react";
import "./PersonDetailPage.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import { getPersonInfo, getPersonWorkDomains, getPersonWorks, likePerson, unlikePerson } from "../../API/PersonApi";
import {NavLink, useParams} from "react-router-dom";
import NotFound from "../NotFound/NotFound";


const PersonDetailPage = ({userInfo, openLogin}) => {
    const [notFound, setNotFound] = useState(false);

    const [personInfo, setPersonInfo] = useState(null);
    const [workInfo, setWorkInfo] = useState(null);

    const domainNameMap = {
        "영화" : "MOVIE",
        "시리즈" : "SERIES",
        "책" : "BOOK",
        "웹툰" : "WEBTOON",
        "음반" : "RECORD"
    }

    const { id } = useParams();
    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await getPersonInfo(id);
                if (res) {
                    setPersonInfo(res.data);
                } else {
                    setPersonInfo(null);
                }
    
                const res2 = await getPersonWorkDomains(id);
    
                var workList = {};
                for (const domain of res2.data) {
                    workList[domain.contentType] = workList[domain.contentType] || {};
                    workList[domain.contentType][domain.department] = workList[domain.contentType][domain.department] || [];
                }
    
                for (const domain in workList) {
                    for (const department in workList[domain]) {
                        const res3 = await getPersonWorks(id, domainNameMap[domain], department, 1);
                        workList[domain][department] = res3.data;
                    }
                }
                setWorkInfo(workList);
            } catch (error) {
                setNotFound(true);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(personInfo);
    }, [personInfo]);

    useEffect(() => {
        console.log(workInfo);
    }, [workInfo]);

    const handleMoreClick = async (contentType, department, page) => {
        console.log(domainNameMap[contentType], department, page);
        const res = await getPersonWorks(id, domainNameMap[contentType], department, page);
        if (res) {
            setWorkInfo({
                ...workInfo,
                [contentType]: {
                    ...workInfo[contentType],
                    [department]: {
                        ...workInfo[contentType][department],
                        content: [...workInfo[contentType][department].content, ...res.data.content],
                        page: res.data.page
                    }
                }
            });
        }
    }

    const handleLikeClick = async () => {
        if (personInfo.liked) {
            const res = await unlikePerson(id);
            if (res) {
                setPersonInfo({
                    ...personInfo,
                    liked: false,
                    likeCount: personInfo.likeCount - 1
                });
            }
        } else {
            const res = await likePerson(id);
            if (res) {
                setPersonInfo({
                    ...personInfo,
                    liked: true,
                    likeCount: personInfo.likeCount + 1
                });
            }
        }
    }

    const headerHeight = 94; // 헤더 높이(px)

    const handleTabClick = (contentType, department = null) => {
        const idToScroll = department ? `${contentType}-${department}` : contentType;
        const element = document.getElementById(idToScroll);
        if (element) {
            const top = element.getBoundingClientRect().top + window.pageYOffset - headerHeight;
            window.scrollTo({ top, behavior: 'smooth' });
            setActiveTab(department ? `${contentType}-${department}` : contentType);
        }
    };

    const [activeTab, setActiveTab] = useState(null);

    /*외부서비스 로고*/
    const serviceLogos = {
        NETFLIX: '/externalLogo/netflix.png',
        DISNEY_PLUS: '/externalLogo/disney.png',
        WAVVE: '/externalLogo/wavve.png',
        WATCHA: '/externalLogo/watcha.png',
        TVING: '/externalLogo/tving.png',
        TVN: '/externalLogo/tvn.png',
        APPLE_TV_PLUS: '/externalLogo/apple-tv-plus.png',
        COUPANG_PLAY: '/externalLogo/coupang-play.png',

        ALADIN: '/externalLogo/aladin.png',
        YES24: '/externalLogo/yes24.png',
        KYOBO: '/externalLogo/kyobo.png',

        NAVER: '/externalLogo/naverWebtoon.png',
        KAKAO: '/externalLogo/kakaoWebtoon.png',
    };

    return(notFound ? <NotFound /> :
        <div className="person-detail-page container">
            {/*인물 프로필 섹션*/}
             <section className="person-detail-page-profile">
                <div className="person-detail-page-profile-img">
                    <img src={personInfo?.profile ? personInfo?.profile : "/user.png" } alt=""/>
                </div>
                <div className="person-detail-page-profile-info">
                    <h4>{personInfo?.name}</h4>
                    <p>{personInfo?.job}</p>
                </div>
                 <div className="person-detail-page-profile-like">
                     <div><button onClick={() => userInfo ? handleLikeClick() : openLogin()}><i className={personInfo?.liked ? "bi bi-heart-fill" : "bi bi-heart"}/> 좋아요 {personInfo?.likeCount}명이 이 인물을 좋아합니다.</button></div>
                 </div>
            </section>

            {/*컨텐츠 섹션*/}
            <section className="person-detail-page-content">

                <div>
                    {workInfo && Object.entries(workInfo).map(([contentType, departments]) => (
                        <div key={contentType} id={contentType} className="person-detail-page-content-domain">
                            <h5>{contentType}</h5>
                            <div className="person-detail-page-content-domain-wrap">
                                {Object.entries(departments).map(([department, items]) => (
                                    <div key={department}>
                                        <p id={`${contentType}-${department}`}>{department}</p>
                                        <div className="person-detail-page-content-domain-title">
                                            <div></div>
                                            <div></div>
                                            <div>제목</div>
                                            <div>역할</div>
                                            <div>평가</div>
                                            <div>감상서비스</div>
                                        </div>
                                        {items.content.map(item =>
                                            <NavLink key={item.id} to={`/content/${item.id}`} className="person-detail-page-content-domain-list">
                                                <div className="domain-list-date">{(new Date(item.releaseDate).getFullYear())}</div>
                                                <div className="domain-list-image"><img src={item.poster} alt=""/></div>
                                                <div className="domain-list-title">{item.title}</div>
                                                <div className="domain-list-role">{item.role}</div>

                                                <div className="domain-list-ratingAvg">
                                                    {item.ratingAvg && (
                                                        <div>평균 <i className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}</div>
                                                    )}
                                                </div>


                                                <div className="domain-list-externalService">
                                                    {item.externalServices?.map(service => (
                                                        <div key={service.id} onClick={(e) => {e.preventDefault(); window.open(service.href)}} target="_blank" rel="noopener noreferrer">
                                                            <img src={serviceLogos[service.type] || '/images/default-logo.png'} alt={service.type}/>
                                                        </div>
                                                    ))}
                                                </div>

                                            </NavLink>
                                        )}
                                        <div className="person-detail-page-content-domain-btn">{workInfo[contentType][department].page.number+1 < workInfo[contentType][department].page.totalPages ? <button onClick={() => handleMoreClick(contentType, department, workInfo[contentType][department].page.number+2)}>더보기</button> : null} </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>

                {/*탭영역 섹션*/}
                <div className="person-detail-page-content-tab">
                    <ul>
                        {workInfo && Object.entries(workInfo).map(([contentType, departments]) => (
                            <li key={contentType}>
                                <div>
                                    <button
                                        className={activeTab === contentType ? 'active' : ''}
                                        onClick={() => handleTabClick(contentType)}
                                    >
                                        {contentType}
                                    </button>
                                </div>
                                <ul>
                                    {Object.keys(departments).map((department) => (
                                        <li key={department}>
                                            <button
                                                className={activeTab === `${contentType}-${department}` ? 'active' : ''}
                                                onClick={() => handleTabClick(contentType, department)}
                                            >
                                                {department}
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </li>
                        ))}
                    </ul>
                </div>
            </section>
        </div>
    )


};
export default PersonDetailPage;