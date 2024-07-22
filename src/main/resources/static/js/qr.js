document.addEventListener("DOMContentLoaded", function() {
    pollForVerification();
    const darkModeToggle = document.querySelector('.dark-mode-toggle');
    darkModeToggle.addEventListener('click', toggleDarkMode);

    // Sjekk og sett tema ved sidens lasting
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-mode');
        darkModeToggle.textContent = 'Light Mode';
    } else {
        document.body.classList.remove('dark-mode');
        darkModeToggle.textContent = 'Dark Mode';
    }
});

function pollForVerification() {
    fetch('/verification-status')
        .then(response => response.json())
        .then(data => {
            if (data == true) {
                window.location.href = '/presentation-view';
            } else {
                setTimeout(pollForVerification, 2000); // Poll every 2 seconds
            }
        });
}

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