import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './home.css';

const Home = () => {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const navigate = useNavigate();

  // Hent tema fra localStorage ved lasting av komponenten
  useEffect(() => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
      setIsDarkMode(savedTheme === 'dark');
    } else {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      setIsDarkMode(prefersDark);
      localStorage.setItem('theme', prefersDark ? 'dark' : 'light');
    }
  }, []);

  // Oppdater body-klassen og localStorage når temaet endres
  useEffect(() => {
    document.body.classList.toggle('dark-mode', isDarkMode);
    localStorage.setItem('theme', isDarkMode ? 'dark' : 'light');
  }, [isDarkMode]);

  // Funksjon for å skifte mellom dark og light mode
  const toggleDarkMode = () => {
    setIsDarkMode(prevMode => !prevMode);
  };

  // Naviger til QR-siden
  const onNavigateToQR = () => {
    navigate('/qr-html');
  };

  return (
    <div>
      <header>
        <button
          className="dark-mode-toggle"
          aria-pressed={isDarkMode}
          aria-label="Aktiver dark mode for bedre lesbarhet"
          onClick={toggleDarkMode}
        >
          {isDarkMode ? 'Light Mode' : 'Dark Mode'}
        </button>
      </header>
      <section class="center-container">
      <h1 id="main-heading">Funksjonalitet for å velge hvilke data som skal deles</h1>
      <div class="button-container">
        <button class="qr-button" onClick={onNavigateToQR} aria-label="Naviger til siden for å generere din personlige QR-kode">
          Generer din personlige QR-kode
        </button>
      </div>
    </section>
    </div>
  );
};

export default Home;
