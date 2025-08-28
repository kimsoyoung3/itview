import React from 'react'
import { Route, Routes } from 'react-router-dom';
import Home from "../pages/Home/Home";
import ContentRoutes from './ContentRoutes';
import CommentRoutes from './CommentRoutes';

function AppRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/content/*" element={<ContentRoutes userInfo={userInfo} openLogin={openLogin} />} />
        <Route path="/comment/*" element={<CommentRoutes userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default AppRoutes;