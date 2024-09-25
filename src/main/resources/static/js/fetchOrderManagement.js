$(document).ready(function() {
    getPendingOrder().then(r => {})

    getRatingValue();

});

async function repurchaseEvent(orderId){
    const response = await fetch('/api/order/repurchase_order/'+orderId);

    const productIds = await response.json();
    console.log(productIds);
    if (response.ok){
        for (const productId of productIds) {
            const addResponse = await fetch('/api/cart/add/' + productId+'?quantity=' + 1, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (!addResponse.ok) {
                console.error(`Failed to add product ${productId} to cart`);
            }
        }

        //redirect
        window.location.href = '/shopping_cart';
    }
}

function canceledOrderEvent(orderId, status){
    Swal.fire({
        title: 'Cancel Order',
        text: "Do you really want to cancel this order?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, cancel it!'
    }).then((result) => {
        if (result.isConfirmed) {

            //Call updateOrderStatus function to handle
            updateOrderStatus(orderId, status).then(r => {
                Swal.fire(
                    'Canceled!',
                    'Your order has been canceled.',
                    'success'
                );
            });
        }
    });
}

async function updateOrderStatus(orderId, status){
    let messageElement = '';
    try {
        const response = await fetch('/api/order/update_status/'+orderId+'/'+status, {
            method: 'PUT',
            headers: {"Content-Type": "application/json"}
        });

        const result = await response.json();
        if (response.ok){
            messageElement = result.message;
            // messageElement.style.color = 'green';

            await getAllOrders();
        }else {
            messageElement = result.message;
        }


    }catch (error){
        console.error('There was a problem with the update order status in the order management page operation:', error);
    }
}

async function getPendingOrder(){
    const orderBody = document.querySelector('#pendingOrder');

    try{
        const response = await fetch('/order/pendingOrderFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const orders = await response.text();
        if (!orderBody) {
            console.log('Element #productTable not found in the response');
        }

        orderBody.innerHTML = "";
        orderBody.innerHTML = orders;
        console.log("getPendingOrder Success")
        const productPendingOrderImg = document.querySelectorAll(".product-pending-order-img");

        if (productPendingOrderImg){
            for (const productPendingOrder of productPendingOrderImg) {
                const productId = productPendingOrder.getAttribute("data-product-id");
                const fileName = productPendingOrder.getAttribute("data-file-name");

                try {
                    await getProductImage(productId, fileName, productPendingOrder);
                } catch (error) {
                    console.error(`There was a problem with the get product pending order image operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }

    }catch (error){
        console.error('There was a problem with the fetch all orders in the order management page operation:', error);
    }
}

async function getDeliveringOrder(){
    const orderBody = document.querySelector('#deliveringOrder');

    try{
        const response = await fetch('/order/deliveringOrderFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const orders = await response.text();
        if (!orderBody) {
            console.log('Element #productTable not found in the response');
        }

        orderBody.innerHTML = "";
        orderBody.innerHTML = orders;
        console.log("getDeliveringOrder Success")
        const productDeliveringOrderImg = document.querySelectorAll(".product-delivering-order-img");

        if (productDeliveringOrderImg){
            for (const productDeliveringOrder of productDeliveringOrderImg) {
                const productId = productDeliveringOrder.getAttribute("data-product-id");
                const fileName = productDeliveringOrder.getAttribute("data-file-name");

                try {
                    await getProductImage(productId, fileName, productDeliveringOrder);
                } catch (error) {
                    console.error(`There was a problem with the get product delivering order image operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }
    }catch (error){
        console.error('There was a problem with the fetch all orders in the order management page operation:', error);
    }
}

async function getReceivedOrder(){
    const receivedOrderBody = document.querySelector('#receivedOrder');

    try{
        const response = await fetch('/order/receivedOrderFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const orders = await response.text();
        if (!receivedOrderBody) {
            console.log('Element #productTable not found in the response');
        }

        receivedOrderBody.innerHTML = "";
        receivedOrderBody.innerHTML = orders;
        console.log("getReceivedOrder Success")

        const productReceivedOrderImg = document.querySelectorAll(".product-received-order-img");

        if (productReceivedOrderImg){
            for (const productReceivedOrder of productReceivedOrderImg) {
                const productId = productReceivedOrder.getAttribute("data-product-id");
                const fileName = productReceivedOrder.getAttribute("data-file-name");

                try {
                    await getProductImage(productId, fileName, productReceivedOrder);
                } catch (error) {
                    console.error(`There was a problem with the get product received order image operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }
    }catch (error){
        console.error('There was a problem with the fetch all orders in the order management page operation:', error);
    }
}

async function getCanceledOrder(){
    const orderBody = document.querySelector('#canceledOrder');

    try{
        const response = await fetch('/order/canceledOrderFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const orders = await response.text();
        if (!orderBody) {
            console.log('Element #productTable not found in the response');
        }

        orderBody.innerHTML = "";
        orderBody.innerHTML = orders;
        console.log("getCanceledOrder Success")
        const productCanceledOrderImg = document.querySelectorAll(".product-canceled-order-img");

        if (productCanceledOrderImg){
            for (const productCanceledOrder of productCanceledOrderImg) {
                const productId = productCanceledOrder.getAttribute("data-product-id");
                const fileName = productCanceledOrder.getAttribute("data-file-name");

                try {
                    await getProductImage(productId, fileName, productCanceledOrder);
                } catch (error) {
                    console.error(`There was a problem with the get product canceled order image operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }
    }catch (error){
        console.error('There was a problem with the fetch all orders in the order management page operation:', error);
    }
}

let productIdGlobal;
let orderIdGlobal;

function clickToCallPopup(productId, orderId, event){
    event.preventDefault();

    // console.log("Product ID: "+productId);
    productIdGlobal = productId;
    orderIdGlobal = orderId;

    var feedbackBtns = document.querySelectorAll('.btn-feedback-a');
    const messageElement = document.querySelector('.feedback-message');
    var modal = document.getElementById("myModal");
    var done = document.getElementsByClassName("btn-done")[0];
    // var studentForm = document.getElementById("studentForm");

    if (feedbackBtns && feedbackBtns.length > 0) {
        // Lặp qua tất cả các nút và gắn sự kiện click
        feedbackBtns.forEach(function(feedbackBtn) { // Thêm vòng lặp để duyệt qua các phần tử
            feedbackBtn.addEventListener("click", function () {

                modal.style.display = "block";
                setTimeout(function () {
                    modal.classList.add("show");
                }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng
            });
        });
    } else {
        console.error("Feedback button not found");
    }

    // Ẩn modal khi click vào nút "done"
    done.addEventListener("click", function () {

        productIdGlobal = null;
        messageElement.textContent = '';

        modal.classList.remove("show");
        setTimeout(function () {
            modal.style.display = "none";
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });

    // Ẩn modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {

        // productIdGlobal = null;
        // messageElement.textContent = '';

        if (event.target == modal) {
            modal.classList.remove("show");
            setTimeout(function () {
                modal.style.display = "none";
                // studentForm.reset();
            }, 500); // Khớp với thời gian của transition
        }
    });
}


function getRatingValue(){
    const stars = document.querySelectorAll('.star');
    const ratingValue = document.getElementById('rating-value');
    stars.forEach(star => {
        star.addEventListener('click', () => {
            const value = star.getAttribute('data-value');
            ratingValue.value = value;

            stars.forEach(s => {
                s.classList.remove('selected');
            });

            star.classList.add('selected');
            star.previousElementSibling ? star.previousElementSibling.classList.add('selected') : null;
            let prevStar = star;
            while ((prevStar = prevStar.previousElementSibling)) {
                prevStar.classList.add('selected');
            }
        });

    });
}

async function saveFeedback(){

    const ratingFinalValue = document.getElementById('rating-value');
    const feedbackContent = document.getElementById('feedback-content');

    const messageElement = document.querySelector('.feedback-message');

    //check empty
    if (ratingFinalValue.value == 0){
        messageElement.textContent = "Please rate the product with the corresponding number of stars";
        messageElement.style.color = 'red';
        return;
    }

    //Create an object
    const feedbackObject = {
        content: feedbackContent.value.trim(),
        feedbackRating: ratingFinalValue.value,
        product: {id: productIdGlobal},
        order: {id: orderIdGlobal}
    }

    try {
        //fetch api
        const response = await fetch("/api/feedback/save", {
            method: "POST",
            headers:{
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(feedbackObject)
        });

        const result = await response.json();

        if (response.ok){
            messageElement.textContent = result.message;
            messageElement.style.color = 'green';

            const stars = document.querySelectorAll('.star');
            stars.forEach(s => {
                s.classList.remove('selected');
            });

            feedbackContent.value = '';

        }
    }catch (error) {
        console.error('There is a error with the sending a feedback:', error);
    }



}

































