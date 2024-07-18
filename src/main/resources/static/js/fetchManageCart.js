$(document).ready(function() {
    var selectedSellers = {}; // an object to store seller info
    //Lắng nghe sự kiện thay đổi trên tất cả các checkbox của seller
    document.querySelectorAll('.seller-checkbox').forEach(function (sellerCheckbox){

        var sellerId = sellerCheckbox.getAttribute('data-seller-id');
        selectedSellers[sellerId] = {}; //init the array to store for each seller


        sellerCheckbox.addEventListener('change', function (){
            var isChecked = this.checked;

            // Tìm tất cả các checkbox của sản phẩm tương ứng với seller
            document.querySelectorAll('.product-checkbox[data-seller-id="'+sellerId+'"]').forEach(function (productCheckbox){
                productCheckbox.checked = isChecked;
            });

            if (isChecked){
                // Nếu checkbox của seller được chọn
                selectedSellers[sellerId] = getSelectedProducts(sellerId);
                console.log('Selected products for seller:', selectedSellers[sellerId]);
                // updateTotalAmount();
            }else {
                // Nếu checkbox của seller bị bỏ chọn
                selectedSellers[sellerId] = {};
                console.log('No products selected for seller ' + sellerId);
            }

            updateTotalAmount()

        });
    });

    // Lắng nghe sự kiện thay đổi trên tất cả các checkbox của sản phẩm
    document.querySelectorAll('.product-checkbox').forEach(function (productCheckbox){
        productCheckbox.addEventListener('change', function (){
            var sellerId = this.getAttribute('data-seller-id');
            selectedSellers[sellerId] = getSelectedProducts(sellerId);
            console.log('Selected products for seller:', selectedSellers[sellerId]);

            updateTotalAmount()
        });
    });




});


// Hàm để lấy thông tin các sản phẩm được chọn
function getSelectedProducts(sellerId) {
    var selectedSellers = [];

    document.querySelectorAll('.seller-checkbox:checked').forEach(function (sellerCheckbox) {
        var sellerId = sellerCheckbox.getAttribute('data-seller-id');
        var selectedProducts = {};

        document.querySelectorAll('.product-checkbox[data-seller-id="' + sellerId + '"]:checked').forEach(function (productCheckbox) {
            var productId = productCheckbox.getAttribute('data-product-id');
            var getQuantityInput = document.querySelector('.quantity-input[data-product-id="' + productId + '"]');
            var getTotal = document.querySelector('.product-total[data-product-id="' + productId + '"]');

            var productTotal = getTotal ? parseFloat(getTotal.textContent.trim().replace('$', '')) : 0;
            var productQuantity = getQuantityInput ? parseInt(getQuantityInput.value) : 1;
            selectedProducts[productId] = {
                productId: productId,
                quantity: productQuantity,
                total: productTotal
            };

        });

    });

    // Nếu không tích vào seller mà chỉ tích vào sản phẩm, lấy seller của các sản phẩm đã tích
    if (selectedSellers.length === 0) {
        var selectedProducts = {};

        // Lặp qua tất cả các sản phẩm được tích chọn
        document.querySelectorAll('.product-checkbox:checked').forEach(function (productCheckbox) {
            var sellerId = productCheckbox.getAttribute('data-seller-id');
            var productId = productCheckbox.getAttribute('data-product-id');
            var getQuantityInput = document.querySelector('.quantity-input[data-product-id="' + productId + '"]');
            var getTotal = document.querySelector('.product-total[data-product-id="' + productId + '"]');

            var productTotal = getTotal ? parseFloat(getTotal.textContent.trim().replace('$', '')) : 0;
            var productQuantity = getQuantityInput ? parseInt(getQuantityInput.value) : 1;

            if (!selectedProducts[sellerId]) {
                selectedProducts[sellerId] = {};
            }

            selectedProducts[sellerId][productId] = {
                productId: productId,
                quantity: productQuantity,
                total: productTotal
            };
        });

        // Chuyển đổi selectedProducts thành dạng mảng selectedSellers để trả về
        Object.keys(selectedProducts).forEach(function (sellerId) {
            selectedSellers.push({
                sellerId: sellerId,
                products: selectedProducts[sellerId]
            });
        });
    }

    return selectedSellers;
}

function updateTotalAmount(){
    var selectedSellers = getSelectedProducts();
    var total = 0;
    var numberOfProduct = 0;


    // Duyệt qua từng seller trong mảng selectedSellers để tính tổng số tiền và số lượng sản phẩm
    selectedSellers.forEach(function (seller) {
        Object.keys(seller.products).forEach(function (productId) {
            var product = seller.products[productId];
            total += product.total;
            numberOfProduct ++;
        });
    });
    document.getElementById("totalProduct").innerText = numberOfProduct.toString();
    document.getElementById("totalMoney").innerText = '$'+total.toFixed(1).toString();

    return total;
}

function transferObject(){
    const datas = getSelectedProducts();

    return datas.map(data => ({
        user: {id: data.sellerId},
        cartTemps: Object.values(data.products).map(cartTemp => ({
            product: {id: cartTemp.productId},
            quantity: cartTemp.quantity,
            total: cartTemp.total
        }))
    }));
}


function checkoutAction(){
    try {
        const data = transferObject();

        const purchaseLink = document.getElementById("purchase-link");
        if (purchaseLink){
            const queryString = new URLSearchParams({ data: JSON.stringify(data) }).toString();
            purchaseLink.setAttribute('href', `/shopping_cart/checkout?${queryString}`);

            window.location.href = purchaseLink.href;
            //redirect to url = /shopping_cart/checkout?data=..........
        }

    }catch (error) {
        console.error('An error with the checkout action operation:', error);
    }
}

async function getCart(){
    const cartBody = document.querySelector("#seller");
    try {
        const response = await fetch("/shopping_cart/seller_fragment");

        if (!response.ok){
            throw new Error(response.statusText);
        }

        const carts = await response.text();
        if (!cartBody){
            console.log('Element #cartBody not found in the response');
        }

        cartBody.innerHTML = "";
        cartBody.innerHTML = carts;
    }catch (error) {
        console.error(`There was a problem with the get cart operation:`, error);
    }
}

async function addProductToCart(productId){
    const productQuantity = document.getElementById("product-quantity");
    try{
        const response = await fetch("/api/cart/add/"+productId +"?quantity="+productQuantity.value, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!response.ok){
            // console.Error("Something went wrong with API response. +");
            const errorMessage = await response.text();
            throw new Error(errorMessage);
            return;
        }

        //Popup notice successfully
        Swal.fire({
            title: "Success!",
            text: "The product was added to cart successfully.",
            icon: "success",
            timer: 1000, // thời gian hiển thị 1 giây (1000ms)
            showConfirmButton: false // không hiển thị nút bấm xác nhận
        });
        await showNumberOfProductInCartIcon();
    }catch (error) {
        console.error(`There was a problem with the add product to cart operation:`, error);

        Swal.fire({
            title: "Error!",
            text: "Something went wrong.",
            icon: "error",
            timer: 1000, // thời gian hiển thị 1 giây (1000ms)
            showConfirmButton: false // không hiển thị nút bấm xác nhận
        });
    }
}

async function updateQuantity(productId, quantity) {
    const quantityInput = document.getElementById("quantity-input-product-"+productId);
    const totalAmount = document.getElementById("total-amount-product-"+productId);
    try {
        console.log("Quantity: "+quantity);
        console.log("Product id: "+productId);

        const response = await fetch("/api/cart/quantity/" + productId +"?quantity="+quantity, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!response.ok){
            const errorMessage = await response.text();
            throw new Error(errorMessage);
            return;
        }

        const data = await response.json();

        //update data into view
        quantityInput.value = data.quantity;
        totalAmount.innerText = '$' + (data.price * data.quantity).toFixed(1);

        // await getCart();
        const productCartImg = document.querySelectorAll(".product-cart-image");

        if (productCartImg){
            for (const productCart of productCartImg) {
                const productId = productCart.getAttribute("data-product-id");
                const fileName = productCart.getAttribute("data-file-name");

                try {
                    await getProductImage(productId, fileName, productCart);
                } catch (error) {
                    console.error(`There was a problem with the update quantity operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }

    } catch (error) {
        console.error(`There was a problem with the update quantity from cart operation:`, error);
    }


}

async function deleteProductFromCart(productId){
    try {
        const response = await fetch("/api/cart/"+productId, {
            method: "DELETE"
        });

        if (!response.ok){
            const errorMessage = await response.text();
            throw new Error(`Failed to delete product: ${errorMessage}`);
        }

        await getCart();
        await showNumberOfProductInCartIcon();

        const productCartImg = document.querySelectorAll(".product-cart-image");

        if (productCartImg){
            for (const productCart of productCartImg) {
                const productId = productCart.getAttribute("data-product-id");
                const fileName = productCart.getAttribute("data-file-name");

                try {
                    await getProductImage(productId, fileName, productCart);
                } catch (error) {
                    console.error(`There was a problem with the delete product from cart operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }


    }catch (error) {
        console.error(`There was a problem with the delete product from cart operation:`, error);

        Swal.fire({
            title: "Error!",
            text: "Something went wrong.",
            icon: "error",
            timer: 1000, // thời gian hiển thị 1 giây (1000ms)
            showConfirmButton: false // không hiển thị nút bấm xác nhận
        });
    }
}
