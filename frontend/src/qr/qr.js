document.addEventListener("DOMContentLoaded", function() {
    const darkModeToggle = document.querySelector('.dark-mode-toggle');
    darkModeToggle.addEventListener('click', toggleDarkMode);

    const savedTheme = localStorage.getItem('theme') || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    updateTheme(savedTheme);

    pollForVerification();
});

function pollForVerification() {
    fetch('/verification-status')
        .then(response => response.json())
        .then(data => {
            if (data === true) {
                window.location.href = '/presentation-view';
            } else {
                setTimeout(pollForVerification, 2000);
            }
        });
}

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