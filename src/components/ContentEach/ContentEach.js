import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./ContentEach.css";
import {NavLink} from "react-router-dom";

const ContentEach = ({ratingData, ratingType}) => {


    return (
        <div className="content-each">
            <div className="content-each-post">
                <NavLink to={`/content/${ratingData?.content.id}`}>
                    <img
                        src={ratingData?.content.poster}
                        alt={ratingData?.content.title}/>
                </NavLink>
            </div>
            <div className="content-each-info">
                <p>{ratingData?.content.title}</p>
                <p>평가함 <i className="bi bi-star-fill"/> {ratingData?.score / 2}</p>
            </div>
        </div>
    )
}

export default ContentEach