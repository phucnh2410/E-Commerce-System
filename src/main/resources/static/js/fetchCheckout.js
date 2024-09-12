$(document).ready(function() {
    // showPopUpNotice();
});

async function paymentAction(){
    try {
        //address
        const getAddressElement = document.getElementById('data-address-id');
        const getAddress = getAddressElement.value;
        //voucher
        const getVoucherElement = document.getElementById('data-voucher-id');
        const getVoucher = getVoucherElement.value;
        //payment
        const getPaymentElement = document.getElementById('data-payment-id');
        const getPayment = getPaymentElement.value;
        //final total
        const finalTotalElement = document.getElementById("payment-total");
        const finalToalData = finalTotalElement.innerText.replace(/[^\d.-]/g, '');
        let finalTotal = parseFloat(finalToalData);

        //seller and products
        let userCarts = [];

        document.querySelectorAll('.seller-name').forEach(function (seller){
            let cartTemps = [];
            const sellerId = seller.getAttribute("data-seller-id");

            document.querySelectorAll('.products[data-seller-id="'+sellerId+'"]').forEach(function (product){
                const productId = product.getAttribute("data-product-id");

                // Tìm phần tử chứa số lượng sản phẩm
                const quantityElement = product.querySelector('.product-checkout-quantity');
                // const quantity = quantityElement && quantityElement.getAttribute('data-product-id') === productId
                //     ? parseInt(quantityElement.textContent.trim())
                //     : null;
                const quantity = quantityElement && quantityElement.getAttribute('data-product-id') === productId
                    ? parseInt(quantityElement.value)
                    : null;

                // Tìm phần tử chứa tổng giá sản phẩm
                const productTotalElement = product.querySelector('.product-checkout-total');
                // const productTotalData = productTotalElement && productTotalElement.getAttribute('data-product-id') === productId
                //     ? productTotalElement.innerText.replace(/[^\d.-]/g, '')
                //     : '0';
                const productTotalData = productTotalElement && productTotalElement.getAttribute('data-product-id') === productId
                    ? productTotalElement.value
                    : '0';
                const productTotal = parseFloat(productTotalData);

                //push cartTemp into list cartTemps
                cartTemps.push({
                    product: {id: productId},
                    quantity: quantity,
                    total: productTotal
                });
            })

            userCarts.push({
                user: { id: sellerId },
                cartTemps: cartTemps
            });
        })

        const orderTemp = {
            userCarts: userCarts,
            voucher: {id: getVoucher},
            paymentMethod: {id: getPayment},
            address: {id: getAddress},
            finalTotal: finalTotal
        }

        if (orderTemp.paymentMethod.id === "3"){//pay with PayPal
            try{
                // Lưu giá trị vào localStorage vì khi chuyển hướng thì js sẽ bị reload và mất giá trị
                localStorage.setItem('finalTotal', orderTemp.finalTotal);

                const response = await fetch("/api/order/store_order_info", {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(orderTemp)
                });

                const result = await response.text();

                if (!response.ok) {
                    console.log(result);
                    return result;
                }

                console.log(result)
                window.location.href = "/payment/paypal";
            }catch (error) {
                console.error('An error with the payment operation:', error);
            }
        }else if (orderTemp.paymentMethod.id === "1"){
            try {
                //send the data in to api to save this order
                const response = await fetch("/api/order/save", {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(orderTemp)
                });

                if (!response.ok) {
                    const result = await response.json();
                    console.log(result.message);
                    return;
                }
                const result = await response.json();
                await showNumberOfProductInCartIcon();
                window.location.href = "/order/success?id="+result.order.id;

            } catch (error) {
                console.error('An error with the ordered operation:', error);
            }
        }

    }catch (e){
        console.error("Cannot parse the data!!!")
    }
}

// function showPopUpNotice(result){
//     // result = "You ordered successful";
//     const modal = document.getElementById("bill-modal");
//     const done = document.getElementsByClassName("btn-done")[0];
//     const viewOrderDetail = document.getElementById('btn-view');
//
//     if (result && result.order) {
//         modal.style.display = "block";
//         setTimeout(function () {
//             modal.classList.add("show");
//         }, 10);
//         document.getElementById('order-message').innerText = result.message;
//         document.getElementById('order-id').innerText = result.order.id;
//         document.getElementById('final-total-amount').innerText = "$"+result.order.totalAmount;
//         document.getElementById('address').innerText = result.address.street+", "
//             +result.address.wardAndCommune+", "
//             +result.address.district+", "
//             +result.address.city;
//         document.getElementById('payment-method').innerText = result.payment.name;
//     }
//
//     viewOrderDetail.addEventListener("click", function (){
//         if (result && result.order && result.order.id) {
//             const orderId = result.order.id;
//             // Update href attribute of the a tag
//             viewOrderDetail.setAttribute('href', `/order?id=${orderId}`);
//             // Execute direction
//             window.location.href = viewOrderDetail.href;
//         }else {
//             console.error('Order ID does not exist in the result variable');
//         }
//     });
//
//     done.addEventListener("click", function () {
//         modal.classList.remove("show");
//         setTimeout(function () {
//             modal.style.display = "none";
//         }, 500);
//     });
//
// }









































