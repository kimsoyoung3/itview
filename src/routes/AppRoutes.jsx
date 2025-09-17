import React from 'react'
import { Route, Routes } from 'react-router-dom';
import Home from "../pages/Home/Home";
import ContentRoutes from './ContentRoutes';
import CommentRoutes from './CommentRoutes';
import PersonRoutes from "./PersonRoutes";
import UserRoutes from './UserRoutes';
import CollectionRoutes from './CollectionRoutes';
import SearchPage from '../pages/SearchPage/SearchPage';
import SearchContentDetailPage from "../pages/SearchContentDetailPage/SearchContentDetailPage";

function AppRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/content/*" element={<ContentRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/comment/*" element={<CommentRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/person/*" element={<PersonRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/user/*" element={<UserRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/collection/*" element={<CollectionRoutes userInfo={userInfo} openLogin={openLogin} />} />
      <Route path="/search" element={<SearchPage />} />

        <Route path="/search/content/detail" element={<SearchContentDetailPage/>} />

    </Routes>
  )
}

export default AppRoutes;