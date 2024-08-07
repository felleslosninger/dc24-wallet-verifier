// App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './Home/home-html';
import QRPage from './qr/qr-html';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/qr-html" element={<QRPage />} />
      </Routes>
    </Router>
  );
};

export default App;