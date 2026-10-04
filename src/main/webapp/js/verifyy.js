document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector("form");

    if (!form) {
        return;
    }

    form.addEventListener("submit", function (e) {

        const answers = form.querySelectorAll("input[name^='answer_']");

        for (const input of answers) {
            if (input.value.trim().length === 0) {
                e.preventDefault();
                alert("Please answer every verification question.");
                input.focus();
                return;
            }
        }

        // Don't call preventDefault() here.
        // Let the browser submit the form to ClaimServlet.

    });

});
