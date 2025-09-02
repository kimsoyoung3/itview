import React from 'react'
import { Route, Routes } from 'react-router-dom';
import PersonDetailPage from '../pages/PersonDetailPage/PersonDetailPage';

function PersonRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path=":id" element={<PersonDetailPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default PersonRoutes;
