import React from 'react';
import Header from './Header';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';

const Layout = ({ children }) => {
    return (
        
        <div className="layout">
            <main>{children}</main>
        </div>
    );
};

export default Layout;
