import React, { useEffect, useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import Header from './components/Header/Header';
import AppRoutes from './routes/AppRoutes';
import "./App.css"
import { getMyInfo, loginUser, logoutUser } from './API/UserApi';
import { ToastContainer, Slide, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

function App() {

    // 로그인 상태 관리
    const [userInfo, setUserInfo] = useState(null);

    const checkLoginStatus = async () => {
        try {
            const response = await getMyInfo();
            setUserInfo(response.data);
        } catch (error) {
            setUserInfo(null);
        }
    };

    // 때 로그인 상태 확인
    useEffect(() => {
        checkLoginStatus();
        const match = document.cookie.match(/(?:^|;\s*)FLASH_ERROR=([^;]+)/);
        if (!match) return;
        const msg = decodeURIComponent(match[1]);
        if (msg) {
            setTimeout(() => toast(msg), 0); // 쿠키에 저장된 메시지를 alert로 표시
        }

        // 쿠키는 일회성으로 쓰고 바로 지우기
        document.cookie = "FLASH_ERROR=; Max-Age=0; Path=/";
    }, []);

    const handleLogin = async (loginEmail, loginPassword) => {
        // 로그인 로직 처리
        try {
            await loginUser({
                email: loginEmail,
                password: loginPassword
            });
            return true;
        } catch (error) {
            return false;
        }
    };

    const handleLogout = async () => {
        // 로그아웃 로직 처리
        try {
            await logoutUser();
            setUserInfo(null);
            window.location.reload();
        } catch (error) {
            toast('로그아웃에 실패했습니다. 다시 시도해주세요.');
        }
    };

    // 로그인 창 관리
    const [isLoginOpen, setLoginOpen] = useState(false);
    
    const openLogin = () => setLoginOpen(true);
    const closeLogin = () => setLoginOpen(false);

    return (
        <BrowserRouter>
            <Header userInfo={userInfo} handleLogin={handleLogin} handleLogout={handleLogout} isLoginOpen={isLoginOpen} openLogin={openLogin} closeLogin={closeLogin}/>
            <AppRoutes userInfo={userInfo} openLogin={openLogin} />

            <ToastContainer
                position="bottom-center"   // 위치 (top-right, top-center, bottom-left 등)
                autoClose={1000}       // 자동 닫힘 시간 (ms)
                hideProgressBar={true} // 프로그레스바 숨김 여부
                newestOnTop={false}    // 최신 알림이 위에 올지
                closeOnClick           // 클릭 시 닫기
                transition={Slide}
                pauseOnHover           // 마우스 올리면 멈춤
                draggable              // 드래그로 닫기
                icon={false}
            />
        </BrowserRouter>
    );
}
export default App;
