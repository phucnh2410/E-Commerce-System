$(document).ready(function() {
    loadProduct();
});

function loadProduct(){
    //get input value
    const searchInput = document.getElementById('search-input');

    searchInput.addEventListener('keypress', function (event){
        // const query = searchInput.value.trim();
        // if (query.length > 0){
        //     searchProduct(query).then(r => {});
        // }else {
        //     getAllProduct().then(r => {});
        // }
        if (event.key === 'Enter') {
            event.preventDefault();

            var searchTerm = searchInput.value.trim();
            if (searchTerm) {
                // Thay đổi URL theo cách bạn muốn
                let baseURL = '/products/search';
                let queryParams = 'name=' + encodeURIComponent(searchTerm);
                window.location.href = baseURL + '?' + queryParams;

                searchInput.value = "";
            }
        }
    });
}

// async function searchProduct(productName){
//     const productSearching = document.querySelector('#product-searching-page');
//
//     // try{
//     //     const response = await fetch('/products/search?query='+encodeURIComponent(productName));
//     //
//     //     if (!response.ok) {
//     //         throw new Error(response.statusText);
//     //     }
//     //
//     //     const products = await response.text();
//     //     if (!productSearching) {
//     //         console.log('Element #productTable not found in the response');
//     //     }
//     //
//     //     productSearching.innerHTML = "";
//     //     productSearching.innerHTML = products;
//     // }catch (error){
//     //     console.error('There was a problem with the product searching operation:', error);
//     // }
//
// }











































