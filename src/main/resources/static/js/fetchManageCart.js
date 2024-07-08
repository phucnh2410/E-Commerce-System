$(document).ready(function() {

});

async function addProductToCard(productId){
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
            text: "The operation was completed successfully.",
            icon: "success",
            timer: 1000, // thời gian hiển thị 1 giây (1000ms)
            showConfirmButton: false // không hiển thị nút bấm xác nhận
        });
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
