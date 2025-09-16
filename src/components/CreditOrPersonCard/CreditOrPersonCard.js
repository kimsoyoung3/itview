import React from "react";
import "./CreditOrPersonCard.css";
import PropTypes from "prop-types";
import { NavLink } from "react-router-dom";

const CreditOrPersonCard = ({ data, type }) => {
    const isCredit = type === "credit";
    const person = isCredit ? data.person : data;

    return (
        <div className="credit-card">

            {/* 링크 전체 영역 */}
            <NavLink to={`/person/${person.id}`} className="credit-card-inner">

                {/* 프로필 이미지 */}
                <div className="credit-card-image">
                    <img src={person.profile ? person.profile : `${process.env.PUBLIC_URL}/user.png`} alt="프로필"/>
                </div>

                {/* 크레딧인지 일반 인물인지 구분 */}
                {isCredit ? (
                    <ul className="credit-card-info">
                        <li>{person.name}</li>
                        <li>
                            <span>{data.role} </span>
                            {data.characterName && (<span> &#124; {data.characterName}</span>)}
                        </li>
                    </ul>
                ) : (
                    <ul className="credit-card-info">
                        <li>{person.name}</li>
                        <li>{person.job}</li>
                    </ul>
                )}

            </NavLink>
        </div>
    );
};

/* PropTypes 검증 */
CreditOrPersonCard.propTypes = {
    data: PropTypes.shape({
        profile: PropTypes.string,
        name: PropTypes.string,
        job: PropTypes.string,
        role: PropTypes.string,
        characterName: PropTypes.string,
        person: PropTypes.shape({
            profile: PropTypes.string,
            name: PropTypes.string,
        }),
    }).isRequired,
    type: PropTypes.oneOf(['credit', 'person']).isRequired,
};

export default CreditOrPersonCard;
