// window.addEventListener("scroll", function () {
//     var header = document.querySelector("header");
//     header.classList.toggle("sticky", window.scrollY > 0);
// });

window.addEventListener("scroll", function () {
    var header = document.querySelector("header");
    var isSticky = window.scrollY > 0;

    if (isSticky) {
        header.classList.add("sticky");
    } else {
        header.classList.remove("sticky");
    }
});