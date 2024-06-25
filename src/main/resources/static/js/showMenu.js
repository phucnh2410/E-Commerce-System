function showSidebar() {
    const sidebar = document.querySelector(".sidebar");
    sidebar.style.display = "block";
    setTimeout(() => {
        sidebar.classList.add("visible");
    }, 10);
}

function hideSidebar() {
    const sidebar = document.querySelector(".sidebar");
    sidebar.classList.remove("visible");
    sidebar.addEventListener(
        "transitionend",
        function handleTransitionEnd() {
            sidebar.style.display = "none";
            sidebar.removeEventListener("transitionend", handleTransitionEnd);
        }
    );
}