    const form = document.querySelector("form");

    const fullName = document.getElementById("fullName");
    const email = document.getElementById("email");
    const phone = document.getElementById("phone");
    const itemId = document.getElementById("itemId");
    const proof = document.getElementById("proof");
    const proofFile = document.getElementById("proofFile");

    form.addEventListener("submit", function (e) {

        e.preventDefault();

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        const phonePattern = /^[0-9]{10}$/;

        const itemPattern = /^FND-\d{4}$/i;

        if (fullName.value.trim() === "") {
            alert("Please enter your full name.");
            fullName.focus();
            return;
        }

        if (!emailPattern.test(email.value.trim())) {
            alert("Please enter a valid email address.");
            email.focus();
            return;
        }

        if (!phonePattern.test(phone.value.trim())) {
            alert("Please enter a valid 10-digit phone number.");
            phone.focus();
            return;
        }

        if (!itemPattern.test(itemId.value.trim())) {
            alert("Item ID should be in this format: FND-2295");
            itemId.focus();
            return;
        }

        if (proof.value.trim().length < 15) {
            alert("Please provide more details to prove ownership (minimum 15 characters).");
            proof.focus();
            return;
        }

        if (proofFile.files.length > 0) {

            const file = proofFile.files[0];

            const allowedTypes = [
                "image/jpeg",
                "image/png",
                "application/pdf"
            ];

            if (!allowedTypes.includes(file.type)) {
                alert("Only JPG, PNG or PDF files are allowed.");
                proofFile.value = "";
                return;
            }

            if (file.size > 2 * 1024 * 1024) {
                alert("File size should be less than 2 MB.");
                proofFile.value = "";
                return;
            }

        }

        alert("Verification Successful! Redirecting to Claim Page...");

        form.reset();

        window.location.href = "claim.html";

    });