import React from 'react'
import { Route, Routes } from 'react-router-dom';
import CommentDetailPage from "../pages/CommentDetailPage/CommentDetailPage";

function CommentRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path=":id" element={<CommentDetailPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default CommentRoutes;