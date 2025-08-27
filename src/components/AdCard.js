import React from 'react';
import '../css/AdCard.css';
import {Link} from "react-router-dom";

const AdCard = ({ image, tag, title, subtitle, badge, link }) => {
    return (
        <div className="ad-card">
            <Link to={link} className="ad-link">
                <img src={image} alt={title} className="ad-image" />
                {badge && <div className="ad-badge">{badge}</div>}
                <div className="ad-info">
                    <div className="ad-tag">{tag}</div>
                    <div className="ad-title">{title}</div>
                    <div className="ad-subtitle">{subtitle}</div>
                </div>
            </Link>
        </div>
    );
};

export default AdCard;
