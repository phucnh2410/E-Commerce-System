const carousel = document.getElementById("carousel");
const prevBtn = document.getElementById("previous-btn");
const nextBtn = document.getElementById("next-btn");

prevBtn.addEventListener("click", () => {
    carousel.scrollBy({
        left: -220,
        behavior: "smooth",
    });
});

nextBtn.addEventListener("click", () => {
    carousel.scrollBy({
        left: 220,
        behavior: "smooth",
    });
});
// Optionally, you can disable the buttons when reaching the start/end
carousel.addEventListener("scroll", () => {
    prevBtn.disabled = carousel.scrollLeft === 0;
    nextBtn.disabled =
        carousel.scrollLeft + carousel.offsetWidth >= carousel.scrollWidth;
});