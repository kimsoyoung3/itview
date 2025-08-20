import React from 'react'
import { Route, Routes } from 'react-router-dom';
import DetailPage from '../pages/DetailPage';

function ContentRoutes() {
  return (
    <Routes>
        <Route path=":id" element={<DetailPage />} />
    </Routes>
  )
}

export default ContentRoutes;