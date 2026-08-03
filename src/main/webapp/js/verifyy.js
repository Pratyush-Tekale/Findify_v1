

const form = document.querySelector("form");
const proof = document.getElementById("proof");

form.addEventListener("submit", function (e) {

    if (proof.value.trim().length < 15) {

        e.preventDefault();

        alert("Please provide more details to prove ownership (minimum 15 characters).");

        proof.focus();

        return;
    }

    // Don't call preventDefault().
    // Let the browser submit the form to ClaimServlet.

});