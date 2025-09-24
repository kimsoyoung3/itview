import React from 'react'
import { Route, Routes } from 'react-router-dom';
import Home from '../pages/Home/Home';
import Movie from '../pages/Home/Movie/Movie';

function HomeRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/movie" element={<Movie />} />
    </Routes>
  )
}

export default HomeRoutes;