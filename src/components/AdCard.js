import React from 'react';
import '../App.css';

const AdCard = ({ image, tag, title, subtitle, badge, link }) => {
    return (
        <div className="ad-card">
            <img src={image} alt={title} className="ad-image" />
            <div className="ad-info">
                {badge && <div className="ad-badge">{badge}</div>}
                <div className="ad-tag">{tag}</div>
                <div className="ad-title">{title}</div>
                <div className="ad-subtitle">{subtitle}</div>
                {link && (
                    <a href={link} className="ad-button" target="_blank" rel="noreferrer">
                        상세보기
                    </a>
                )}
            </div>
        </div>
    );
};

export default AdCard;
