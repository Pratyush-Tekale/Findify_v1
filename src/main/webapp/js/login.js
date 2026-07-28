const form = document.querySelector("form");

const email = document.getElementById("email");
const password = document.getElementById("password");
const eye = document.getElementById("togglePassword");

if (eye) {

    eye.addEventListener("click", function () {

        if (password.type === "password") {

            password.type = "text";
            eye.textContent = "🙈";

        } else {

            password.type = "password";
            eye.textContent = "👁";

        }

    });

}

form.addEventListener("submit", function (e) {

	form.addEventListener("submit", function (e) {

	    const emailValue = email.value.trim();
	    const passwordValue = password.value.trim();

	    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

	    if (emailValue === "") {
	        e.preventDefault();
	        alert("Please enter your email.");
	        email.focus();
	        return;
	    }

	    if (!emailPattern.test(emailValue)) {
	        e.preventDefault();
	        alert("Please enter a valid email address.");
	        email.focus();
	        return;
	    }

	    if (passwordValue === "") {
	        e.preventDefault();
	        alert("Please enter your password.");
	        password.focus();
	        return;
	    }

	    if (passwordValue.length < 6) {
	        e.preventDefault();
	        alert("Password must be at least 6 characters.");
	        password.focus();
	        return;
	    }

	    // No preventDefault here.
	    // The form submits to LoginServlet.
	});``
    const emailValue = email.value.trim();
    const passwordValue = password.value.trim();

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailValue === "") {
        alert("Please enter your email.");
        email.focus();
        return;
    }

    if (!emailPattern.test(emailValue)) {
        alert("Please enter a valid email address.");
        email.focus();
        return;
    }

    if (passwordValue === "") {
        alert("Please enter your password.");
        password.focus();
        return;
    }

    if (passwordValue.length < 6) {
        alert("Password must be at least 6 characters.");
        password.focus();
        return;
    }

    	

    

});