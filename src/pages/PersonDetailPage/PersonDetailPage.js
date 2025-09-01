import React, {useEffect, useState} from "react";
import "./PersonDetailPage.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import { getPersonInfo, getPersonWorkDomains } from "../../API/PersonApi";
import { useParams } from "react-router-dom";


const PersonDetailPage = ({userInfo, openLogin}) => {
    const [personInfo, setPersonInfo] = useState(null);
    const [workDomain, setWorkDomain] = useState(null);

    const { id } = useParams();
    useEffect(() => {
        const fetchData = async () => {
            const res = await getPersonInfo(id);
            if (res) {
                setPersonInfo(res.data);
            } else {
                setPersonInfo(null);
            }

            const res2 = await getPersonWorkDomains(id);
            if (res2) {
                setWorkDomain(res2.data);
            } else {
                setWorkDomain(null);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(personInfo);
    }, [personInfo]);

    useEffect(() => {
        console.log(workDomain);
    }, [workDomain]);

    return(
        <div className="person-detail-page container">
             <section className="person-detail-page-profile">
                <div className="person-detail-page-profile-img">
                    <img src={personInfo?.profile ? personInfo?.profile : "/user.png" } alt=""/>
                </div>
                <div className="person-detail-page-profile-info">
                    <h4>{personInfo.name}</h4>
                    <p>{personInfo.job}</p>
                </div>
                 <div className="person-detail-page-profile-like">
                     <hr/>
                     <div><button><i className="bi bi-hand-thumbs-up"/> 좋아요 {personInfo.likeCount}명이 이 인물을 좋아합니다.</button></div>
                     <hr/>
                 </div>
            </section>

            <section className="person-detail-page-content">

            </section>
        </div>
    )


};
export default PersonDetailPage;