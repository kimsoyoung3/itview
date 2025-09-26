import React, {useEffect, useRef, useState} from 'react';
import {Link, NavLink, useLocation, useNavigate} from 'react-router-dom';
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

    const [mobileSearchModal, setMobileSearchModal] =useState(false)

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
    const openSignup = () => {
        setSignupOpen(true);
        document.body.style.overflow = 'hidden';
    };
    const closeSignup = () => {
        setSignupOpen(false);
        document.body.style.overflow = 'auto';
    };

    const openReset = () => {
        setResetOpen(true);
        document.body.style.overflow = 'hidden';
    };
    const closeReset = () => {
        setResetOpen(false);
        document.body.style.overflow = 'auto';
    };

    const openResetCheck = () => {
        setResetCheckOpen(true);
        document.body.style.overflow = 'hidden';
    };
    const closeResetCheck = () => {
        setResetCheckOpen(false);
        document.body.style.overflow = 'auto';
    };

    const openResetNew = () => {
        setResetNewOpen(true);
        document.body.style.overflow = 'hidden';
    };
    const closeResetNew = () => {
        setResetNewOpen(false);
        document.body.style.overflow = 'auto';
    };

    const openMobileSearchModal = () => {
        setMobileSearchModal(true);
        document.body.style.overflow = 'hidden';
    }

    const closeMobileSearchModal = () => {
        setMobileSearchModal(false);
        document.body.style.overflow = 'auto';
    }


    /*검색 기능*/
    const navigate = useNavigate(); // useNavigate 훅 추가

    const [searchKeyword, setSearchKeyword] = useState(""); // 검색어 상태 추가

    // 검색 제출
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (!searchKeyword.trim()) return;

        // HashRouter 기준 URL 이동
        navigate(`/search?keyword=${encodeURIComponent(searchKeyword)}`);
        setSearchKeyword(""); // 검색 후 입력 초기화

        closeMobileSearchModal();
    };

    // 모바일 드롭업
    const [menuOpen, setMenuOpen] = useState(false);
    const menuRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setMenuOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const toggleMenu = () => setMenuOpen(prev => !prev);

    const handleClose = () => setMenuOpen(false);


    return (
        <>
            {/* 헤더 */}
            <header className={`header ${scrolled ? 'header-scrolled' : 'header-transparent'}`}>
                <div className="header_width container">

                    {/* 좌측: 로고 및 메뉴 */}
                    <div className="header_inner header_left">
                        <NavLink to="/" className="logo">
                            <img src={`${process.env.PUBLIC_URL}/itview-logo/mainLogo.svg`} alt="로고"/>
                        </NavLink>

                        {/* 메뉴 */}
                        <ul className="nav">
                            <li><NavLink to="/">홈</NavLink></li>
                            <li><NavLink to="/movie">영화</NavLink></li>
                            <li><NavLink to="/series">시리즈</NavLink></li>
                            <li><NavLink to="/book">책</NavLink></li>
                            <li><NavLink to="/webtoon">웹툰</NavLink></li>
                            <li><NavLink to="/record">음악</NavLink></li>
                        </ul>

                        {/* 모바일/드롭다운 메뉴 */}
                        <div className="nav-item dropdown">
                            <NavLink to="/" className="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown"
                                     aria-expanded="false">홈</NavLink>
                            <ul className="dropdown-menu">
                                <li><NavLink to="/movie">영화</NavLink></li>
                                <li><NavLink to="/series">시리즈</NavLink></li>
                                <li><NavLink to="/book">책</NavLink></li>
                                <li><NavLink to="/webtoon">웹툰</NavLink></li>
                                <li><NavLink to="/record">음악</NavLink></li>
                            </ul>
                        </div>
                    </div>

                    {/* 우측: 검색 및 로그인/회원가입 */}
                    <div className="header_inner">

                        {/* 검색바 */}
                        {/* 검색바 */}
                        <div className="search-bar">
                            <form onSubmit={handleSearchSubmit}>
                                <button type="submit" className="search-button">
                                    <i className="bi bi-search" />
                                </button>
                                <input
                                    type="text"
                                    placeholder="검색어를 입력해주세요"
                                    className="search-input"
                                    value={searchKeyword}
                                    onChange={(e) => setSearchKeyword(e.target.value)}
                                />
                            </form>
                        </div>

                        {/* 로그인/회원정보 */}
                        {userInfo ? (
                            <div className="user-menu">
                                <Link to={`/user/${userInfo.userId}`} className="login-button">마이페이지</Link>
                                <button onClick={handleLogout} className="login-button">로그아웃</button>
                            </div>
                        ) : (
                            <div className="user-menu">
                                <button onClick={() => {openLogin(); closeSignup();}} className="login-button">로그인</button>
                                <button onClick={() => {closeLogin(); openSignup();}} className="signUp-button">회원가입</button>
                            </div>
                        )}
                    </div>
                </div>
            </header>

            <header className="mobile-header">
                <div className="mobile-header-inner" ref={menuRef}>
                    {userInfo ? (
                        <div className="mobile-header-menu-login">
                            <div className="mobile-header-menu-list" onClick={toggleMenu}>
                                <div>
                                    <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-menu.svg`} alt=""/>
                                </div>
                                <p className="mobile-menu-name">메뉴</p>
                            </div>

                            <div className="mobile-header-menu-list" onClick={openMobileSearchModal}>
                                <div>
                                    <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-search.svg`} alt=""/>
                                </div>
                                <p className="mobile-menu-name">검색</p>
                            </div>

                            <div className="mobile-header-menu-list">
                                <div>
                                    <NavLink to="/">
                                        <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-home.svg`} alt=""/>
                                    </NavLink>
                                </div>
                                <p className="mobile-menu-name">홈</p>
                            </div>

                            <div className="mobile-header-menu-list">
                                <div>
                                    <Link to={`/user/${userInfo.userId}`} className="login-button">
                                        <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-user-profile.svg`} alt=""/>
                                    </Link>
                                </div>
                                <p className="mobile-menu-name">마이페이지</p>
                            </div>

                            

                            <div className="mobile-header-menu-list">
                                <div>
                                    <button onClick={handleLogout} className="login-button">
                                        <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-logout.svg`} alt=""/>
                                    </button>
                                </div>
                                <p className="mobile-menu-name">로그아웃</p>
                            </div>
                        </div>
                    ) : (
                        <div className="mobile-header-menu">
                            <div className="mobile-header-menu-list" onClick={toggleMenu}>
                                <div>
                                    <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-menu.svg`} alt=""/>
                                </div>
                                <p className="mobile-menu-name">메뉴</p>
                            </div>

                            <div className="mobile-header-menu-list" onClick={openMobileSearchModal}>
                                <div>
                                    <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-search.svg`} alt=""/>
                                </div>
                                <p className="mobile-menu-name">검색</p>
                            </div>

                            <div className="mobile-header-menu-list">
                                <div>
                                    <NavLink to="/">
                                        <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-home.svg`} alt=""/>
                                    </NavLink>
                                </div>
                                <p className="mobile-menu-name">홈</p>
                            </div>

                            <div className="mobile-header-menu-list">
                                <div>
                                    <button onClick={openLogin} className="login-button">
                                        <img className="mobile-menu-icon" src={`${process.env.PUBLIC_URL}/mobile-icon/mobile-user-profile.svg`} alt=""/>
                                    </button>
                                </div>
                                <p className="mobile-menu-name">로그인</p>
                            </div>
                        </div>
                    )}

                    <div className={`mobile-header-menu-tab-bar ${menuOpen ? "open" : "close"}`}>
                        <ul className="mobile-header-menu-tab-bar-menu">
                            <li><NavLink to="/movie" onClick={handleClose}>영화</NavLink></li>
                            <li><NavLink to="/series" onClick={handleClose}>시리즈</NavLink></li>
                            <li><NavLink to="/book" onClick={handleClose}>책</NavLink></li>
                            <li><NavLink to="/webtoon" onClick={handleClose}>웹툰</NavLink></li>
                            <li><NavLink to="/record" onClick={handleClose}>음악</NavLink></li>
                        </ul>
                    </div>
                </div>

                {mobileSearchModal && (
                    <div className="modal-overlay" onClick={closeMobileSearchModal}>
                        <div className="mobile-search-modal" onClick={(e) => e.stopPropagation()}>
                            <div className="mobile-search-bar">
                                <form onSubmit={handleSearchSubmit}>
                                    <button type="submit" className="mobile-search-button">
                                        <i className="bi bi-search" />
                                    </button>
                                    <input
                                        type="text"
                                        placeholder="검색어를 입력해주세요"
                                        className="mobile-search-input"
                                        value={searchKeyword}
                                        onChange={(e) => setSearchKeyword(e.target.value)}
                                    />
                                </form>
                            </div>
                            <button className="mobile-search-modal-btn" onClick={closeMobileSearchModal}>취소</button>
                        </div>
                    </div>
                )}
            </header>

            {/* 로그인 모달 */}
            {isLoginOpen && (
                <div className="modal-overlay" onClick={closeLogin}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src={`${process.env.PUBLIC_URL}/itview-logo/mainLogo.svg`} alt="로고"/></h1>
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
                            <input required type="email" placeholder="이메일" onChange={(e) => setLoginEmail(e.target.value)}/>
                            <input required type="password" placeholder="비밀번호" onChange={(e) => setLoginPassword(e.target.value)}/>
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
                                   onClick={() => link({redirectURL: window.location.href})}><img src={`${process.env.PUBLIC_URL}/LoginLogo/kakao-logo.svg`} alt="카카오"/></a>
                                <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/naver`}
                                   onClick={() => link({redirectURL: window.location.href})}><img src={`${process.env.PUBLIC_URL}/LoginLogo/naver-logo.svg`} alt="네이버"/></a>
                                <a href={`${process.env.REACT_APP_API_URL}/oauth2/authorization/google`}
                                   onClick={() => link({redirectURL: window.location.href})}><img src={`${process.env.PUBLIC_URL}/LoginLogo/google-logo.svg`} alt="구글"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* 회원가입 모달 */}
            {isSignupOpen && (
                <div className="modal-overlay" onClick={closeSignup}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src={`${process.env.PUBLIC_URL}/itview-logo/mainLogo.svg`} alt="로고"/></h1>
                        <h2>회원가입</h2>
                        <form onSubmit={(e) => { e.preventDefault(); handleSignup(); }}>
                            <input required type="text" placeholder="닉네임" onChange={(e) => setSignupNickname(e.target.value)}/>
                            <input required type="email" placeholder="이메일" onChange={(e) => setSignupEmail(e.target.value)}/>
                            <input required type="password" placeholder="비밀번호" onChange={(e) => setSignupPassword(e.target.value)}/>
                            <button type="submit" className="login-submit">회원가입</button>
                        </form>
                        <p className="login-text-bottom">
                            이미 가입하셨나요?
                            <span onClick={() => {closeSignup();openLogin();}}>로그인</span>
                        </p>
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
