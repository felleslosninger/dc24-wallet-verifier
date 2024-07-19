// Funksjon for å toggle dark mode
function toggleDarkMode() {
    const body = document.body;
    body.classList.toggle('dark-mode');

    // Oppdater tekst på knappen
    const button = document.querySelector('.dark-mode-toggle');
    if (body.classList.contains('dark-mode')) {
        button.textContent = 'Light Mode';
        localStorage.setItem('theme', 'dark');
    } else {
        button.textContent = 'Dark Mode';
        localStorage.setItem('theme', 'light');
    }
}

// Sjekk og sett tema ved sidens lasting
document.addEventListener('DOMContentLoaded', (event) => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-mode');
        document.querySelector('.dark-mode-toggle').textContent = 'Light Mode';
    } else {
        document.body.classList.remove('dark-mode');
        document.querySelector('.dark-mode-toggle').textContent = 'Dark Mode';
    }
});