import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './qr.css';

const QrHtml = ({ qrCodeUrl }) => {
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

  // Funksjon for å navigere tilbake
  const goBack = () => {
    navigate(-1);
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
        <button
          className="back-button"
          aria-label="Gå tilbake til forrige side"
          onClick={goBack}
        >
          Tilbake
        </button>
      </header>
      <main>
        <section className="container">
          <h4>
            <img src={qrCodeUrl} alt="QR Code" />
          </h4>
          <div id="loading-dot"></div>
        </section>
      </main>
      <footer>
        <p>© 2024 Digdircamp.</p>
      </footer>
    </div>
  );
};

export default QrHtml;