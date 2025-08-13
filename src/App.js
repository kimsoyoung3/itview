import React from 'react';
import Layout from './components/Layout';
import { BrowserRouter } from 'react-router-dom';
import Header from './components/Header';

function App() {
    return (
        <BrowserRouter>
            <Header/>
            <Layout/>
        </BrowserRouter>
    );
}
export default App;
