import "./CollectionCard.css";
import {NavLink, useNavigate} from "react-router-dom";

const CollectionCard = ({collectionData}) => {

    const navigate = useNavigate();

    return (
        <div className="collection-card">
            <NavLink to={`/collection/${collectionData?.id}`} className="collection-card-wrap">

                {/* 배경 이미지와 그림자 */}
                <div className="collection-card-bg">
                    {collectionData?.poster?.slice(0, 5).map((poster, index, arr) => (
                        <div
                            key={index}
                            className="stacked-poster-wrapper"
                            style={{
                                left: `${index * 15}%`, // 겹치는 위치 조정
                                zIndex: arr.length - index,
                            }}
                        >
                            <img src={poster} alt={`포스터 ${index}`} />
                        </div>
                    ))}

                    <div className="collection-card-bg-shadow"></div>

                    <div className="collection-card-info">
                        {/* 사용자 프로필 */}
                        <div className="collection-card-profile"
                             onClick={(e) => {
                                 e.preventDefault(); // 링크 이동 방지
                                 navigate(`/user/${collectionData?.user?.id}`)}}>
                            <div className="collection-card-profile-image">
                                <img
                                    src={collectionData?.user?.profile ? collectionData.user.profile : `${process.env.PUBLIC_URL}/user.png`}
                                    alt="사용자 프로필"
                                />
                            </div>
                            <p>{collectionData?.user?.nickname}</p>
                        </div>

                        {/* 컬렉션 아이템 개수 */}
                        <div className="collection-card-item-count">
                            {collectionData?.contentCount}
                        </div>
                    </div>

                </div>

                {/* 컬렉션 제목 및 통계 */}
                <div className="collection-card-title">
                    <h1>{collectionData?.title || '제목'}</h1>
                    <span>좋아요 {collectionData?.likeCount}</span>
                    <span> 댓글 {collectionData?.replyCount}</span>
                </div>

            </NavLink>
        </div>
    );
};

export default CollectionCard;
