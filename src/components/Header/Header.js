import React, { useEffect, useState } from 'react';
import { Link, NavLink, useLocation } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Header.css';
import {
    checkEmail,
    checkVerification,
    createVerification,
    link,
    registerUser,
    setPassword
} from '../../API/UserApi';
import { toast } from "react-toastify";

const Header = ({ userInfo, handleLogin, handleLogout, isLoginOpen, openLogin, closeLogin }) => {
    /* 현재 경로 및 스크롤 상태 */
    const location = useLocation();
    const [scrolled, setScrolled] = useState(false);
    const isDetailPage = location.pathname.match(/^\/content\/\d+/);

    useEffect(() => {
        const handleScroll = () => setScrolled(window.scrollY > 50);

        if (isDetailPage) {
            window.addEventListener('scroll', handleScroll);

            /* 상세페이지 내 댓글 페이지는 항상 scrolled */
            if (location.pathname.includes('/comment')) setScrolled(true);

            return () => window.removeEventListener('scroll', handleScroll);
        } else {
            setScrolled(true);
        }
    }, [isDetailPage, location.pathname]);

    /* 로그인 상태 및 에러 처리 */
    const [loginEmail, setLoginEmail] = useState('');
    const [loginPassword, setLoginPassword] = useState('');
    const [loginError, setLoginError] = useState(null);
    const [isErrorModalOpen, setErrorModalOpen] = useState(false);
    const closeErrorModal = () => setErrorModalOpen(false);

    /* 비밀번호 재설정 관련 */
    const [verifyingEmail, setVerifyingEmail] = useState('');
    const [isSignupOpen, setSignupOpen] = useState(false);
    const [signupNickname, setSignupNickname] = useState('');
    const [signupEmail, setSignupEmail] = useState('');
    const [signupPassword, setSignupPassword] = useState('');

    const [isResetOpen, setResetOpen] = useState(false);
    const [isResetCheckOpen, setResetCheckOpen] = useState(false);
    const [isResetNewOpen, setResetNewOpen] = useState(false);

    /* 비밀번호 찾기 로직 */
    const handleCreateVerification = async (email) => {
        try {
            await checkEmail({ email });
            setVerifyingEmail(email);
            toast('인증 번호를 전송했습니다. 메일을 확인해주세요.');
            createVerification({ email });
            closeReset();
            openResetCheck();
        } catch {
            toast('인증 이메일 전송에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleCheckVerification = async (email, code) => {
        try {
            await checkVerification({ email, code });
            toast('인증이 완료되었습니다. 비밀번호를 재설정해주세요.');
            closeResetCheck();
            openResetNew();
        } catch {
            toast('인증 코드가 잘못되었습니다. 다시 시도해주세요.');
        }
    };

    const handleSetPassword = async (newPassword) => {
        try {
            await setPassword({ email: verifyingEmail, newPassword });
            toast('비밀번호가 성공적으로 변경되었습니다.');
            closeResetNew();
            openLogin();
        } catch {
            toast('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
        }
    };

    /* 회원가입 로직 */
    const handleSignup = async () => {
        try {
            await registerUser({
                nickname: signupNickname,
                email: signupEmail,
                password: signupPassword
            });
            toast('회원가입이 완료되었습니다.');
            closeSignup();
        } catch {
            toast('이미 존재하는 이메일입니다. 다시 시도해주세요.');
        }
    };

    /* 모달 열기/닫기 함수 */
    const openSignup = () => setSignupOpen(true);
    const closeSignup = () => setSignupOpen(false);

    const openReset = () => setResetOpen(true);
    const closeReset = () => setResetOpen(false);

    const openResetCheck = () => setResetCheckOpen(true);
    const closeResetCheck = () => setResetCheckOpen(false);

    const openResetNew = () => setResetNewOpen(true);
    const closeResetNew = () => setResetNewOpen(false);

    return (
        <>
            {/* 헤더 */}
            <header className={`header ${scrolled ? 'header-scrolled' : 'header-transparent'}`}>
                <div className="header_width container">

                    {/* 좌측: 로고 및 메뉴 */}
                    <div className="header_inner header_left">
                        <NavLink to="/" className="logo">
                            <img src="/itview-logo/mainLogo.svg" alt="로고"/>
                        </NavLink>

                        {/* 메뉴 */}
                        <ul className="nav">
                            <li><NavLink to="/">홈</NavLink></li>
                            <li><NavLink to="/movies">영화</NavLink></li>
                            <li><NavLink to="/series">시리즈</NavLink></li>
                            <li><NavLink to="/books">책</NavLink></li>
                            <li><NavLink to="/webtoons">웹툰</NavLink></li>
                            <li><NavLink to="/music">음악</NavLink></li>
                        </ul>

                        {/* 모바일/드롭다운 메뉴 */}
                        <div className="nav-item dropdown">
                            <NavLink to="/" className="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown"
                                     aria-expanded="false">홈</NavLink>
                            <ul className="dropdown-menu">
                                <li><NavLink to="/movies">영화</NavLink></li>
                                <li><NavLink to="/series">시리즈</NavLink></li>
                                <li><NavLink to="/books">책</NavLink></li>
                                <li><NavLink to="/webtoons">웹툰</NavLink></li>
                                <li><NavLink to="/music">음악</NavLink></li>
                            </ul>
                        </div>
                    </div>

                    {/* 우측: 검색 및 로그인/회원가입 */}
                    <div className="header_inner">

                        {/* 검색바 */}
                        <div className="search-bar">
                            <form action="">
                                <button className="search-button"><i className="bi bi-search"/></button>
                                <input type="text" placeholder="검색어를 입력해주세요" className="search-input"/>
                            </form>
                        </div>

                        {/* 로그인/회원정보 */}
                        {userInfo ? (
                            <div className="user-menu">
                                <Link to={`/user/${userInfo}`} className="login-button">마이페이지</Link>
                                <button onClick={handleLogout} className="login-button">로그아웃</button>
                            </div>
                        ) : (
                            <div className="user-menu">
                                <button onClick={openLogin} className="login-button">로그인</button>
                                <button onClick={openSignup} className="signUp-button">회원가입</button>
                            </div>
                        )}
                    </div>
                </div>
            </header>

            {/* 로그인 모달 */}
            {isLoginOpen && (
                <div className="modal-overlay" onClick={closeLogin}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src="/itview-logo/mainLogo.svg" alt="로고"/></h1>
                        <h2>로그인</h2>
                        <form onSubmit={async (e) => {
                            e.preventDefault();
                            const res = await handleLogin(loginEmail, loginPassword);
                            if (res) {
                                closeLogin();
                                window.location.reload();
                            } else {
                                setLoginError('로그인에 실패했습니다. 다시 시도해주세요.');
                                setErrorModalOpen(true);
                            }
                        }}>
                            <input type="email" placeholder="이메일" onChange={(e) => setLoginEmail(e.target.value)}/>
                            <input type="password" placeholder="비밀번호" onChange={(e) => setLoginPassword(e.target.value)}/>
                            <button type="submit" className="login-submit">로그인</button>
                        </form>
                        <p onClick={openReset} className="login-text-top">비밀번호를 잊으셨나요?</p>
                        <p className="login-text-bottom">
                            계정이 없으신가요?
                            <span onClick={() => {closeLogin(); openSignup();}}>회원가입</span>
                        </p>
                        {/* 소셜 로그인 */}
                        <div className="login-social">
                            <div className="login-social-border">OR</div>
                            <div className="login-social-logo">
                                <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/kakao`}
                                   onClick={() => link({redirectURL: window.location.href})}><img src="/LoginLogo/kakao-logo.svg" alt="카카오"/></a>
                                <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/naver`}
                                   onClick={() => link({redirectURL: window.location.href})}><img src="/LoginLogo/naver-logo.svg" alt="네이버"/></a>
                                <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/google`}
                                   onClick={() => link({redirectURL: window.location.href})}><img src="/LoginLogo/google-logo.svg" alt="구글"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* 회원가입 모달 */}
            {isSignupOpen && (
                <div className="modal-overlay" onClick={closeSignup}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src="/itview-logo/mainLogo.svg" alt="로고"/></h1>
                        <h2>회원가입</h2>
                        <form onSubmit={(e) => { e.preventDefault(); handleSignup(); }}>
                            <input type="text" placeholder="닉네임" onChange={(e) => setSignupNickname(e.target.value)}/>
                            <input type="email" placeholder="이메일" onChange={(e) => setSignupEmail(e.target.value)}/>
                            <input type="password" placeholder="비밀번호" onChange={(e) => setSignupPassword(e.target.value)}/>
                            <button type="submit" className="login-submit">회원가입</button>
                        </form>
                        <p className="login-text-bottom">
                            이미 가입하셨나요?
                            <span onClick={() => {closeSignup();openLogin();}}>로그인</span>
                        </p>
                        {/* 소셜 로그인 */}
                        <div className="login-social">
                            <div className="login-social-border">OR</div>
                            <div className="login-social-logo">
                                <a href="http://localhost:8080/oauth2/authorization/kakao"
                                   onClick={() => link({redirectURL: window.location.href})}><img src="/LoginLogo/kakao-logo.svg" alt="카카오"/></a>
                                <a href="http://localhost:8080/oauth2/authorization/naver"
                                   onClick={() => link({redirectURL: window.location.href})}><img src="/LoginLogo/naver-logo.svg" alt="네이버"/></a>
                                <a href="http://localhost:8080/oauth2/authorization/google"
                                   onClick={() => link({redirectURL: window.location.href})}><img src="/LoginLogo/google-logo.svg" alt="구글"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* 로그인 실패 모달 */}
            {isErrorModalOpen && (
                <div className="modal-overlay" onClick={closeErrorModal}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <p>{loginError}</p>
                        <button onClick={closeErrorModal}>닫기</button>
                    </div>
                </div>
            )}

            {/* 비밀번호 찾기 모달 */}
            {isResetOpen && (
                <div className="modal-overlay" onClick={closeReset}>
                    <div className="reset-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeReset}><i className="bi bi-x-lg"/></button>
                        <h2>비밀번호 재설정</h2>
                        <p>가입했던 이메일을 입력해주세요.</p>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            const email = e.target[0].value;
                            if (email) handleCreateVerification(email);
                        }}>
                            <input type="email" placeholder="이메일"/>
                            <button type="submit" className="reset-submit">이메일 보내기</button>
                        </form>
                    </div>
                </div>
            )}

            {/* 비밀번호 인증 모달 */}
            {isResetCheckOpen && (
                <div className="modal-overlay" onClick={closeResetCheck}>
                    <div className="reset-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeResetCheck}><i className="bi bi-x-lg"/></button>
                        <h2>비밀번호 재설정</h2>
                        <p>인증번호를 입력하세요</p>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            const code = e.target[0].value;
                            if (code) handleCheckVerification(verifyingEmail, code);
                        }}>
                            <input type="text" placeholder="인증번호"/>
                            <button type="submit" className="reset-submit">입력</button>
                        </form>
                    </div>
                </div>
            )}

            {/* 새 비밀번호 입력 모달 */}
            {isResetNewOpen && (
                <div className="modal-overlay" onClick={closeResetNew}>
                    <div className="reset-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeResetNew}><i className="bi bi-x-lg"/></button>
                        <h2>비밀번호 재설정</h2>
                        <p>새로운 비밀번호를 입력해주세요</p>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            const newPassword = e.target[0].value;
                            if (newPassword) handleSetPassword(newPassword);
                        }}>
                            <input type="password" placeholder="비밀번호"/>
                            <button type="submit" className="login-submit">비밀번호 재설정</button>
                        </form>
                    </div>
                </div>
            )}
        </>
    );
};

export default Header;
