import React from "react";
import "./NotFound.css"
import {useNavigate} from "react-router-dom";


const NotFound = () => {
    const navigate = useNavigate();

    return (
        <div className="not-fount-page container">
            <div className="not-fount-page-wrap">
                <div className="not-fount-page-img">
                    <img src={`${process.env.PUBLIC_URL}/icon/404-icon.svg`} alt=""/>
                </div>
                <ul className="not-fount-text">
                    <li><img src={`${process.env.PUBLIC_URL}/icon/404-error.svg`} alt=""/></li>
                    <li><h3>찾을 수 없는 페이지입니다.</h3></li>
                    <li><p>페이지 주소가 삭제되었거나, 잘못된 경로를 이용하셨어요 :)</p></li>
                    <li>
                        <button onClick={()=> navigate("/")}>홈으로 이동</button>
                        <button onClick={()=> navigate(-1)}>이전페이지</button>
                    </li>
                </ul>
            </div>
        </div>
    );

    
};
export default NotFound;