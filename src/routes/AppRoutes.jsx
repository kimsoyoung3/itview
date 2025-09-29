import React from 'react'
import { Route, Routes } from 'react-router-dom';
import ContentRoutes from './ContentRoutes';
import CommentRoutes from './CommentRoutes';
import PersonRoutes from "./PersonRoutes";
import UserRoutes from './UserRoutes';
import CollectionRoutes from './CollectionRoutes';
import SearchRoutes from './SearchRoutes';
import HomeRoutes from './HomeRoutes';
import NotificationPage from '../pages/NotificationPage/NotificationPage';

function AppRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
      <Route path="/*" element={<HomeRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/content/*" element={<ContentRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/comment/*" element={<CommentRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/person/*" element={<PersonRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/user/*" element={<UserRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/collection/*" element={<CollectionRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/search/*" element={<SearchRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/notification" element={<NotificationPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default AppRoutes;