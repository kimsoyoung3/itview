import React from 'react';
import {NavLink} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css'; // 스타일 불러오기

const Footer = () => {
    return (
        <div className="footer-mobile">
            <div className="mobile-btn">
                <i className="bi bi-house-door-fill"></i>
                <p>홈</p>
            </div>
            <div className="mobile-btn">
                <i className="bi bi-search"></i>
                <p>검색</p>
            </div>
            <div className="mobile-btn">
                <i className="bi bi-person-fill"></i>
                <p>로그인</p>
            </div>
        </div>
    )
}

export default Footer;
