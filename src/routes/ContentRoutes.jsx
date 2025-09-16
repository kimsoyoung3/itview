import React from 'react'
import { Route, Routes } from 'react-router-dom';
import DetailPage from '../pages/DetailPage/DetailPage';
import CommentPage from "../pages/CommentPage/CommentPage";

function ContentRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
      <Route path=":id" element={<DetailPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/comment" element={<CommentPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default ContentRoutes;
