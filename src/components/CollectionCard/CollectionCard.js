import React from "react";
import "./CollectionCard.css"

const CollectionCard = ({collectionData, userInfo, openLogin}) => {

    return(
        <div className="collection-card">
            <div className="collection-card-wrap">
                <div className="collection-card-bg">
                    <div className="collection-card-bg-shadow"></div>
                    <img src="/basic-bg.jpg" alt=""/>
                    <div className="collection-card-profile">
                        <div className="collection-card-profile-image">
                            <img src={collectionData?.user?.profile ? collectionData?.user?.profile : '/user.png'} alt=""/>
                        </div>
                        <p>이름</p>
                    </div>

                    <div className="collection-card-item-count">10</div>
                </div>



                <div className="collection-card-title">
                    <h1>제목</h1>
                    <span>좋아요 </span>
                    <span> 댓글</span>
                </div>
            </div>
        </div>
    )

}
export default CollectionCard;