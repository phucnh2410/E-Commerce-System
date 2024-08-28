// $(document).ready(function() {
//     const productPendingOrderImg = document.querySelectorAll(".product-pending-order-img");
//
//     if (productPendingOrderImg){
//         console.log("productPendingOrderImg was found in loadImg File!!! with length is: "+productPendingOrderImg.length);
//         productPendingOrderImg.forEach(async (productPendingOrder) =>{
//             console.log("productPendingOrderImg foreach was found!!!");
//             const productId = productPendingOrder.getAttribute("data-product-id");
//             const fileName = productPendingOrder.getAttribute("data-file-name");
//             console.log("productID: "+productId);
//             console.log("productImg: "+fileName);
//
//             try {
//                 await getProductImage(productId, fileName, productPendingOrder);
//                 console.log("productPendingOrderImg try catch was found!!!");
//             } catch (error) {
//                 console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
//             }
//         });
//     }else {
//         console.log("class 'product-cart-image' does not exist!!!");
//     }
//
//
// });

// async function getUserAvatar(userId, fileName, typeImage){
//     try{
//         const response = await fetch("/api/download/userAvatar/"+userId+"/"+fileName);
//         if (!response.ok){
//             console.log("User avatar was not found!!!");
//             return;
//         }
//         const blob = await response.blob();
//         const avatarUrl = URL.createObjectURL(blob);
//         if (typeImage){
//             typeImage.src = avatarUrl;
//         }
//
//     }catch (error){
//         console.error('There was a problem with the get user Avatar operation:', error);
//     }
// }


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