import React from 'react'
import { Route, Routes } from 'react-router-dom';
import UserDetailPage from '../pages/UserDetailPage/UserDetailPage';
import UserRatingPage from '../pages/UserRatingPage/UserRatingPage';
import UserContentPage from '../pages/UserContentPage/UserContentPage';

function UserRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path=":id" element={<UserDetailPage userInfo={userInfo} openLogin={openLogin} />} />
        <Route path=":id/rating" element={<UserRatingPage userInfo={userInfo} openLogin={openLogin} />} />
        <Route path=":id/content/:contentType" element={<UserContentPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default UserRoutes;
