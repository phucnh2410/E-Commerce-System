$(document).ready(function() {

});

function paymentAction(){
    const userCartElement = document.querySelector('.row[data-user-carts]');
    const userCartData = userCartElement.getAttribute('data-user-carts');

    try {
        const userCartObject = JSON.parse(userCartData);

        console.log(userCartObject);
    }catch (e){
        console.error("Cannot parse the data!!!")
    }
}









































