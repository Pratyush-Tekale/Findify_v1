javascript
const form = document.querySelector("form");

const fullname = document.getElementById("fullname");
const email = document.getElementById("email");
const phone = document.getElementById("phone");
const password = document.getElementById("password");
const confirmPassword = document.getElementById("confirmPassword");

const togglePassword = document.getElementById("togglePassword");
const toggleConfirmPassword = document.getElementById("toggleConfirmPassword");


if (togglePassword) {

    togglePassword.addEventListener("click", () => {

        if (password.type === "password") {
            password.type = "text";
            togglePassword.textContent = "🙈";
        } else {
            password.type = "password";
            togglePassword.textContent = "👁";
        }

    });

}


if (toggleConfirmPassword) {

    toggleConfirmPassword.addEventListener("click", () => {

        if (confirmPassword.type === "password") {
            confirmPassword.type = "text";
            toggleConfirmPassword.textContent = "🙈";
        } else {
            confirmPassword.type = "password";
            toggleConfirmPassword.textContent = "👁";
        }

    });

}


form.addEventListener("submit", function (e) {

    const name = fullname.value.trim();
    const mail = email.value.trim();
    const mobile = phone.value.trim();
    const pass = password.value.trim();
    const confirm = confirmPassword.value.trim();


    // Email validation
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;


    // Indian phone number validation
    const phonePattern = /^[6-9]\d{9}$/;


    // Name should contain only letters and spaces
    const namePattern = /^[A-Za-z ]+$/;


    // Password must contain:
    // At least 6 characters
    // At least one letter
    // At least one number
    // Only letters and numbers
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;


    // Full name
    if (name === "") {
        e.preventDefault();
        alert("Please enter your full name.");
        fullname.focus();
        return;
    }

    if (!namePattern.test(name)) {
        e.preventDefault();
        alert("Full name should contain only letters and spaces.");
        fullname.focus();
        return;
    }


    // Email
    if (!emailPattern.test(mail)) {
        e.preventDefault();
        alert("Please enter a valid email.");
        email.focus();
        return;
    }


    // Phone
    if (!phonePattern.test(mobile)) {
        e.preventDefault();
        alert("Please enter a valid 10-digit phone number.");
        phone.focus();
        return;
    }


    // Password
    if (!passwordPattern.test(pass)) {
        e.preventDefault();
        alert("Password must be at least 6 characters and contain both letters and numbers.");
        password.focus();
        return;
    }


    // Confirm password
    if (pass !== confirm) {
        e.preventDefault();
        alert("Passwords do not match.");
        confirmPassword.focus();
        return;
    }


    // If everything is valid,
    // the form submits automatically to RegisterServlet.

});

