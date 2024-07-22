// Funksjon for å toggle dark mode
function toggleDarkMode() {
    const body = document.body;
    body.classList.toggle('dark-mode');

    // Oppdater tekst og aria-pressed på knappen
    const button = document.querySelector('.dark-mode-toggle');
    const isDarkMode = body.classList.contains('dark-mode');
    button.textContent = isDarkMode ? 'Light Mode' : 'Dark Mode';
    button.setAttribute('aria-pressed', isDarkMode);
    localStorage.setItem('theme', isDarkMode ? 'dark' : 'light');
}

// Sjekk og sett tema ved sidens lasting
document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('theme');
    const isDarkMode = savedTheme === 'dark';
    document.body.classList.toggle('dark-mode', isDarkMode);
    const button = document.querySelector('.dark-mode-toggle');
    button.textContent = isDarkMode ? 'Light Mode' : 'Dark Mode';
    button.setAttribute('aria-pressed', isDarkMode);
});