$(document).ready(function() {

    // $('#btn-register').click(function (event){
    //     event.preventDefault();
    //     register().then(r => {});
    // })

    getAllOrders().then(r => {});
    getAllUsers().then(r => {
        const userImgAdminPage =  document.querySelectorAll('.user-img');

        if (userImgAdminPage){
            userImgAdminPage.forEach(async (userAvatar) =>{
                const userId = userAvatar.getAttribute("data-user-id");
                const fileName = userAvatar.getAttribute("data-file-name");

                try {
                    await getUserAvatar(userId, fileName, userAvatar);
                } catch (error) {
                    console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
                }
            });
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }
    });


    getAllCategories().then(r => {});

});

async function getAllOrders(){
    const orderFragment = document.querySelector('#orderManagement');

    try{
        const response = await fetch('/admin/orderFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const orders = await response.text();
        if (!orderFragment) {
            console.log('Element #productTable not found in the response');
        }

        orderFragment.innerHTML = "";
        orderFragment.innerHTML = orders;
    }catch (error){
        console.error('There was a problem with the fetch all orders in the Admin operation:', error);
    }
}

async function getAllUsers(){
    const accountFragment = document.querySelector('#accountTable');

    try{
        const response = await fetch('/admin/accountFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const users = await response.text();
        if (!accountFragment) {
            console.log('Element #productTable not found in the response');
        }

        accountFragment.innerHTML = "";
        accountFragment.innerHTML = users;
    }catch (error){
        console.error('There was a problem with the fetch all users in the Admin operation:', error);
    }
}

async function getAllCategories(){
    const categoryFragment = document.querySelector('#categoryTable');

    try{
        const response = await fetch('/admin/categoryFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const categories = await response.text();
        if (!categoryFragment) {
            console.log('Element #productTable not found in the response');
        }

        categoryFragment.innerHTML = "";
        categoryFragment.innerHTML = categories;
    }catch (error){
        console.error('There was a problem with the fetch all categories in the Admin operation:', error);
    }
}