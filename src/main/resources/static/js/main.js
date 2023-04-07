
// handle alert timeouts
setTimeout(function() {
    const alerts = document.querySelectorAll('.alert');
    for (let i = 0; i < alerts.length; i++) {
        alerts[i].style.display = 'none';
    }
}, 7000);

// handle demo user login
function demoL() {
    document.getElementById("demoU").value = "demo@gmail.com";
    document.getElementById("demoP").value = "demoPassword";
    document.getElementById("demoLF").submit();
}