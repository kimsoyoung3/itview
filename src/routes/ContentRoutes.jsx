import React from 'react'
import { Route, Routes } from 'react-router-dom';
import DetailPage from '../pages/DetailPage';

function ContentRoutes({ userInfo }) {
  return (
    <Routes>
        <Route path=":id" element={<DetailPage userInfo={userInfo} />} />
    </Routes>
  )
}

export default ContentRoutes;