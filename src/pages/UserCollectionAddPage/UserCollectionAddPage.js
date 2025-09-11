import {React, useRef, useState} from 'react';
import "./UserCollectionAddPage.css"
import { toast } from 'react-toastify';
import { CollectionCreate } from '../../API/CollectionApi';

const UserCollectionAddPage = () => {
    const [collectionAddModal, setCollectionAddModal] = useState()

    const openCollectionAddModal = () => setCollectionAddModal(true)
    const closeCollectionAddModal = () => setCollectionAddModal(false)

    const title = useRef();
    const description = useRef();

    const handleCreateCollection = async () => {
        if(title.current.value === ""){
            toast("컬렉션 제목을 입력해주세요");
            return;
        }
        if(description.current.value === ""){
            toast("컬렉션 설명을 입력해주세요");
            return;
        }
        const data = {
            title: title.current.value,
            description: description.current.value
        }
        try {
            await CollectionCreate(data);
            toast("컬렉션이 생성되었습니다.");
        } catch (error) {
            toast("컬렉션 생성에 실패했습니다.");
            return;
        }
    }

    return (
        <div className="user-collection-add-page container">
            <div className="user-collection-add-page-wrap">
                <h1>새 컬렉션</h1>

                <div className="user-collection-add-btn-box">
                    <button className="user-collection-add-btn" onClick={handleCreateCollection}>만들기</button>
                </div>

                <input type="text" placeholder="컬렉션 제목을 입력해주세요" className="user-collection-add-title" maxLength={50} ref={title}/>

                <textarea placeholder="컬렉션 설명을 입력해주세요" rows={5} className="user-collection-add-text" maxLength={200} ref={description}/>

                <div className="user-collection-add-image-wrap">
                    <p>작품들</p>
                    <div className="user-collection-add-image-box">
                        <button className="user-collection-add-image-btn" onClick={openCollectionAddModal}>
                            <i className="bi bi-plus-lg"></i>
                            <p>작품추가</p>
                        </button>
                        {/*<label htmlFor="user-collection-add-image-input">
                                <input id="user-collection-add-image-input" type="file"/>
                            </label>*/}
                    </div>
                </div>
            </div>

            {/*컬렉션 이미지 추가 모달창*/}
            {collectionAddModal && (
                <div className="collection-add-modal-overlay" onClick={closeCollectionAddModal}>
                    <div className="collection-add-modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="collection-add-modal-close-button" onClick={closeCollectionAddModal}>
                            <i className="bi bi-x-lg"></i>
                        </button>

                        <div className="collection-add-modal-content-top">
                            <p className="collection-add-modal-title">작품 추가</p>
                            <button className="collection-add-modal-content-btn">추가</button>
                        </div>
                        <div className="collection-add-modal-content-middle">
                            <div className="collection-add-modal-search-bar">
                                <button className="collection-add-modal-search-button"><i className="bi bi-search"/></button>
                                <input
                                    type="text"
                                    placeholder="검색하여 작품 추가하기"
                                    className="collection-add-modal-search-input"
                                />
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserCollectionAddPage;