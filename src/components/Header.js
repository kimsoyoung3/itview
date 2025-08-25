import React, {useEffect, useState} from 'react';
import {Link, NavLink, useLocation} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css'; // 스타일 불러오기
import { checkEmail, checkVerification, createVerification, getMyInfo, google, loginUser, logoutUser, registerUser, setPassword } from '../API/UserApi';

const Header = () => {
    /*스크롤 시*/
    const location = useLocation();
    const [scrolled, setScrolled] = useState(false);

    // 상세페이지 여부 체크
    const isDetailPage = location.pathname.includes('/content');

    useEffect(() => {
        const handleScroll = () => {
            setScrolled(window.scrollY > 50);
        };

        if (isDetailPage) {
            window.addEventListener('scroll', handleScroll);
            return () => window.removeEventListener('scroll', handleScroll);
        } else {
            setScrolled(true); // 상세페이지가 아니면 항상 scrolled 상태
        }
    }, [isDetailPage]);

    // 로그인 상태 관리
    // isLoggedIn: 로그인 여부, userInfo: 사용자 이메일
    const [userInfo, setUserInfo] = useState(null);
    const checkLoginStatus = async () => {
        try {
            const response = await getMyInfo();
            setUserInfo(response.data);
        } catch (error) {
            setUserInfo(null);
        }
    };

    useEffect(() => {
        console.log(userInfo);
    }, [userInfo]);

    const isLoggedIn = !!userInfo;
    // 컴포넌트가 마운트될 때 로그인 상태 확인
    useEffect(() => {
        checkLoginStatus();
        const match = document.cookie.match(/(?:^|;\s*)FLASH_ERROR=([^;]+)/);
        if (!match) return;
        const msg = decodeURIComponent(match[1]);
        if (msg) {
            alert(msg); // 쿠키에 저장된 메시지를 alert로 표시
        }

        // 쿠키는 일회성으로 쓰고 바로 지우기
        document.cookie = "FLASH_ERROR=; Max-Age=0; Path=/";
    }, []);

    /*로그인 모달*/
    const [isLoginOpen, setLoginOpen] = useState(false);
    const [loginEmail, setLoginEmail] = useState('');
    const [loginPassword, setLoginPassword] = useState('');
    const [loginError, setLoginError] = useState(null); // 에러 메시지 저장용
    const [isErrorModalOpen, setErrorModalOpen] = useState(false);

    const closeErrorModal = () => setErrorModalOpen(false);

    const handleLogin = async () => {
        // 로그인 로직 처리
        try {
            await loginUser({
                email: loginEmail,
                password: loginPassword
            });
            closeLogin();
            window.location.reload();
        } catch (error) {
            setLoginError('로그인에 실패했습니다. 다시 시도해주세요.');
            setErrorModalOpen(true);
        }
    };

    const handleLogout = async () => {
        // 로그아웃 로직 처리
        try {
            await logoutUser();
            setUserInfo(null);
            window.location.reload();
        } catch (error) {
            alert('로그아웃에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const [verifyingEmail, setVerifyingEmail] = useState('');
    const handleCreateVerification = async (email) => {
        // 이메일 인증 로직 처리
        try {
            await checkEmail({ email });
            setVerifyingEmail(email);
            alert('인증 번호를 전송했습니다. 메일을 확인해주세요.');
            createVerification({ email });
            closeReset();
            openResetCheck();
        } catch (error) {
            alert('인증 이메일 전송에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleCheckVerification = async (email, code) => {
        // 인증 코드 확인 로직 처리
        try {
            await checkVerification({ email, code });
            alert('인증이 완료되었습니다. 비밀번호를 재설정해주세요.');
            closeResetCheck();
            openResetNew();
        } catch (error) {
            alert('인증 코드가 잘못되었습니다. 다시 시도해주세요.');
        }
    };

    const handleSetPassword = async (newPassword) => {
        // 새 비밀번호 설정 로직 처리
        try {
            await setPassword({ email: verifyingEmail, newPassword });
            alert('비밀번호가 성공적으로 변경되었습니다.');
            closeResetNew();
            openLogin();
        } catch (error) {
            alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
        }
    };

    /*회원가입 모달*/
    const [isSignupOpen, setSignupOpen] = useState(false);
    const [signupNickname, setSignupNickname] = useState('');
    const [signupEmail, setSignupEmail] = useState('');
    const [signupPassword, setSignupPassword] = useState('');
    const handleSignup = async () => {
        // 회원가입 로직 처리
        try {
            await registerUser({
                nickname: signupNickname,
                email: signupEmail,
                password: signupPassword
            });
            alert('회원가입이 완료되었습니다.');
            closeSignup();
        } catch (error) {
            alert('이미 존재하는 이메일입니다. 다시 시도해주세요.');
        }
    };

    /*로그인 모달*/
    const openLogin = () => setLoginOpen(true);
    const closeLogin = () => setLoginOpen(false);

    /*회원가입 모달*/
    const openSignup = () => setSignupOpen(true);
    const closeSignup = () => setSignupOpen(false);

    /*모달 바깥 클릭 시 이벤트 전파 차단용*/
    const stopPropagation = (e) => e.stopPropagation();

    /*비빌번호 찾기 모달*/
    const [isResetOpen, setResetOpen] = useState(false);
    const openReset = () => setResetOpen(true);
    const closeReset = () => setResetOpen(false);

    /*비빌번호 찾기 이메일인증 모달*/
    const [isResetCheckOpen, setResetCheckOpen] = useState(false);
    const openResetCheck = () => setResetCheckOpen(true);
    const closeResetCheck = () => setResetCheckOpen(false);

    /*비빌번호 찾기 새비밀번호 입력 모달*/
    const [isResetNewOpen, setResetNewOpen] = useState(false);
    const openResetNew = () => setResetNewOpen(true);
    const closeResetNew = () => setResetNewOpen(false);

    return (
        <>
            <header
                className={`header ${scrolled ? 'header-scrolled' : 'header-transparent'}`}>

                {/*헤더 넓이 지정 부분*/}
                <div className="header_width container">

                    {/*메인 로고*/}
                    <div className="header_inner header_left">
                        <NavLink to="/" className="logo">
                            <img src="/logo.svg" alt=""/>
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
                            <NavLink className="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown"
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
                        {isLoggedIn ? (
                            <div className="user-menu">
                                <Link to="/MyPage" className="login-button">마이페이지</Link>
                                <button onClick={handleLogout} className="login-button">로그아웃</button>
                                <a href="http://localhost:8080/oauth2/authorization/google"
                                   onClick={() => google({redirectURL: window.location.href})}>
                                    구글 로그인
                                </a>
                            </div>
                        ) : (
                            <div className="user-menu">
                                <button onClick={openLogin} className="login-button">로그인</button>
                                <button onClick={openSignup} className="signUp-button">회원가입</button>
                                <a href="http://localhost:8080/oauth2/authorization/google"
                                   onClick={() => google({redirectURL: window.location.href})}>
                                    구글 로그인
                                </a>
                            </div>
                        )}
                    </div>
                </div>
            </header>

            {/*로그인 모달창*/}
            {isLoginOpen && (
                <div className="modal-overlay" onClick={closeLogin}>
                    <div className="login-modal" onClick={(e) => e.stopPropagation()}>
                        <h1><img src="/logo.svg" alt=""/></h1>
                        <h2>로그인</h2>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            handleLogin();
                        }}>
                            <input
                                type="email"
                                placeholder="이메일"
                                onChange={(e) => setLoginEmail(e.target.value)}
                            />
                            <input
                                type="password"
                                placeholder="비밀번호"
                                onChange={(e) => setLoginPassword(e.target.value)}
                            />
                            <button type="submit" className="login-submit">로그인</button>
                        </form>
                        <p onClick={openReset} className="login-text-top">비밀번호를 잊어버리셨나요?</p>
                        <p className="login-text-bottom">
                            계정이 없으신가요?
                            <span onClick={() => {
                                closeLogin();
                                openSignup();
                            }}>회원가입</span>
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
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            handleSignup();
                        }}>
                            <input
                                type="text"
                                placeholder="닉네임"
                                onChange={(e) => setSignupNickname(e.target.value)}
                            />
                            <input
                                type="email"
                                placeholder="이메일"
                                onChange={(e) => setSignupEmail(e.target.value)}
                            />
                            <input
                                type="password"
                                placeholder="비밀번호"
                                onChange={(e) => setSignupPassword(e.target.value)}
                            />
                            <button type="submit" className="login-submit">회원가입</button>
                        </form>
                        <p className="login-text-bottom">
                            이미 가입하셨나요?
                            <span onClick={() => {
                                closeSignup();
                                openLogin();
                            }}>로그인</span>
                        </p>
                    </div>
                </div>
            )}

            {/*로그인 실패 모달창*/}
            {isErrorModalOpen && (
                <div className="modal-overlay" onClick={closeErrorModal}>
                    <div className="modal-content" onClick={e => e.stopPropagation()}>
                        <p>{loginError}</p>
                        <button onClick={closeErrorModal}>닫기</button>
                    </div>
                </div>
            )}

            {/*비밀번호 찾기 모달창*/}
            {isResetOpen && (
                <div className="modal-overlay" onClick={closeReset}>
                    <div className="reset-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeReset}><i className="bi bi-x-lg"></i></button>
                        <h2>비밀번호 재설정</h2>
                        <div></div>
                        <p>비밀번호를 잊으셨나요?</p>
                        <p>가입했던 이메일을 입력해주세요.</p>
                        <p>입력하신 이메일 주소로 비밀번호 변경 메일을 보낼게요</p>
                        <form
                            onSubmit={(e) => {
                                e.preventDefault();
                                const email = e.target[0].value;
                                if (email) {
                                    handleCreateVerification(email);
                                }
                            }}>
                            <input type="email" placeholder="이메일" />
                            <button type="submit" className="reset-submit">이메일 보내기</button>
                        </form>
                    </div>
                </div>
            )}

            {/*비밀번호 찾기 이메일인증 모달창*/}
            {isResetCheckOpen && (
                <div className="modal-overlay" onClick={closeResetCheck}>
                    <div className="reset-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeResetCheck}><i className="bi bi-x-lg"></i></button>
                        <h2>비밀번호 재설정</h2>
                        <div></div>
                        <p>인증번호를 입력하세요</p>
                        <form
                            onSubmit={(e) => {
                                e.preventDefault();
                                const code = e.target[0].value;
                                if (code) {
                                    handleCheckVerification(verifyingEmail, code);
                                }
                            }}>
                            <input type="text" placeholder="인증번호" />
                            <button type="submit" className="reset-submit">입력</button>
                        </form>
                    </div>
                </div>
            )}

            {/*비밀번호 찾기 새 비밀번호 모달창*/}
            {isResetNewOpen && (
                <div className="modal-overlay" onClick={closeResetNew}>
                    <div className="reset-modal" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeResetNew}><i className="bi bi-x-lg"></i></button>
                        <h2>비밀번호 재설정</h2>
                        <div></div>
                        <p>새로운 비밀번호를 입력해주세요</p>
                        <form
                            onSubmit={(e) => {
                                e.preventDefault();
                                const newPassword = e.target[0].value;
                                if (newPassword) {
                                    handleSetPassword(newPassword);
                                }
                            }}>
                            <input type="password" placeholder="비밀번호" />
                            <button type="submit" className="login-submit">비밀번호 재설정</button>
                        </form>
                    </div>
                </div>
            )}
        </>
    );
};

export default Header;
