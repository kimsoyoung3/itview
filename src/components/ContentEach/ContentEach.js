import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./ContentEach.css";

const ContentEach = () => {
    return (
        <div className="content-each">
            <div className="content-post">
                <img src="/image.jpg" alt="" />
            </div>
            <div className="info">
                <p className="title">제목</p>
                <p className="rating">평가</p>
            </div>
        </div>
    )
}

export default ContentEach