document.addEventListener("DOMContentLoaded", () => {

    const addBtn = document.getElementById("addQuestionBtn");
    const extraRows = document.getElementById("extraQuestionRows");

    if (!addBtn || !extraRows) {
        return;
    }

    let nextIndex = 4; // rows 1-3 are already in the form
    const maxQuestions = 5;

    addBtn.addEventListener("click", () => {

        if (nextIndex > maxQuestions) {
            return;
        }

        const row = document.createElement("div");
        row.className = "row qa-row";
        row.innerHTML =
            '<div class="input-group">' +
            '<label>Question ' + nextIndex + '</label>' +
            '<input type="text" name="question' + nextIndex + '" placeholder="Optional extra question">' +
            '</div>' +
            '<div class="input-group">' +
            '<label>Answer ' + nextIndex + '</label>' +
            '<input type="text" name="answer' + nextIndex + '" placeholder="Optional extra answer">' +
            '</div>';

        extraRows.appendChild(row);

        nextIndex++;

        if (nextIndex > maxQuestions) {
            addBtn.style.display = "none";
        }
    });

});
