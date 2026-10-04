document.addEventListener("DOMContentLoaded", function () {

    const form = document.querySelector("form");

    const itemName = document.getElementById("itemName");
    const category = document.getElementById("category");
    const dateFound = document.getElementById("dateFound");
    const location = document.getElementById("location");
    const description = document.getElementById("description");
    const contact = document.getElementById("contact");
    const email = document.getElementById("email");
    const image = document.getElementById("image");


    // --------------------------------------------------
    // DATE: Do not allow future dates
    // --------------------------------------------------

    const today = new Date().toISOString().split("T")[0];

    dateFound.max = today;


    // --------------------------------------------------
    // CONTACT: Allow only numbers
    // --------------------------------------------------

    contact.addEventListener("input", function () {

        contact.value = contact.value.replace(/\D/g, "");

        if (contact.value.length > 10) {
            contact.value = contact.value.substring(0, 10);
        }

    });


    // --------------------------------------------------
    // FORM VALIDATION
    // --------------------------------------------------

    form.addEventListener("submit", function (event) {

        // Remove extra spaces
        itemName.value = itemName.value.trim();
        location.value = location.value.trim();
        description.value = description.value.trim();
        contact.value = contact.value.trim();
        email.value = email.value.trim();


        // --------------------------------------------------
        // ITEM NAME
        // --------------------------------------------------

        if (itemName.value.length < 3) {

            alert("Item name must be at least 3 characters.");

            itemName.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // CATEGORY
        // --------------------------------------------------

        if (category.value === "") {

            alert("Please select a category.");

            category.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // DATE FOUND
        // --------------------------------------------------

        if (dateFound.value === "") {

            alert("Please select the date when the item was found.");

            dateFound.focus();

            event.preventDefault();

            return;
        }


        if (dateFound.value > today) {

            alert("Date Found cannot be a future date.");

            dateFound.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // LOCATION
        // --------------------------------------------------

        if (location.value.length < 3) {

            alert("Found location must be at least 3 characters.");

            location.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // DESCRIPTION
        // --------------------------------------------------

        if (description.value.length < 20) {

            alert("Description must be at least 20 characters.");

            description.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // CONTACT NUMBER
        // --------------------------------------------------

        if (!/^[0-9]{10}$/.test(contact.value)) {

            alert("Contact number must contain exactly 10 digits.");

            contact.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // EMAIL
        // --------------------------------------------------

        const emailPattern =
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(email.value)) {

            alert("Please enter a valid email address.");

            email.focus();

            event.preventDefault();

            return;
        }


        // --------------------------------------------------
        // IMAGE
        // --------------------------------------------------

        if (image.files.length > 0) {

            const file = image.files[0];

            const allowedTypes = [
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/webp"
            ];

            const maxSize = 5 * 1024 * 1024;


            // Check file type

            if (!allowedTypes.includes(file.type)) {

                alert("Only JPG, PNG, GIF or WEBP images are allowed.");

                image.value = "";

                event.preventDefault();

                return;
            }


            // Check file size

            if (file.size > maxSize) {

                alert("Image size must be less than or equal to 5 MB.");

                image.value = "";

                event.preventDefault();

                return;
            }

        }


        // --------------------------------------------------
        // ALL VALID
        // --------------------------------------------------

        // Form will submit normally.

    });

});