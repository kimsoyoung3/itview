import React, {useEffect, useState} from "react";
import "./PersonDetailPage.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import { getPersonInfo, getPersonWorkDomains, getPersonWorks } from "../../API/PersonApi";
import { useParams } from "react-router-dom";


const PersonDetailPage = ({userInfo, openLogin}) => {
    const [personInfo, setPersonInfo] = useState(null);
    const [workInfo, setWorkInfo] = useState(null);

    const domainNameMap = {
        "영화" : "MOVIE",
        "시리즈" : "SERIES",
        "책" : "BOOK",
        "웹툰" : "WEBTOON",
        "음반" : "RECORD"
    }

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

            var workList = {};
            for (const domain of res2.data) {
                workList[domain.contentType] = workList[domain.contentType] || {};
                workList[domain.contentType][domain.department] = workList[domain.contentType][domain.department] || [];
            }

            for (const domain in workList) {
                for (const department in workList[domain]) {
                    const res3 = await getPersonWorks(id, domainNameMap[domain], department, 1);
                    workList[domain][department] = res3.data;
                }
            }
            setWorkInfo(workList);
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(personInfo);
    }, [personInfo]);

    useEffect(() => {
        console.log(workInfo);
    }, [workInfo]);

    return(
        <div className="person-detail-page container">
             <section className="person-detail-page-profile">
                <div className="person-detail-page-profile-img">
                    <img src={personInfo?.profile ? personInfo?.profile : "/user.png" } alt=""/>
                </div>
                <div className="person-detail-page-profile-info">
                    <h4>{personInfo?.name}</h4>
                    <p>{personInfo?.job}</p>
                </div>
                 <div className="person-detail-page-profile-like">
                     <hr/>
                     <div><button><i className="bi bi-hand-thumbs-up"/> 좋아요 {personInfo?.likeCount}명이 이 인물을 좋아합니다.</button></div>
                     <hr/>
                 </div>
            </section>

            <section className="person-detail-page-content">
                {workInfo && Object.entries(workInfo).map(([contentType, departments]) => (
                    <div key={contentType} className="person-detail-page-content-domain">
                        <h5>{contentType}</h5>
                        <ul>
                            {Object.entries(departments).map(([department, items]) => (
                                <li key={department}>{department}</li>
                            ))}
                        </ul>
                    </div>
                ))}
            </section>
        </div>
    )


};
export default PersonDetailPage;