document.addEventListener('DOMContentLoaded', () => {
    const button = document.querySelector('.dark-mode-toggle');
    button.addEventListener('click', toggleDarkMode);

    const savedTheme = localStorage.getItem('theme');
    updateTheme(savedTheme);
});

function toggleDarkMode() {
    const isDarkMode = document.body.classList.toggle('dark-mode');
    const theme = isDarkMode ? 'dark' : 'light';
    localStorage.setItem('theme', theme);
    updateButton(isDarkMode);
}

function updateTheme(theme) {
    const isDarkMode = theme === 'dark';
    document.body.classList.toggle('dark-mode', isDarkMode);
    updateButton(isDarkMode);
}

function updateButton(isDarkMode) {
    const button = document.querySelector('.dark-mode-toggle');
    button.textContent = isDarkMode ? 'Light Mode' : 'Dark Mode';
    button.setAttribute('aria-pressed', isDarkMode);
}