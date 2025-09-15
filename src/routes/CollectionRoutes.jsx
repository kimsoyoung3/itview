import React from 'react'
import { Route, Routes } from 'react-router-dom';
import CollectionFormPage from '../pages/CollectionFormPage/CollectionFormPage';
import CollectionDetailPage from '../pages/CollectionDetailPage/CollectionDetailPage';

function CollectionRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
        <Route path="/new" element={<CollectionFormPage userInfo={userInfo} openLogin={openLogin} />} />
        <Route path=":id" element={<CollectionDetailPage userInfo={userInfo} openLogin={openLogin} />} />
    </Routes>
  )
}

export default CollectionRoutes;