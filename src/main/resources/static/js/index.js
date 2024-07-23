document.addEventListener("DOMContentLoaded", function() {
    const darkModeToggle = document.querySelector('.dark-mode-toggle');
    darkModeToggle.addEventListener('click', toggleDarkMode);

    // Sjekk og sett tema ved sidens lasting basert på lagret tema eller systeminnstillinger
    const savedTheme = localStorage.getItem('theme') || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    updateTheme(savedTheme);
});

function toggleDarkMode() {
    const isDarkMode = document.body.classList.toggle('dark-mode');
    updateTheme(isDarkMode ? 'dark' : 'light');
}

function updateTheme(theme) {
    const isDarkMode = theme === 'dark';
    document.body.classList.toggle('dark-mode', isDarkMode);
    const button = document.querySelector('.dark-mode-toggle');
    button.textContent = isDarkMode ? 'Light Mode' : 'Dark Mode';
    button.setAttribute('aria-pressed', isDarkMode.toString());
    localStorage.setItem('theme', theme);
}