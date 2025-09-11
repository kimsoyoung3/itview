import {React, useState} from 'react';
import "./UserCollectionAddPage.css"

const UserCollectionAddPage = () => {
    const [collectionAddModal, setCollectionAddModal] = useState()

    return (
        <div className="user-collection-add-page container">
            <div className="user-collection-add-page-wrap">
                <h1>새 컬렉션</h1>
                <div className="user-collection-add-btn-box"><button className="user-collection-add-btn">만들기</button></div>
                <form action="" className="user-collection-add-form">
                    <input type="text" placeholder="컬렉션 제목을 입력해주세요" className="user-collection-add-title"/>
                    <textarea placeholder="컬렉션 설명을 입력해주세요" rows={5} className="user-collection-add-text"/>
                    <div className="user-collection-add-image-wrap">
                        <p>작품들</p>
                        <div className="user-collection-add-image-box">
                            <button className="user-collection-add-image-btn">
                                <i className="bi bi-plus-lg"></i>
                                <p>작품추가</p>
                            </button>
                            {/*<label htmlFor="user-collection-add-image-input">
                                <input id="user-collection-add-image-input" type="file"/>
                            </label>*/}
                        </div>
                    </div>

                </form>
            </div>
        </div>
    );
};

export default UserCollectionAddPage;