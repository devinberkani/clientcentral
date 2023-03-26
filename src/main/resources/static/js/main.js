setTimeout(function() {
    const alerts = document.querySelectorAll('.alert');
    for (let i = 0; i < alerts.length; i++) {
        alerts[i].style.display = 'none';
    }
}, 7000);