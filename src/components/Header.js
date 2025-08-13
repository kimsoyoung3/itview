import React, {useEffect, useState} from 'react';
import {NavLink} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css'; // 스타일 불러오기
import { getMyInfo, loginUser, registerUser } from '../API/UserApi';

const Header = () => {
    // 로그인 상태 관리
    // isLoggedIn: 로그인 여부, userInfo: 사용자 이메일
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userInfo, setUserInfo] = useState(null);
    const checkLoginStatus = async () => {
        try {
            const response = await getMyInfo();
            setIsLoggedIn(true);
            setUserInfo(response.data);
        } catch (error) {
            setIsLoggedIn(false);
            setUserInfo(null);
        }
    };

    useEffect(() => {
        console.log(isLoggedIn, userInfo);
    }, [isLoggedIn, userInfo]);

    // 컴포넌트가 마운트될 때 로그인 상태 확인
    useEffect(() => {
        checkLoginStatus();
    }, []);

    /*로그인 모달*/
    const [isLoginOpen, setLoginOpen] = useState(false);
    const [loginEmail, setLoginEmail] = useState('');
    const [loginPassword, setLoginPassword] = useState('');
    const handleLogin = async () => {
        // 로그인 로직 처리
        try {
            await loginUser({
                email: loginEmail,
                password: loginPassword
            });
            alert('로그인이 완료되었습니다.');
            closeLogin();
        } catch (error) {
            alert('로그인에 실패했습니다. 다시 시도해주세요.');
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
                        <input type="email" placeholder="이메일" onChange={(e) => setLoginEmail(e.target.value)}/>
                        <input type="password" placeholder="비밀번호" onChange={(e) => setLoginPassword(e.target.value)}/>
                        <button className="login-submit" onClick={handleLogin}>로그인</button>
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
                        <input type="text" placeholder="닉네임" onChange={(e) => setSignupNickname(e.target.value)}/>
                        <input type="email" placeholder="이메일" onChange={(e) => setSignupEmail(e.target.value)} />
                        <input type="password" placeholder="비밀번호" onChange={(e) => setSignupPassword(e.target.value)} />
                        <button className="login-submit" onClick={handleSignup}>회원가입</button>
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
