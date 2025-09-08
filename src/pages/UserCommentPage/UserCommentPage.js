import React, {useState} from "react";
import "./UserCommentPage.css"

function UserCommentPage({ userInfo, openLogin }) {
    const [activeId, setActiveId] = useState("comment-page-tab1");

    return (
        <div className="user-comment-page container">
            <div className="user-comment-page-wrap">
                <h1>코멘트</h1>
                <div>
                    <div className="user-comment-tab-title">
                        <div className={`comment-page-tab-btn ${activeId === "comment-page-tab1" ? "active" : ""}`}
                             onClick={(e) => setActiveId(e.target.id)} id="comment-page-tab1">영화</div>
                        <div className={`comment-page-tab-btn ${activeId === "comment-page-tab2" ? "active" : ""}`}
                             onClick={(e) => setActiveId(e.target.id)} id="comment-page-tab2">시리즈</div>
                        <div className={`comment-page-tab-btn ${activeId === "comment-page-tab3" ? "active" : ""}`}
                             onClick={(e) => setActiveId(e.target.id)} id="comment-page-tab3">책</div>
                        <div className={`comment-page-tab-btn ${activeId === "comment-page-tab4" ? "active" : ""}`}
                             onClick={(e) => setActiveId(e.target.id)} id="comment-page-tab4">웹툰</div>
                        <div className={`comment-page-tab-btn ${activeId === "comment-page-tab5" ? "active" : ""}`}
                             onClick={(e) => setActiveId(e.target.id)} id="comment-page-tab5">음반</div>

                        <span
                            className="comment-tab-indicator"
                            style={{
                                width: "20%",
                                transform: `translateX(${["comment-page-tab1","comment-page-tab2","comment-page-tab3","comment-page-tab4","comment-page-tab5"].indexOf(activeId) * 100}%)`
                            }}
                        />
                    </div>

                    <div className="user-comment-page-select-box">
                        <select className="form-select user-comment-page-select"  aria-label="Default select example">
                            <option selected value="new">담은 순</option>
                            <option value="old">담은 역순</option>
                            <option value="my_score_high">1</option>
                            <option value="my_score_low">2</option>
                            <option value="avg_score_high">평균 별점 높은 순</option>
                            <option value="avg_score_low">평균 별점 낮은 순</option>
                        </select>
                    </div>
                </div>

                <div className="user-comment-tab-content">

                    {activeId === "comment-page-tab1" && <div className="comment-page-tab1">
                        <p>탭1</p>
                    </div>}

                    {activeId === "comment-page-tab2" && <div className="comment-page-tab2">
                        <p>탭2</p>
                    </div>}


                    {activeId === "comment-page-tab3" && <div className="comment-page-tab3">
                        <p>탭3</p>
                    </div>}


                    {activeId === "comment-page-tab4" && <div className="comment-page-tab4">
                        <p>탭4</p>
                    </div>}


                    {activeId === "comment-page-tab5" && <div className="comment-page-tab5">
                        <p>탭5</p>
                    </div>}

                </div>
            </div>
        </div>
    );
}

export default UserCommentPage;