$(document).ready(function() {

});

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



function checkoutAction(event){
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






































