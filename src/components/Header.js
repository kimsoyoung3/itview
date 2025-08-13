import React, {useState} from 'react';
import {Link, NavLink} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css'; // 스타일 불러오기

const Header = () => {
    /*로그인 모달*/
    const [isLoginOpen, setLoginOpen] = useState(false);
    /*회원가입 모달*/
    const [isSignupOpen, setSignupOpen] = useState(false);

    /*로그인 모달*/
    const openLogin = () => setLoginOpen(true);
    const closeLogin = () => setLoginOpen(false);

    /*회원가입 모달*/
    const openSignup = () => setSignupOpen(true);
    const closeSignup = () => setSignupOpen(false);

    // 모달 바깥 클릭 시 이벤트 전파 차단용
    const stopPropagation = (e) => e.stopPropagation();
    return (
        <>
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
                            <button onClick={openLogin} className="login-button">로그인</button>
                            <button onClick={openSignup} className="signUp-button">회원가입</button>
                        </div>
                    </div>
                </div>
            </header>

            {/*로그인 모달창*/}
            {isLoginOpen && (
                <div className="modal-overlay" onClick={closeLogin}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src="/logo.svg" alt=""/></h1>
                        <h2>로그인</h2>
                        <input type="email" placeholder="이메일" />
                        <input type="password" placeholder="비밀번호" />
                        <button className="login-submit">로그인</button>
                        <p className="login-text-top">비밀번호를 잊어버리셨나요?</p>
                        <p className="login-text-bottom">계정이 없으신가요?
                            <span onClick={() => {
                            closeLogin();
                            openSignup();}}>회원가입</span>
                        </p>
                    </div>
                </div>
            )}

            {/*회원가입 모달창*/}
            {isSignupOpen && (
                <div className="modal-overlay" onClick={closeSignup}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src="/logo.svg" alt=""/></h1>
                        <h2>회원가입</h2>
                        <input type="text" placeholder="닉네임" />
                        <input type="email" placeholder="이메일" />
                        <input type="password" placeholder="비밀번호" />
                        <button className="login-submit">회원가입</button>
                        <p  className="login-text-bottom">이미 가입하셨나요?
                            <span onClick={() => {
                                closeSignup();
                                openLogin();}}>로그인</span>
                        </p>
                    </div>
                </div>
            )}
        </>
    );
};

export default Header;
