import React from 'react'
import { Route, Routes } from 'react-router-dom';
import UserCollectionAddPage from '../pages/UserCollectionAddPage/UserCollectionAddPage';
import UserCollectionDetailPage from '../pages/UserCollectionDetailPage/UserCollectionDetailPage';

function CollectionRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path="/new" element={<UserCollectionAddPage userInfo={userInfo} openLogin={openLogin} />} />
        <Route path=":id" element={<UserCollectionDetailPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default CollectionRoutes;