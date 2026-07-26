const form = document.querySelector("form");

const itemName = document.getElementById("itemName");
const category = document.getElementById("category");
const dateLost = document.getElementById("dateLost");
const locationField = document.getElementById("location");
const description = document.getElementById("description");
const contact = document.getElementById("contact");
const email = document.getElementById("email");
const image = document.getElementById("image");

form.addEventListener("submit", function (e) {

    e.preventDefault();

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phonePattern = /^[0-9]{10}$/;

    if (itemName.value.trim() === "") {
        alert("Please enter item name.");
        itemName.focus();
        return;
    }

    if (category.value === "") {
        alert("Please select a category.");
        category.focus();
        return;
    }

    if (dateLost.value === "") {
        alert("Please select the date.");
        dateLost.focus();
        return;
    }

    const selectedDate = new Date(dateLost.value);
    const today = new Date();

    today.setHours(0,0,0,0);

    if (selectedDate > today) {
        alert("Date cannot be in the future.");
        dateLost.focus();
        return;
    }

    if (locationField.value.trim() === "") {
        alert("Please enter the lost location.");
        locationField.focus();
        return;
    }

    if (description.value.trim().length < 10) {
        alert("Description should be at least 10 characters.");
        description.focus();
        return;
    }

    if (!phonePattern.test(contact.value.trim())) {
        alert("Please enter a valid 10-digit contact number.");
        contact.focus();
        return;
    }

    if (!emailPattern.test(email.value.trim())) {
        alert("Please enter a valid email address.");
        email.focus();
        return;
    }

    if (image.files.length > 0) {

        const file = image.files[0];

        const allowedTypes = [
            "image/jpeg",
            "image/png",
            "image/jpg"
        ];

        if (!allowedTypes.includes(file.type)) {
            alert("Only JPG and PNG images are allowed.");
            image.value = "";
            return;
        }

        if (file.size > 2 * 1024 * 1024) {
            alert("Image size should be less than 2 MB.");
            image.value = "";
            return;
        }

    }

    alert("Lost Item Report Submitted Successfully!");

    form.reset();

});