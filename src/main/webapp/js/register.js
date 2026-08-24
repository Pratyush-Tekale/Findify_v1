const form = document.querySelector("form");

const fullname = document.getElementById("fullname");
const email = document.getElementById("email");
const phone = document.getElementById("phone");
const password = document.getElementById("password");
const confirmPassword = document.getElementById("confirmPassword");

const togglePassword = document.getElementById("togglePassword");
const toggleConfirmPassword = document.getElementById("toggleConfirmPassword");


// ===============================
// Password Visibility
// ===============================

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


// ===============================
// Form Validation
// ===============================

form.addEventListener("submit", function (e) {

    const name = fullname.value.trim();
    const mail = email.value.trim();
    const mobile = phone.value.trim();

    // Don't trim passwords
    const pass = password.value;
    const confirm = confirmPassword.value;


    // ===============================
    // Validation Patterns
    // ===============================

    // Email
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    // Indian mobile number
    // Must start with 6, 7, 8 or 9
    // Exactly 10 digits
    const phonePattern = /^[6-9]\d{9}$/;

    // Name
    // Only English letters and single spaces
    // Numbers and special characters are NOT allowed
    const namePattern = /^[A-Za-z]+(?: [A-Za-z]+)*$/;

    // Password
    // Minimum 6 characters
    // At least one letter
    // At least one number
    // Only letters and numbers
const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@]{6,}$/;

    // ===============================
    // Full Name Validation
    // ===============================

    if (name === "") {

        e.preventDefault();

        alert("Please enter your full name.");

        fullname.focus();

        return;
    }


    if (!namePattern.test(name)) {

        e.preventDefault();

        alert("Full name should contain only letters and spaces. Numbers and special characters are not allowed.");

        fullname.focus();

        return;
    }


    // ===============================
    // Email Validation
    // ===============================

    if (mail === "") {

        e.preventDefault();

        alert("Please enter your email.");

        email.focus();

        return;
    }


    if (!emailPattern.test(mail)) {

        e.preventDefault();

        alert("Please enter a valid email.");

        email.focus();

        return;
    }


    // ===============================
    // Phone Validation
    // ===============================

    if (mobile === "") {

        e.preventDefault();

        alert("Please enter your phone number.");

        phone.focus();

        return;
    }


    if (!phonePattern.test(mobile)) {

        e.preventDefault();

        alert("Please enter a valid 10-digit Indian phone number starting with 6, 7, 8 or 9.");

        phone.focus();

        return;
    }


    // ===============================
    // Password Validation
    // ===============================

    if (pass === "") {

        e.preventDefault();

        alert("Please enter a password.");

        password.focus();

        return;
    }


    if (!passwordPattern.test(pass)) {

        e.preventDefault();

        alert("Password must be at least 6 characters and contain both letters and numbers. Only letters and numbers are allowed.");

        password.focus();

        return;
    }


    // ===============================
    // Confirm Password Validation
    // ===============================

    if (confirm === "") {

        e.preventDefault();

        alert("Please confirm your password.");

        confirmPassword.focus();

        return;
    }


    if (pass !== confirm) {

        e.preventDefault();

        alert("Passwords do not match.");

        confirmPassword.focus();

        return;
    }


    // ===============================
    // Everything Valid
    // ===============================
    // Form will automatically submit
    // to RegisterServlet using POST.

});