import React, {useEffect, useRef, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../css/CommentDetailPage.css";
import CommentCard from "../components/CommentCard";


const CommentDetailPage = ({userInfo, openLogin}) => {
    const [comments, setComments] = useState([]);



    return(
        <div className="comment-detail-page container">
            <div className="comment-detail-page-content">
{/*
                <CommentCard comment={comments} userInfo={userInfo} openLogin={openLogin}/>
*/}
            </div>
        </div>
    )


}
export default CommentDetailPage;