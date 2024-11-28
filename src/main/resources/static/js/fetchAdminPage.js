$(document).ready(function() {
    const adminTabLink = document.querySelector('.tab-link');
    if (adminTabLink){
        getAllOrders().then(r => {});
    }
});

let debounceTimeout;
async function searchOrderById(){
    clearTimeout(debounceTimeout)
    debounceTimeout = setTimeout(async () => {
        const orderTableFragment = document.querySelector('#orderManagement');
        const searchOrderInputElement = document.querySelector('#search-order-input');
        const messageElement = document.getElementById('message-order-searching');

        function isValidInteger(value){
            return /^[1-9]\d*$/.test(value);
        }

        if (!searchOrderInputElement.value) {
            // messageElementBody.textContent = "";
            messageElement.textContent = "";
            await getAllOrders();
            return
        }

        if (!isValidInteger(searchOrderInputElement.value)){
            messageElement.textContent = "Please enter number to search orders";
            messageElement.style.color = 'red';
            return;
        }

        try {
            const response = await fetch('/admin/orderFragment/' + searchOrderInputElement.value);

            if (!response.ok) {
                throw new Error(response.statusText);
            }

            const orders = await response.text();

            if (!orderTableFragment) {
                console.log('Element #orderTableFragment not found in the response');
                return;
            }

            // messageElement.textContent = ""; // Xóa thông báo nếu tìm thấy đơn hàng
            orderTableFragment.innerHTML = "";
            orderTableFragment.innerHTML = orders;
        } catch (error) {
            console.error('There was a problem with the fetch orders by id in the Admin operation:', error);
        }
    } , 300); // wait 300ms after the user stop to enter, the searching will be handled
}

async function getAllOrders(){
    const orderFragment = document.querySelector('#orderManagement');
    console.log("All Order already to show");

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

async function getVoucherByStatus(status){
    console.log("Voucher already to show");
    let voucherFragment = null;
    if (status === 'New'){
        voucherFragment = document.querySelector('#new-voucher-body');
    }else if (status === 'Published'){
        voucherFragment = document.querySelector('#published-voucher-body');
    }

    try{
        const response = await fetch('/admin/voucherFragment/'+status);

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const vouchers = await response.text();
        if (!voucherFragment) {
            console.log('Element #voucherFragment not found in the response');
        }

        voucherFragment.innerHTML = "";
        voucherFragment.innerHTML = vouchers;
    }catch (error){
        console.error('There was a problem with the fetch all orders in the Admin operation:', error);
    }
}

async function getAllUsers(){
    const accountFragment = document.querySelector('#accountTable');
    console.log("All Users already to show");

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
    console.log("All Category already to show");

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

/////////////////////////Seller ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
async function getAllSeller(){
    const sellerFragment = document.querySelector('#seller-body');
    console.log("Seller statistics already to show");

    try{
        const response = await fetch('/admin/sellerFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const sellers = await response.text();
        if (!sellerFragment) {
            console.log('Element #productTable not found in the response');
        }
        sellerFragment.innerHTML = "";
        sellerFragment.innerHTML = sellers;
    }catch (error){
        console.error('There was a problem with the fetch all sellers in the Admin operation:', error);
    }
}

/////////////////////////Customer ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
async function getAllCustomer(){
    const customerFragment = document.querySelector('#customer-body');
    console.log("Customer statistics already to show");

    try{
        const response = await fetch('/admin/customerFragment');

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const customers = await response.text();
        if (!customerFragment) {
            console.log('Element #productTable not found in the response');
        }
        customerFragment.innerHTML = "";
        customerFragment.innerHTML = customers;
    }catch (error){
        console.error('There was a problem with the fetch all customers in the Admin operation:', error);
    }
}

async function getAllStatisticCategory(){
    const categoryFragment = document.querySelector('#category-body');
    console.log("Category statistics already to show");

    try{
        const response = await fetch('/admin/categoryStatisticFragment');

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
        console.error('There was a problem with the fetch all customers in the Admin operation:', error);
    }
}


