import React, { useEffect, useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import Header from './components/Header';
import AppRoutes from './routes/AppRoutes';
import { getMyInfo, loginUser, logoutUser } from './API/UserApi';

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
            alert(msg); // 쿠키에 저장된 메시지를 alert로 표시
        }

        // 쿠키는 일회성으로 쓰고 바로 지우기
        document.cookie = "FLASH_ERROR=; Max-Age=0; Path=/";
    }, []);
    
    useEffect(() => {
        console.log(userInfo);
    }, [userInfo]);

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
            alert('로그아웃에 실패했습니다. 다시 시도해주세요.');
        }
    };

    return (
        <BrowserRouter>
            <Header userInfo={userInfo} handleLogin={handleLogin} handleLogout={handleLogout}/>
            <AppRoutes/>
        </BrowserRouter>
    );
}
export default App;
