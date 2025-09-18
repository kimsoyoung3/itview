import React, {useEffect, useState} from "react";
import "./SearchPage.css"
import {NavLink, useParams, useSearchParams} from "react-router-dom";
import CommentCard from "../../components/CommentCard/CommentCard";
import {searchContents, searchPersons, searchCollections, searchUsers} from "../../API/SearchApi";
import {getUserLikeCollection, getUserLikeComment, getUserLikePerson} from "../../API/UserApi";
import ContentEach from "../../components/ContentEach/ContentEach";
import SearchContentEach from "../../components/SearchContentEach/SearchContentEach";
import CreditOrPersonCard from "../../components/CreditOrPersonCard/CreditOrPersonCard";
import CollectionCard from "../../components/CollectionCard/CollectionCard";

const SearchPage = () => {
    const [activeTab, setActiveTab] = useState("search-page-tab-btn1")

    const [notFound, setNotFound] = useState(false);

    const [searchParams] = useSearchParams();
    const keyword =searchParams.get("keyword")

    const [contents, setContents] = useState([]);
    const [persons, setPersons] = useState([]);
    const [collections, setCollections] = useState([]);
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [contentsRes, personsRes, collectionsRes, usersRes] = await Promise.all([
                    searchContents(keyword),
                    searchPersons(keyword),
                    searchCollections(keyword),
                    searchUsers(keyword),
                ]);

                setContents(contentsRes.data);
                setPersons(personsRes.data);
                setCollections(collectionsRes.data);
                setUsers(usersRes.data);

            } catch (error) {
                setNotFound(true);
            }
        };

        if (keyword) {
            fetchData();
        }
    }, [keyword]);


    useEffect(() => {
        console.log("검색어:", keyword);
    }, [keyword]);

    useEffect(() => {
        console.log(users);
    }, [users]);


    return (
        <div className="search-page">
            <div className="search-result">
                <p className="container">"{keyword}" 검색결과</p>
            </div>

            <div className="search-page-wrap container">
                <div className="search-page-tab-title-wrap">
                    <div className={`search-page-tab-btn ${activeTab === "search-page-tab-btn1" ? "active" : ""}`}
                          onClick={() => setActiveTab('search-page-tab-btn1')}>
                        콘텐츠
                    </div>
                    <div className={`search-page-tab-btn ${activeTab === "search-page-tab-btn2" ? "active" : ""}`}
                         onClick={() => setActiveTab('search-page-tab-btn2')}>
                        인물
                    </div>
                    <div className={`search-page-tab-btn ${activeTab === "search-page-tab-btn3" ? "active" : ""}`}
                         onClick={() => setActiveTab('search-page-tab-btn3')}>
                        컬렉션
                    </div>
                    <div className={`search-page-tab-btn ${activeTab === "search-page-tab-btn4" ? "active" : ""}`}
                         onClick={() => setActiveTab('search-page-tab-btn4')}>
                        유저
                    </div>
                </div>

                <div className="search-page-tab-content-wrap">
                    {activeTab === "search-page-tab-btn1" && <div className="search-page-tab search-page-tab1">
                        {contents?.movie?.length > 0 && (
                            <div className="search-page-tab-content">
                                <div className="search-page-tab-content-title">
                                    <p>영화</p>
                                    <NavLink to={`/search/content?type=movie&keyword=${keyword}`}>더보기</NavLink>
                                </div>
                                <div className="search-page-tab-content-list">
                                    {contents?.movie?.map(item=>
                                        <div key={item.id}>
                                            <SearchContentEach searchData={item} />
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {contents?.series?.length > 0 && (
                            <div className="search-page-tab-content">
                                <div className="search-page-tab-content-title">
                                    <p>시리즈</p>
                                    <NavLink to={`/search/content?type=series&keyword=${keyword}`}>더보기</NavLink>
                                </div>
                                <div className="search-page-tab-content-list">
                                    {contents?.series?.map(item=>
                                        <div key={item.id}>
                                            <SearchContentEach searchData={item} />
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {contents?.book?.length > 0 && (
                            <div className="search-page-tab-content">
                                <div className="search-page-tab-content-title">
                                    <p>책</p>
                                    <NavLink to={`/search/content?type=book&keyword=${keyword}`}>더보기</NavLink>
                                </div>
                                <div className="search-page-tab-content-list">
                                    {contents?.book?.map(item=>
                                        <div key={item.id}>
                                            <SearchContentEach searchData={item} />
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {contents?.webtoon?.length > 0 && (
                            <div className="search-page-tab-content">
                                <div className="search-page-tab-content-title">
                                    <p>웹툰</p>
                                    <NavLink to={`/search/content?type=webtoon&keyword=${keyword}`}>더보기</NavLink>
                                </div>
                                <div className="search-page-tab-content-list">
                                    {contents?.webtoon?.map(item=>
                                        <div key={item.id}>
                                            <SearchContentEach searchData={item} />
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {contents?.record?.length > 0 && (
                            <div className="search-page-tab-content">
                                <div className="search-page-tab-content-title">
                                    <p>음반</p>
                                    <NavLink to={`/search/content?type=record&keyword=${keyword}`}>더보기</NavLink>
                                </div>
                                <div className="search-page-tab-content-list">
                                    {contents?.record?.map(item=>
                                        <div key={item.id}>
                                            <SearchContentEach searchData={item} />
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                    </div>}

                    {activeTab === "search-page-tab-btn2" && <div className="search-page-tab search-page-tab2">
                        {persons?.content?.length > 0 ? (
                            <div>
                                <div className="search-page-tab-person">
                                    {persons?.content?.map(item =>
                                        <div key={item.id}>
                                            <CreditOrPersonCard data={item} type="person"/>
                                        </div>
                                    )}

                                </div>

                                <div className="search-page-tab-more-btn"><button>더보기</button></div>
                            </div>
                        ) : (
                            <p className="empty-message">검색 결과가 없습니다 :)</p>
                        )}
                    </div>}

                    {activeTab === "search-page-tab-btn3" && <div className="search-page-tab search-page-tab3">
                        {collections?.content?.length > 0 ? (
                            <div>
                                <div className="search-page-tab-collection">
                                    {collections?.content?.map(item =>
                                        <div key={item.id}>
                                            <CollectionCard collectionData={item}/>
                                        </div>
                                    )}
                                </div>

                                <div className="search-page-tab-more-btn"><button>더보기</button></div>
                            </div>
                        ) : (
                            <p className="empty-message">검색 결과가 없습니다 :)</p>
                        )}
                    </div>}

                    {activeTab === "search-page-tab-btn4" && <div className="search-page-tab search-page-tab4">
                        {users?.content?.length > 0 ? (
                            <div>
                                <div className="search-page-tab-user">
                                    {users?.content?.map(item =>
                                        <NavLink to={`/user/${item?.userProfile?.id}`} className="search-page-tab-user-profile-wrap">
                                            <div className="search-page-tab-user-profile">
                                                <img src={item.userProfile.profile ? item.userProfile.profile : `${process.env.PUBLIC_URL}/user.png`} alt=""/>
                                            </div>
                                            <div className="search-page-tab-user-info">
                                                <p>{item.userProfile.nickname}</p>
                                                <p>
                                                    {item.userProfile.introduction ? (
                                                        item.userProfile.introduction
                                                    ) : (
                                                        <>
                                                            {item.ratingCount > 0 && <>평가 {item.ratingCount} </>}
                                                            {item.commentCount > 0 && <>&middot; 댓글 {item.commentCount} </>}
                                                            {item.collectionCount > 0 && <>&middot; 컬렉션 {item.collectionCount}</>}
                                                        </>
                                                    )}
                                                </p>
                                            </div>

                                        </NavLink>
                                    )}
                                </div>

                                <div className="search-page-tab-more-btn"><button>더보기</button></div>
                            </div>
                        ) : (
                            <p className="empty-message">검색 결과가 없습니다 :)</p>
                        )}
                    </div>}

                </div>
            </div>


        </div>
    );

};
export default SearchPage;