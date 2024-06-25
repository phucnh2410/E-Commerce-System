document.addEventListener("DOMContentLoaded", function () {
    const categoryItem = document.querySelector(".category-item");
    const submenu = categoryItem.querySelector(".submenu");

    categoryItem.addEventListener("mouseover", function () {
        submenu.style.display = "block";
        submenu.style.left = "205px";
        setTimeout(() => {
            submenu.classList.add("visible");
        }, 10);
    });

    categoryItem.addEventListener("mouseout", function () {
        setTimeout(() => {
            submenu.classList.remove("visible");
        }, 5);
    });
});