import React, {useState} from "react";
import "./PersonDetailPage.css";
import 'bootstrap/dist/css/bootstrap.min.css';


const PersonDetailPage = ({userInfo, openLogin}) => {



    return(
        <div className="person-detail-page container">
            <section className="person-detail-page-profile">
                <div className="person-detail-page-profile-img">
                    <img src="" alt=""/>
                </div>
                <div className="person-detail-page-profile-info">
                    <h3></h3>
                    <p></p>
                </div>
            </section>

            <section className="person-detail-page-content">

            </section>
        </div>
    )


};
export default PersonDetailPage;