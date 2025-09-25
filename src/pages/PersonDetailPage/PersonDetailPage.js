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
            const windowWidth = document.documentElement.clientWidth;
            // 태블릿 이하에서는 헤더가 사라지니까 20px만 주자
            const offset = windowWidth < 769 ? 20 : headerHeight;

            const top =
                element.getBoundingClientRect().top + window.pageYOffset - offset;

            window.scrollTo({ top, behavior: "smooth" });
            setActiveTab(department ? `${contentType}-${department}` : contentType);
        }
    };

    const handleScrollToContentType = (contentType) => {
        if (window.innerWidth > 768) return; // 모바일에서만 실행

        const element = document.getElementById(contentType);
        if (element) {
            const offset = 20; // 위 여백 만큼 빼기
            const top = element.getBoundingClientRect().top + window.pageYOffset - offset;
            window.scrollTo({ top, behavior: 'smooth' });
        }
    };



    const [activeTab, setActiveTab] = useState(null);

    /*외부서비스 로고*/
    const serviceLogos = {
        NETFLIX: `${process.env.PUBLIC_URL}/externalLogo/netflix.png`,
        DISNEY_PLUS: `${process.env.PUBLIC_URL}/externalLogo/disney.png`,
        WAVVE: `${process.env.PUBLIC_URL}/externalLogo/wavve.png`,
        WATCHA: `${process.env.PUBLIC_URL}/externalLogo/watcha.png`,
        TVING: `${process.env.PUBLIC_URL}/externalLogo/tving.png`,
        TVN: `${process.env.PUBLIC_URL}/externalLogo/tvn.png`,
        APPLE_TV_PLUS: `${process.env.PUBLIC_URL}/externalLogo/apple-tv-plus.png`,
        COUPANG_PLAY: `${process.env.PUBLIC_URL}/externalLogo/coupang-play.png`,

        ALADIN: `${process.env.PUBLIC_URL}/externalLogo/aladin.png`,
        YES24: `${process.env.PUBLIC_URL}/externalLogo/yes24.png`,
        KYOBO: `${process.env.PUBLIC_URL}/externalLogo/kyobo.png`,

        NAVER: `${process.env.PUBLIC_URL}/externalLogo/naverWebtoon.png`,
        KAKAO: `${process.env.PUBLIC_URL}/externalLogo/kakaoWebtoon.png`,
    };

    return (notFound ? <NotFound/> :
            <div className="person-detail-page">
                <div className="person-detail-page-wrap container">
                    {/*인물 프로필 섹션*/}
                    <section className="person-detail-page-profile">
                        <div className="person-detail-page-profile-img">
                            <img src={personInfo?.profile ? personInfo?.profile : `${process.env.PUBLIC_URL}/user.png`}
                                 alt=""/>
                        </div>
                        <div className="person-detail-page-profile-info">
                            <h4>{personInfo?.name}</h4>
                            <p>{personInfo?.job}</p>
                        </div>
                        <div className="person-detail-page-profile-like">
                            <div>
                                <button onClick={() => userInfo ? handleLikeClick() : openLogin()}><i
                                    className={personInfo?.liked ? "bi bi-heart-fill" : "bi bi-heart"}/> 좋아요 {personInfo?.likeCount}명이
                                    이 인물을 좋아합니다.
                                </button>
                            </div>
                        </div>
                    </section>

                    {/*컨텐츠 섹션*/}
                    <section className="person-detail-page-content">

                        <div>
                            {/*모바일 버전 스크롤 버튼*/}
                            <div className="mobile-content-type-btn">
                                <div>
                                    {workInfo && Object.entries(workInfo).map(([contentType, departments]) =>(
                                        <h5 key={contentType}
                                               onClick={() => handleScrollToContentType(contentType)}>{contentType}</h5>
                                    ))}
                                </div>
                            </div>

                            <div className="top-btn" onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}>
                                <i className="bi bi-chevron-up"></i>
                            </div>

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

                                                {/*pc 버전 컨텐츠*/}
                                                {items.content.map(item =>
                                                    <NavLink key={item.id} to={`/content/${item.id}`}
                                                             className="person-detail-page-content-domain-list">
                                                        <div
                                                            className="domain-list-date">{(new Date(item.releaseDate).getFullYear())}</div>
                                                        <div className="domain-list-image"><img src={item.poster}
                                                                                                alt=""/></div>
                                                        <div className="domain-list-title">{item.title}</div>
                                                        <div className="domain-list-role">{item.role}</div>

                                                        <div className="domain-list-ratingAvg">
                                                            {item.ratingAvg && (
                                                                <div>평균 <i
                                                                    className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}
                                                                </div>
                                                            )}
                                                        </div>


                                                        <div className="domain-list-externalService">
                                                            {item.externalServices?.map(service => (
                                                                <div key={service.id} onClick={(e) => {
                                                                    e.preventDefault();
                                                                    window.open(service.href)
                                                                }} rel="noopener noreferrer">
                                                                    <img
                                                                        src={serviceLogos[service.type] || `${process.env.PUBLIC_URL}/images/default-logo.png`}
                                                                        alt={service.type}/>
                                                                </div>
                                                            ))}
                                                        </div>

                                                    </NavLink>
                                                )}

                                                {/*모바일 버전 컨텐츠*/}
                                                {items.content.map(item =>
                                                    <NavLink key={item.id} to={`/content/${item.id}`}
                                                             className="person-detail-page-content-domain-list-mobile">
                                                        <div
                                                            className="person-detail-page-content-domain-list-mobile-wrap">
                                                            <div className="domain-list-image"><img src={item.poster}
                                                                                                    alt=""/></div>
                                                            <div className="domain-list-info">
                                                                <div className="domain-list-title">{item.title}</div>
                                                                <div className="domain-list-info-flex">
                                                                    <div className="domain-list-role">{item.role}</div>
                                                                    <div
                                                                        className="domain-list-date">{(new Date(item.releaseDate).getFullYear())}</div>
                                                                </div>
                                                                <div className="domain-list-ratingAvg">
                                                                    {item.ratingAvg && (
                                                                        <div>평균 <i
                                                                            className="bi bi-star-fill"/> {(item.ratingAvg / 2).toFixed(1)}
                                                                        </div>
                                                                    )}
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </NavLink>
                                                )}
                                                <div
                                                    className="person-detail-page-content-domain-btn">{workInfo[contentType][department].page.number + 1 < workInfo[contentType][department].page.totalPages ?
                                                    <button
                                                        onClick={() => handleMoreClick(contentType, department, workInfo[contentType][department].page.number + 2)}>더보기</button> : null} </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>

                        {/*pc 탭영역 섹션*/}
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
            </div>
    );


};
export default PersonDetailPage;