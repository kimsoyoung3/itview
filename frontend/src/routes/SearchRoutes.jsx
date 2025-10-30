import React from 'react'
import { Route, Routes } from 'react-router-dom';
import SearchPage from '../pages/SearchPage/SearchPage';
import SearchContentDetailPage from '../pages/SearchContentDetailPage/SearchContentDetailPage';

function SearchRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path="/" element={<SearchPage userInfo={userInfo} openLogin={openLogin} />} />
        <Route path="/content" element={<SearchContentDetailPage/>} />
    </Routes>
  )
}

export default SearchRoutes;