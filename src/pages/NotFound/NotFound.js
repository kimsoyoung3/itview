import React from "react";
import "./NotFound.css"


const NotFound = () => {
    return (
        <div className="not-fount-page container">
            <div className="not-fount-page-wrap">
                <div className="not-fount-page-img">
                    <img src="/icon/404-icon.svg" alt=""/>
                </div>
                <ul className="not-fount-text">
                    <li><img src="/icon/404-error.svg" alt=""/></li>
                    <li>죄송합니다. 요청하신 페이지를 찾을수 없습니다.</li>
                </ul>
            </div>
        </div>
    );

    
};
export default NotFound;