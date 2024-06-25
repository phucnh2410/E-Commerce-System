const banner = document.getElementById("banner");
const images = document.querySelectorAll(".banner-image");
let currentIndex = 0;

function showNextImage() {
    currentIndex = (currentIndex + 1) % images.length;
    const translateX = -currentIndex * 100;
    banner.style.transform = `translateX(${translateX}%)`;
}

setInterval(showNextImage, 3000);