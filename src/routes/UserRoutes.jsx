import React from 'react'
import { Route, Routes } from 'react-router-dom';
import UserDetailPage from '../pages/UserDetailPage/UserDetailPage';

function UserRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path=":id" element={<UserDetailPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default UserRoutes;
