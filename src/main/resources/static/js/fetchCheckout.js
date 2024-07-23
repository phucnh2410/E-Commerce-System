$(document).ready(function() {

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
        const finalTotal = parseFloat(finalToalData);

        //seller and products
        let userCarts = [];

        document.querySelectorAll('.shop-name').forEach(function (seller){
            let cartTemps = [];
            const sellerId = seller.getAttribute("data-seller-id");

            document.querySelectorAll('.products[data-seller-id="'+sellerId+'"]').forEach(function (product){
                const productId = product.getAttribute("data-product-id");

                // Tìm phần tử chứa số lượng sản phẩm
                const quantityElement = product.querySelector('.product-checkout-quantity');
                const quantity = quantityElement && quantityElement.getAttribute('data-product-id') === productId
                    ? parseInt(quantityElement.textContent.trim())
                    : null;

                // Tìm phần tử chứa tổng giá sản phẩm
                const productTotalElement = product.querySelector('.product-checkout-total');
                const productTotalData = productTotalElement && productTotalElement.getAttribute('data-product-id') === productId
                    ? productTotalElement.innerText.replace(/[^\d.-]/g, '')
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

        console.log(orderTemp);

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
                const result = await response.text();
                console.log(result.message);
            }

            await showNumberOfProductInCartIcon();


        }catch (error) {
            console.error('An error with the ordered operation:', error);
        }


    }catch (e){
        console.error("Cannot parse the data!!!")
    }
}









































