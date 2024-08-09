$(document).ready(function() {

    //User avatars
    const avatarProfile = document.getElementById("avatar-profile");
    const avatarHeader = document.getElementById("avatar-header");
    const avatarShop = document.getElementById("avatar-shop");
    const avatarProductDetail = document.getElementById("avatar-product-detail");
    // const userImgAdminPage =  document.querySelectorAll('.user-img');

    if (avatarProfile){
        const userId = avatarProfile.getAttribute("data-user-id");
        const fileName = avatarProfile.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarProfile).then(r => {});
    }

    if (avatarHeader){
        const userId = avatarHeader.getAttribute("data-user-id");
        const fileName = avatarHeader.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarHeader).then(r => {});
    }

    if (avatarShop){
        const userId = avatarShop.getAttribute("data-user-id");
        const fileName = avatarShop.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarShop).then(r => {});
    }

    if (avatarProductDetail){
        const userId = avatarProductDetail.getAttribute("data-user-id");
        const fileName = avatarProductDetail.getAttribute("data-file-name");
        getUserAvatar(userId, fileName, avatarProductDetail).then(r => {});
    }

    // if (userImgAdminPage){
    //     userImgAdminPage.forEach(async (userAvatar) =>{
    //         const userId = userAvatar.getAttribute("data-user-id");
    //         const fileName = userAvatar.getAttribute("data-file-name");
    //
    //         try {
    //             await getUserAvatar(userId, fileName, userAvatar);
    //         } catch (error) {
    //             console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
    //         }
    //     });
    // }else {
    //     console.log("class 'product-cart-image' does not exist!!!");
    // }




    const productInShop = document.querySelectorAll('.card-img-top');
    const productDetailImg = document.getElementById('product-detail-img');
    const productCartImg = document.querySelectorAll(".product-cart-image");
    const productCheckoutImg = document.querySelectorAll(".product-checkout-image");
    const productOrderImg = document.querySelectorAll(".product-order-img");

    if (productInShop){
        productInShop.forEach(async (productImg) =>{
            const productId = productImg.getAttribute("data-product-id");
            const fileName = productImg.getAttribute("data-file-name");
            try {
                await getProductImage(productId, fileName, productImg);
            } catch (error) {
                console.error(`There was a problem with the get product image operation for product ID ${productId}:`, error);
            }
        });
    }

    if (productDetailImg){
        const productId = productDetailImg.getAttribute("data-product-id");
        const fileName = productDetailImg.getAttribute("data-file-name");
        getProductImage(productId, fileName, productDetailImg).then(r => {});
    }

    if (productCartImg){
        productCartImg.forEach(async (productCart) =>{
            const productId = productCart.getAttribute("data-product-id");
            const fileName = productCart.getAttribute("data-file-name");

            try {
                await getProductImage(productId, fileName, productCart);
            } catch (error) {
                console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
            }
        });
    }else {
        console.log("class 'product-cart-image' does not exist!!!");
    }


    if (productCheckoutImg){
        productCheckoutImg.forEach(async (productCheckout) =>{
            const productId = productCheckout.getAttribute("data-product-id");
            const fileName = productCheckout.getAttribute("data-file-name");

            try {
                await getProductImage(productId, fileName, productCheckout);
            } catch (error) {
                console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
            }
        });
    }else {
        console.log("class 'product-cart-image' does not exist!!!");
    }

    if (productOrderImg){
        productOrderImg.forEach(async (productOrder) =>{
            const productId = productOrder.getAttribute("data-product-id");
            const fileName = productOrder.getAttribute("data-file-name");

            try {
                await getProductImage(productId, fileName, productOrder);
            } catch (error) {
                console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
            }
        });
    }else {
        console.log("class 'product-cart-image' does not exist!!!");
    }


});

async function getUserAvatar(userId, fileName, typeImage){
    try{
        const response = await fetch("/api/download/userAvatar/"+userId+"/"+fileName);
        if (!response.ok){
            console.log("User avatar was not found!!!");
            return;
        }
        const blob = await response.blob();
        const avatarUrl = URL.createObjectURL(blob);
        if (typeImage){
            typeImage.src = avatarUrl;
        }

    }catch (error){
        console.error('There was a problem with the get user Avatar operation:', error);
    }
}


async function getProductImage(productId, fileName, imageType){
    try{
        const response = await fetch("/api/download/productImg/"+productId+"/"+fileName);
        if (!response.ok){
            console.log("Product Image was not found!!!");
            return;
        }
        const blob = await response.blob();
        const productImgUrl = URL.createObjectURL(blob);

        if (imageType){
            imageType.src = productImgUrl;
        }

    }catch (error){
        console.error('There was a problem with the get product image operation:', error);
    }
}