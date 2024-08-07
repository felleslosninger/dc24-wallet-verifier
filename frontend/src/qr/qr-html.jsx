/*  import React, { useState, useEffect } from 'react';
import { Button } from 'flowbite-react';
import { AiOutlineLoading } from 'react-icons/ai';

const QrHtml = () => {
  const [darkMode, setDarkMode] = useState(false);
  const [qrCodeUrl, setQrCodeUrl] = useState('');

  useEffect(() => {
    fetch('/api/qr-code')
      .then(response => response.json())
      .then(data => setQrCodeUrl(data.qrCodeUrl))
      .catch(error => {
        console.error('Error fetching QR code:', error);
      });

    const pollForVerification = () => {
      fetch('/verification-status')
        .then(response => response.json())
        .then(data => {
          if (data === true) {
            window.location.href = '/presentation-view';
          } else {
            setTimeout(pollForVerification, 2000);
          }
        });
    };

    pollForVerification();
  }, []);

  const toggleDarkMode = () => {
    setDarkMode(prevMode => !prevMode);
  };

  return (
    <div className={darkMode ? 'dark-mode' : ''}>
      <header>
        <button
          className="dark-mode-toggle"
          aria-pressed={darkMode}
          aria-label="Aktiver dark mode for bedre lesbarhet"
          onClick={toggleDarkMode}
        >
          {darkMode ? 'Light Mode' : 'Dark Mode'}
        </button>
        <nav>
          <ul>
            <li><a href="javascript:history.back()" className="back-button">Tilbake</a></li>
          </ul>
        </nav>
      </header>
      <main>
        <section className="container">
          <Button size="md" isProcessing processingSpinner={<AiOutlineLoading className="h-6 w-6 animate-spin" />}>
            Scan QR-kode
          </Button>
          <h4><img src={qrCodeUrl} alt="QR Code" /></h4>
          <div id="loading-dot"></div>
        </section>
      </main>
      <footer>
        <p>© 2024 Digdircamp.</p>
      </footer>
    </div>
  );
};

export default QrHtml; */