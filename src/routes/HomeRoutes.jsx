import React from 'react'
import { Route, Routes } from 'react-router-dom';
import Home from '../pages/Home/Home';
import Movie from '../pages/Home/Movie/Movie';
import Series from '../pages/Home/Series/Series';
import Book from '../pages/Home/Book/Book';

function HomeRoutes({ userInfo, openLogin }) {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/movie" element={<Movie />} />
      <Route path="/series" element={<Series />} />
      <Route path="/book" element={<Book />} />
    </Routes>
  )
}

export default HomeRoutes;