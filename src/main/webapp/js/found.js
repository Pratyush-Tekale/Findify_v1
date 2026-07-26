document.addEventListener("DOMContentLoaded", () => {

    const searchInput = document.getElementById("searchInput");
    const categoryFilter = document.getElementById("categoryFilter");

    const cards = document.querySelectorAll(".item-card");

    function filterItems(){

        const search = searchInput.value.toLowerCase();

        const category = categoryFilter.value;

        cards.forEach(card => {

            const name = card.querySelector(".item-name").textContent.toLowerCase();

            const cardCategory = card.dataset.category;

            const matchSearch = name.includes(search);

            const matchCategory =
                category === "all" ||
                category === cardCategory;

            if(matchSearch && matchCategory){

                card.style.display = "block";

            }

            else{

                card.style.display = "none";

            }

        });

    }

    searchInput.addEventListener("keyup", filterItems);

    categoryFilter.addEventListener("change", filterItems);

});