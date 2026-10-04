document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("searchInput");
    const categoryFilter = document.getElementById("categoryFilter");
    const cards = document.querySelectorAll(".item-card");

    function filterItems() {

        const search = searchInput.value.toLowerCase().trim();
        const category = categoryFilter.value;

        let visibleCount = 0;

        cards.forEach(function (card) {

            // Get information directly from the existing HTML
            const itemNameElement = card.querySelector(".item-name");
            const itemIdElement = card.querySelector(".item-id");
            const paragraphs = card.querySelectorAll("p");

            const itemName = itemNameElement
                ? itemNameElement.textContent.toLowerCase()
                : "";

            const itemId = itemIdElement
                ? itemIdElement.textContent.toLowerCase()
                : "";

            const location = paragraphs.length > 0
                ? paragraphs[0].textContent.toLowerCase()
                : "";

            const cardCategory = card.getAttribute("data-category");

            const searchMatch =
                itemName.includes(search) ||
                itemId.includes(search) ||
                location.includes(search);

            const categoryMatch =
                category === "all" ||
                category === cardCategory;

            if (searchMatch && categoryMatch) {
                card.style.display = "";
                visibleCount++;
            } else {
                card.style.display = "none";
            }
        });

        let noResults = document.getElementById("noResultsMessage");

        if (!noResults) {
            noResults = document.createElement("h2");
            noResults.id = "noResultsMessage";
            noResults.innerText = "No matching lost items.";
            noResults.style.textAlign = "center";
            noResults.style.width = "100%";
            noResults.style.padding = "60px";
            noResults.style.color = "#555";

            document.getElementById("itemGrid").appendChild(noResults);
        }

        if (visibleCount === 0 && cards.length > 0) {
            noResults.style.display = "block";
        } else {
            noResults.style.display = "none";
        }
    }

    searchInput.addEventListener("input", filterItems);

    categoryFilter.addEventListener("change", filterItems);

});