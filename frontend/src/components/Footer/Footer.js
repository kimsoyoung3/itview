import React from "react";
import "./Footer.css"

const Footer = () => {

    return (
        <div className="footer">
            <div className="footer-inner container">
                <div className="footer-text">
                    <p>개발자 : ITVIEW</p>
                    <p>이메일 : itviewofficial@gmail.com</p>
                    <p>ITVIEW Ⓒ 2025. by ITVIEW Inc. All right reserved.</p>
                </div>
                <div className="footer-logo">
                    <img src={`${process.env.PUBLIC_URL}/itview-logo/footer-logo.svg`} alt=""/>
                </div>
            </div>
        </div>
    );

};
export default Footer;