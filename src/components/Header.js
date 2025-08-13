import React from 'react';
import {NavLink} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css'; // 스타일 불러오기

const Header = () => {
    return (
        <header className="header">

            {/*헤더 넓이 지정 부분*/}
            <div className="header_width container">

                {/*메인 로고*/}
                <div className="header_inner header_left">
                    <NavLink to="/" className="logo">
                        <img src="/logo.svg"/>
                    </NavLink>

                    {/*목록 부분*/}
                    <ul className="nav">
                        <li><NavLink to="/">홈</NavLink></li>
                        <li><NavLink to="/movies">영화</NavLink></li>
                        <li><NavLink to="/series">시리즈</NavLink></li>
                        <li><NavLink to="/books">책</NavLink></li>
                        <li><NavLink to="/webtoons">웹툰</NavLink></li>
                        <li><NavLink to="/music">음악</NavLink></li>
                    </ul>

                    <div className="nav-item dropdown">
                        <NavLink className="nav-link dropdown-toggle" to="/" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            홈
                        </NavLink>
                        <ul className="dropdown-menu">
                            <li><NavLink to="/movies">영화</NavLink></li>
                            <li><NavLink to="/series">시리즈</NavLink></li>
                            <li><NavLink to="/books">책</NavLink></li>
                            <li><NavLink to="/webtoons">웹툰</NavLink></li>
                            <li><NavLink to="/music">음악</NavLink></li>
                        </ul>
                    </div>
                </div>

                <div className="header_inner">

                    {/*검색바*/}
                    <div className="search-bar">
                        <button className="search-button"><i className="bi bi-search"/></button>
                        <input
                            type="text"
                            placeholder="검색어를 입력해주세요"
                            className="search-input"
                        />
                    </div>

                    {/*로그인&회원가입&마이페이지*/}
                    <div className="user-menu">
                        <NavLink to="/login">로그인</NavLink>
                        <NavLink className="signUp" to="/signUp">회원가입</NavLink>
                    </div>
                </div>
            </div>

        </header>
    );
};

export default Header;
