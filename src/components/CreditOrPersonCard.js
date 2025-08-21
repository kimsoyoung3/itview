import React from "react";
import "../App.css"
import PropTypes from "prop-types";

const CreditOrPersonCard = ({data, type}) => {
    const isCredit = type === "credit";
    const person = isCredit ? data.person : data;

    return(
        <div className="credit">
            <div className="credit-inner">
                <div className="credit-image">
                    <img src={person.profile} alt={person.name} />
                </div>

                {isCredit ? (
                    <>
                        <ul className="credit-info">
                            <li>{person.name}</li>
                            <li><span>{data.role} &#124;</span><span> {data.characterName}</span></li>
                        </ul>
                    </>
                ) : (
                    <ul className="credit-info">
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