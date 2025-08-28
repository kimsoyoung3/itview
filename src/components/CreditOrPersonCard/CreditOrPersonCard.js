import React from "react";
import "./CreditOrPersonCard.css"
import PropTypes from "prop-types";

const CreditOrPersonCard = ({data, type}) => {
    const isCredit = type === "credit";
    const person = isCredit ? data.person : data;

    return(
        <div className="credit-card">
            <div className="credit-card-inner">
                <div className="credit-card-image">
                    <img src={person.profile ? person.profile : "/user.png"} alt={person.name} />
                </div>

                {isCredit ? (
                    <>
                        <ul className="credit-card-info">
                            <li>{person.name}</li>
                            <li><span>{data.role} </span>{data.characterName && (<span> &#124; {data.characterName}</span>)}</li>
                        </ul>
                    </>
                ) : (
                    <ul className="credit-card-info">
                        <li>{person.name}</li>
                        <li>{person.job}</li>
                    </ul>
                )}
            </div>
        </div>
    )
}

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