const form = document.querySelector("form");

const email = document.getElementById("email");
const password = document.getElementById("password");

const eye = document.getElementById("togglePassword");


// ===============================
// Password Visibility
// ===============================

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


// ===============================
// Login Form Validation
// ===============================

form.addEventListener("submit", function (e) {

    const emailValue = email.value.trim();

    // Don't trim passwords
    const passwordValue = password.value;

    // Email validation
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;


    // ===============================
    // Email
    // ===============================

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


    // ===============================
    // Password
    // ===============================

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


    // ===============================
    // Everything Valid
    // ===============================
    // No e.preventDefault() here.
    // Form submits normally to LoginServlet.

});