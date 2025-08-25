import React from 'react'
import { Route, Routes } from 'react-router-dom';
import Home from "../pages/main/Home";
import ContentRoutes from './ContentRoutes';

function AppRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/content/*" element={<ContentRoutes userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default AppRoutes;