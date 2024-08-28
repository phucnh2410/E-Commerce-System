// $(document).ready(function() {
    document.addEventListener('DOMContentLoaded', function() {
        var searchInput = document.querySelector('.search-input');
        var header = document.querySelector('header'); // Thay đổi selector nếu cần

        window.addEventListener('scroll', function() {
            var stickyPoint = header.offsetHeight; // Vị trí kích hoạt sticky

            if (window.scrollY > stickyPoint) {
                searchInput.classList.add('sticky-active');
            } else {
                searchInput.classList.remove('sticky-active');
            }
        });
    });


    document.addEventListener('DOMContentLoaded', function() {
        var searchInput = document.querySelector('.search-input');

        searchInput.addEventListener('input', function() {
            if (searchInput.value.trim() !== '') {
                searchInput.classList.add('has-value');
            } else {
                searchInput.classList.remove('has-value');
            }
        });
    });


// });




