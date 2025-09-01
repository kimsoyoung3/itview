import React, {useEffect, useState} from "react";
import "./PersonDetailPage.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import { getPersonInfo } from "../../API/PersonApi";
import { useParams } from "react-router-dom";


const PersonDetailPage = ({userInfo, openLogin}) => {
    const [personInfo, setPersonInfo] = useState(null);

    const { id } = useParams();
    useEffect(() => {
        const fetchData = async () => {
            const res = await getPersonInfo(id);
            if (res) {
                setPersonInfo(res.data);
            } else {
                setPersonInfo(null);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(personInfo);
    }, [personInfo]);

    return(
        <div className="person-detail-page container">
            {/* <section className="person-detail-page-profile">
                <div className="person-detail-page-profile-img">
                    <img src="" alt=""/>
                </div>
                <div className="person-detail-page-profile-info">
                    <h3></h3>
                    <p></p>
                </div>
            </section>

            <section className="person-detail-page-content">

            </section> */}
        </div>
    )


};
export default PersonDetailPage;