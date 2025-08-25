import React from 'react'
import { Route, Routes } from 'react-router-dom';
import Home from "../pages/main/Home";
import ContentRoutes from './ContentRoutes';

function AppRoutes({ userInfo }) {
  return (
    <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/content/*" element={<ContentRoutes userInfo={userInfo} />} />
    </Routes>
  )
}

export default AppRoutes;