
function toggleSidebar() {
    const showMenuElement = document.querySelector('.show-menu a');
    const iconElement = showMenuElement.querySelector('i');

    if (iconElement.classList.contains('bx-menu')) {
        // Nếu đang là nút mở menu
        iconElement.classList.remove('bx-menu');
        iconElement.classList.add('bx-x');
        // showMenuElement.setAttribute('title', 'Đóng menu'); // Thay đổi tiêu đề nếu cần
        // Thực hiện hiện menu
        showSidebar(); // Gọi hàm hiển thị menu
    } else {
        // Nếu đang là nút đóng menu
        iconElement.classList.remove('bx-x');
        iconElement.classList.add('bx-menu');
        // showMenuElement.setAttribute('title', 'Mở menu'); // Thay đổi tiêu đề nếu cần
        // Thực hiện ẩn menu
        hideSidebar(); // Gọi hàm ẩn menu
    }
}


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