document.addEventListener("DOMContentLoaded", () => {

    const searchInput = document.getElementById("searchInput");
    const categoryFilter = document.getElementById("categoryFilter");
    const cards = document.querySelectorAll(".item-card");

    function filterItems() {

        const search = searchInput.value.trim().toLowerCase();
        const category = categoryFilter.value.trim().toLowerCase();

        let visibleCount = 0;

        cards.forEach(card => {

            const name = (card.dataset.name || "").toLowerCase();
            const location = (card.dataset.location || "").toLowerCase();
            const id = (card.dataset.id || "").toLowerCase();
            const cardCategory = (card.dataset.category || "").toLowerCase();

            // Search item name OR location OR found ID
            const matchSearch =
                name.includes(search) ||
                location.includes(search) ||
                id.includes(search);

            // Category filter
            const matchCategory =
                category === "all" ||
                category === cardCategory;

            if (matchSearch && matchCategory) {

                card.style.display = "";

                visibleCount++;

            } else {

                card.style.display = "none";

            }

        });

        updateNoResultsMessage(visibleCount);

    }


    function updateNoResultsMessage(visibleCount) {

        let message = document.getElementById("noResultsMessage");

        if (!message) {

            message = document.createElement("h2");

            message.id = "noResultsMessage";
            message.textContent = "No matching found items.";

            message.style.textAlign = "center";
            message.style.width = "100%";
            message.style.padding = "60px";
            message.style.color = "#555";

            document.querySelector(".item-grid").appendChild(message);

        }

        if (visibleCount === 0 && cards.length > 0) {

            message.style.display = "block";

        } else {

            message.style.display = "none";

        }

    }


    // Search while typing
    searchInput.addEventListener("input", filterItems);

    // Filter when category changes
    categoryFilter.addEventListener("change", filterItems);

});