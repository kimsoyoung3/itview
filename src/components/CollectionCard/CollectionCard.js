import React from "react";
import "./CollectionCard.css";

const CollectionCard = ({ collectionData, userInfo, openLogin }) => {
    return (
        <div className="collection-card">
            <div className="collection-card-wrap">

                {/* 배경 이미지와 그림자 */}
                <div className="collection-card-bg">
                    <div className="collection-card-bg-shadow"></div>
                    <img src="/basic-bg.jpg" alt="배경 이미지" />

                    {/* 사용자 프로필 */}
                    <div className="collection-card-profile">
                        <div className="collection-card-profile-image">
                            <img
                                src={collectionData?.user?.profile ? collectionData.user.profile : '/user.png'}
                                alt="사용자 프로필"
                            />
                        </div>
                        <p>{collectionData?.user?.name || '이름'}</p>
                    </div>

                    {/* 컬렉션 아이템 개수 */}
                    <div className="collection-card-item-count">
                        {collectionData?.items?.length || 0}
                    </div>
                </div>

                {/* 컬렉션 제목 및 통계 */}
                <div className="collection-card-title">
                    <h1>{collectionData?.title || '제목'}</h1>
                    <span>좋아요 {collectionData?.likeCount || 0}</span>
                    <span> 댓글 {collectionData?.commentCount || 0}</span>
                </div>

            </div>
        </div>
    );
};

export default CollectionCard;
