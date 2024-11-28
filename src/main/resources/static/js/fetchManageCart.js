$(document).ready(function() {
    var selectedSellers = {}; // an object to store seller info
    //Lắng nghe sự kiện thay đổi trên tất cả các checkbox của seller
    document.querySelector('#checked-all-checkbox').addEventListener('change', function () {
        var isChecked = this.checked;

        // Chọn hoặc bỏ chọn tất cả các seller-checkbox và product-checkbox
        document.querySelectorAll('.seller-checkbox').forEach(function (sellerCheckbox) {
            sellerCheckbox.checked = isChecked; // Chọn tất cả các seller-checkbox
            var sellerId = sellerCheckbox.getAttribute('data-seller-id');

            // Chọn hoặc bỏ chọn tất cả các product-checkbox của seller đó
            document.querySelectorAll('.product-checkbox[data-seller-id="' + sellerId + '"]').forEach(function (productCheckbox) {
                productCheckbox.checked = isChecked;
            });

            if (isChecked) {
                // Nếu "Select Alls" được chọn, lấy tất cả các sản phẩm của mỗi seller
                selectedSellers[sellerId] = getSelectedProducts(sellerId);
                console.log('Selected products for seller:', selectedSellers[sellerId]);
            } else {
                // Nếu "Select Alls" bị bỏ chọn
                selectedSellers[sellerId] = {};
                console.log('No products selected for seller ' + sellerId);
            }

            updateTotalAmount(); // Cập nhật tổng số lượng và giá
        });
    });

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
            const sellerId = productCheckbox.getAttribute('data-seller-id');
            const productId = productCheckbox.getAttribute('data-product-id');
            const getQuantityInput = document.querySelector('.quantity-input[data-product-id="' + productId + '"]');
            const getTotal = document.querySelector('.product-total[data-product-id="' + productId + '"]');

            const productTotal = getTotal ? parseFloat(getTotal.textContent.trim().replace('$', '')) : 0;
            const productQuantity = getQuantityInput ? parseInt(getQuantityInput.value) : 1;

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
    const data = getSelectedProducts();

    return data.map(data => ({
        user: {id: data.sellerId},
        cartTemps: Object.values(data.products).map(cartTemp => ({
            product: {id: cartTemp.productId},
            quantity: cartTemp.quantity,
            total: cartTemp.total
        }))
    }));
}


async function checkoutAction(){
    const totalMoneyElement = document.getElementById("totalMoney");
    const totalMoney = totalMoneyElement ? parseFloat(totalMoneyElement.textContent.trim().replace('$', '')) : 0;
    if (totalMoney === 0) {
        console.log("No any product is selected")
        return;
    }
    try {
        const data = transferObject();
        // const purchaseLink = document.getElementById("purchase-link");
        // if (purchaseLink){
        //     const queryString = new URLSearchParams({ data: JSON.stringify(data) }).toString();
        //     purchaseLink.setAttribute('href', `/shopping_cart/checkout?${queryString}`);
        //
        //     window.location.href = purchaseLink.href;
        //     //redirect to url = /shopping_cart/checkout?data=..........
        // }

        const response = await fetch('/api/cart/store_checkout_info', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const error = await response.text()
            return error;
        }

        window.location.href = "/shopping_cart/checkout";

    } catch (error) {
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


async function buyNowAction(productId, sellerId, price){
    let productQuantity = 1;
    const getProductQuantity = document.getElementById("product-quantity");
    productQuantity = getProductQuantity && getProductQuantity.value ? parseInt(getProductQuantity.value) : 1;

    console.log('Seller id: '+sellerId +' - product id:'+productId + ' - quantity: '+productQuantity +' - price: '+price +' - total: '+price*productQuantity);

    const data = [
        {
            user: { id: sellerId },
            cartTemps: [
                {
                    product: { id: productId },
                    quantity: productQuantity,
                    total: price * productQuantity
                }
            ]
        }
    ];

   try{
       const response = await fetch('/api/cart/store_checkout_info', {
           method: 'POST',
           headers: {'Content-Type': 'application/json'},
           body: JSON.stringify(data)
       });

       if (!response.ok) {
           if (response.status === 401){
               window.location.href = "/login";
               return;
           }
           if (response.status === 403){
               window.location.href = "/403";
               return;
           }
           const error = await response.text()
           return error;
       }

       window.location.href = "/shopping_cart/checkout";
   }catch (error) {
       console.error('An error with the checkout action operation:', error);
   }

}

async function addProductToCart(productId){
    let productQuantity = 1;
    const getProductQuantity = document.getElementById("product-quantity");
    productQuantity = getProductQuantity && getProductQuantity.value ? parseInt(getProductQuantity.value) : 1;

    const messageElement = document.getElementById('product-detail-message');

    try{
        const response = await fetch("/api/cart/add/"+productId +"?quantity="+productQuantity, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        const result = await response.json();

        if (!response.ok){
            if (response.status === 401){
                window.location.href = "/login";
                return;
            }
            if (response.status === 403){
                window.location.href = "/403";
                return;
            }
            messageElement.style.display = 'block';
            messageElement.textContent = result.message;
            messageElement.style.color = 'red';
            return;
        }

        messageElement.style.display = 'none';

        //Popup notice successfully
        Swal.fire({
            title: "Success!",
            text: result.message,
            icon: "success",
            timer: 1000, // thời gian hiển thị 1 giây (1000ms)
            showConfirmButton: false // không hiển thị nút bấm xác nhận
        });
        getProductQuantity.value = 1;
        await showNumberOfProductInCartIcon();
    }catch (error) {
        console.error(`There was a problem with the add product to cart operation:`, error);
        //
        // Swal.fire({
        //     title: "Error!",
        //     text: "Something went wrong.",
        //     icon: "error",
        //     timer: 1000, // thời gian hiển thị 1 giây (1000ms)
        //     showConfirmButton: false // không hiển thị nút bấm xác nhận
        // });
        // window.location.href = '/login';
    }
}

let debounceTimeout;
async function updateQuantity(productId, quantity) {
    const quantityInput = document.getElementById("quantity-input-product-"+productId);
    clearTimeout(debounceTimeout)
    debounceTimeout = setTimeout(async () => {
        const totalAmount = document.getElementById("total-amount-product-" + productId);

        const messageElement = document.getElementById('cart-message-' + productId);

        try {
            const response = await fetch("/api/cart/quantity/" + productId + "?quantity=" + quantity, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            const result = await response.json();

            if (response.ok) {
                messageElement.textContent = result.message;

                //update data into view
                quantityInput.value = result.quantity;
                totalAmount.innerText = '$' + (result.price * result.quantity).toFixed(1);

                // await getCart();
                const productCartImg = document.querySelectorAll(".product-cart-image");

                if (productCartImg) {
                    for (const productCart of productCartImg) {
                        const productId = productCart.getAttribute("data-product-id");
                        const fileName = productCart.getAttribute("data-file-name");

                        try {
                            await getProductImage(productId, fileName, productCart);
                        } catch (error) {
                            console.error(`There was a problem with the update quantity operation for product ID ${productId}:`, error);
                        }
                    }
                } else {
                    console.log("class 'product-cart-image' does not exist!!!");
                }
            }

            if (!response.ok) {
                messageElement.textContent = result.message;
                messageElement.style.color = 'red';

                if (result.stock !== undefined) {
                    quantityInput.setAttribute('max', result.stock);
                    quantityInput.value = result.stock;
                }
                return;
            }

        } catch (error) {
            console.error(`There was a problem with the update quantity from cart operation:`, error);
        }
    }, 1000);


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
