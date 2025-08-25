import React from 'react'
import { Route, Routes } from 'react-router-dom';
import DetailPage from '../pages/DetailPage';

function ContentRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path=":id" element={<DetailPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default ContentRoutes;