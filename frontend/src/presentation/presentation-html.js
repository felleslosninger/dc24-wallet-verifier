import React, { useEffect } from 'react';

const PresentationView = ({ claims, verified, handleBackNavigation, handleDarkModeToggle, updateTheme }) => {

  const handleAction = () => {
    // Legg til logikk for å utføre en handling her
  };

  useEffect(() => {
    const savedTheme = localStorage.getItem('theme') || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    updateTheme(savedTheme);
  }, []);

  return (
    <div>
      <header>
        <button
          className="dark-mode-toggle"
          aria-pressed="false"
          aria-label="Aktiver dark mode for bedre lesbarhet"
          onClick={handleDarkModeToggle}
        >
          Dark Mode
        </button>
        <nav>
          <ul>
            <li>
              <button onClick={handleBackNavigation} className="back-button" aria-label="Tilbake til forrige side">
                Tilbake
              </button>
            </li>
          </ul>
        </nav>
      </header>
      <main className="container">
        <section>
          <h1 style={{ marginTop: '50px' }}>Verifiserte Attributter</h1>
          <div className="attribute">
            <p className="attribute-header"><strong>Claims</strong></p>
            <div>
              {claims.map((entry, index) => (
                <p key={index}><strong>{entry.key}</strong>: <span>{entry.value}</span></p>
              ))}
            </div>
          </div>
          <div className="attribute">
            <p><strong>Verified:</strong> <span>{verified}</span></p>
          </div>
        </section>
        <section>
          <button onClick={handleAction} className="action-button" aria-label="Utfør en handling" style={{ marginTop: '20px' }}>
            Utfør en handling
          </button>
        </section>
      </main>
      <footer className="footer">
        © 2023 My Company. All rights reserved.
      </footer>
    </div>
  );
};

export default PresentationView;