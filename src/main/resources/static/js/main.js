
// handle alert timeouts
setTimeout(function() {
    const alerts = document.querySelectorAll('.alert');
    for (let i = 0; i < alerts.length; i++) {
        alerts[i].style.display = 'none';
    }
}, 7000);

// handle demo user login
function demoLogin() {
    document.getElementById("demoUsername").value = "demo@gmail.com";
    document.getElementById("demoPassword").value = "demoPassword";
    document.getElementById("demoLoginForm").submit();
}