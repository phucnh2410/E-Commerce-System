$(document).ready(function() {

});

async function getCart(){
    const cartBody = document.querySelector("#cartBody");
    const totalMoney  = document.querySelector("#totalMoney");
    const totalProduct  = document.querySelector("#totalProduct");
    try {
        const response = await fetch("/shopping_cart/fragment");

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
        console.error(`There was a problem with the add product to cart operation:`, error);
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
                    console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
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
