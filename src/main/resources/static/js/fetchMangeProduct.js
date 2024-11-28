$(document).ready(function() {

    // $('#btn-register').click(function (event){
    //     event.preventDefault();
    //     register().then(r => {});
    // })

    getCategory().then(r => {})
    getAllProductSold().then(r => {});

});

// async function getCsrfToken(){
//     try {
//         const response = await fetch("/get-csrf-token");
//         if (!response.ok){
//             throw new Error(response.statusText);
//         }
//         const csrfToken = await response.json();
//
//         return csrfToken.token;
//
//     }catch (error){
//         console.error('There was a problem with the fetch operation:', error);
//     }
// }

async function getCategory(){
    try{
        const response = await fetch("/api/categories");

        if (!response.ok){
            throw new Error(response.statusText);
        }

        const categories = await response.json();
        const selectElement = document.getElementById('category');
        selectElement.innerHTML = '';

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            selectElement.appendChild(option);
        })
    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }
}

async function getAllProductSold(){
    const ProductSoldTable = document.querySelector("#product-sold-table")
    try {
        const response = await fetch("/products/sold-fragment");
        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const products = await response.text();
        if (!ProductSoldTable) {
            console.log('Element #productTable not found in the response');
        }

        ProductSoldTable.innerHTML = "";
        ProductSoldTable.innerHTML = products;

        const productSoldImg = document.querySelectorAll(".product-sold-image");
        if (productSoldImg){
            for (const productSoldElement of productSoldImg) {
                const productId = productSoldElement.getAttribute("data-product-id");
                const fileName = productSoldElement.getAttribute("data-file-name");
                // console.log("productID: "+productId);
                // console.log("productImg: "+fileName);
                try {
                    await getProductImage(productId, fileName, productSoldElement);
                } catch (error) {
                    console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }
    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }

}

async function getAllProductManagement(){
    const productManagementTable = document.querySelector("#product-management-table");


    try {
        const response = await fetch("/products/management-fragment");
        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const products = await response.text();
        if (!productManagementTable) {
            console.log('Element #productTable not found in the response');
        }

        productManagementTable.innerHTML = "";
        productManagementTable.innerHTML = products;

        const productManagementImg = document.querySelectorAll(".product-management-image");
        if (productManagementImg){
            for (const productElement of productManagementImg) {
                const productId = productElement.getAttribute("data-product-id");
                const fileName = productElement.getAttribute("data-file-name");
                // console.log("productID: "+productId);
                // console.log("productImg: "+fileName);
                try {
                    await getProductImage(productId, fileName, productElement);
                } catch (error) {
                    console.error(`There was a problem with the get product cart image operation for product ID ${productId}:`, error);
                }
            }
        }else {
            console.log("class 'product-cart-image' does not exist!!!");
        }

    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }

}


function updateStock(productId){
    const productStockInput = document.getElementById('stock-update-product-'+productId);
    const btnShowUpdate = document.getElementById('show-update-stock-product-'+productId);
    const btnSaveStock = document.getElementById('save-stock-product-'+productId);

    productStockInput.style.display = 'block';
    btnSaveStock.style.display = 'block';
    btnShowUpdate.style.display = 'none';

    // console.log("product stock: "+productStock.value);
}

async function saveUpdateStock(productId){
    const productStockInput = document.getElementById('stock-update-product-'+productId);
    const btnShowUpdate = document.getElementById('show-update-stock-product-'+productId);
    const btnSaveStock = document.getElementById('save-stock-product-'+productId);

    const currentStock = document.getElementById('productStock-'+productId);

    console.log("stock: "+productStockInput.value);
    if (isNaN(productStockInput.value) || productStockInput.value <= 0) {
        alert("Please enter a valid stock value greater than 0.");
        return;
    }

    try{
        const response = await fetch('/api/product/update_stock/'+productId+'/'+productStockInput.value, {
            method: 'PUT'
        });

        if (!response.ok){
            const error = await response.json();
            console.error(error.error)
        }

        const result = await response.json();

        // result.currentStock;

        if (currentStock){
            currentStock.innerText = 'x'+result.currentStock;
        }


        productStockInput.style.display = 'none';
        btnSaveStock.style.display = 'none';
        btnShowUpdate.style.display = 'block';


    }catch (error){
        console.error('There was a problem with the Add product operation:', error);
    }

}

async function confirmProductForOrder(orderId, productId){
    const orderStatusElement = document.getElementById('order-status-'+orderId+'-'+productId);
    // const isConfirm = Swal.fire({
    //     title: 'Are you sure?',
    //     text: 'Do you really want to confirm this product? This action cannot be undone.',
    //     icon: 'warning',
    //     showCancelButton: true,
    //     confirmButtonColor: '#d33',
    //     cancelButtonColor: '#3085d6',
    //     confirmButtonText: 'Yes, confirm it!'
    // });


    // if (isConfirm.isConfirmed) {
        try {
            const response = await fetch('/api/product/update_product_status/'+orderId+'/'+productId, {
                method: 'PUT'
            });

            const result = await response.json();
            if (!response.ok){
                console.error(result.error);
                throw new Error(result.statusText);
            }

            Swal.fire({
                title: 'Confirmed!',
                text: result.message || 'Product status confirmed successfully!',
                icon: 'success',
                confirmButtonText: 'OK'
            });

            if (orderStatusElement){
                if (result.orderStatus) {
                    orderStatusElement.innerText = result.orderStatus;
                }
            }

        }catch (error){
            console.error('There was a problem with the update order status in the order management page operation:', error);
            Swal.fire({
                title: 'Error!',
                text: 'Something went wrong, please try again!',
                icon: 'error',
                confirmButtonText: 'Try Again'
            });
        }
    // }

}


function showProductAddModal(event){
    event.preventDefault();

    let modal = document.getElementById('addAndEditModal');
    const done = document.getElementById("btn-done-add-new");
    const messageElement = document.getElementById('product-message');

    console.log("Add New button clicked");
    modal.style.display = "block";
    setTimeout(function () {
        modal.classList.add("show");
    }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng


    // Ẩn modal khi click vào nút "done"
    done.addEventListener("click", function () {
        modal.classList.remove("show");
        modal.style.display = "none";
        setTimeout(function () {
            messageElement.textContent = '';
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });
}

function showRequestCategoryForm(event){
    let modal = document.getElementById("requestCategoryModal");
    const done = document.getElementById("btn-done-request-category");
    // var studentForm = document.getElementById("studentForm");

    event.preventDefault();
    console.log("Request New Category button clicked");
    modal.style.display = "block";
    setTimeout(function () {
        modal.classList.add("show");
    }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng


    // Ẩn modal khi click vào nút "done"
    done.addEventListener("click", function () {
        modal.classList.remove("show");
        setTimeout(function () {
            modal.style.display = "none";
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });
}


async function saveProduct(event){
    event.preventDefault();

    //Input element
    const productId = document.getElementById("product-id");
    const productName = document.getElementById("product-name");
    const productBrand = document.getElementById("product-brand");
    const productPrice = document.getElementById("product-price");
    const productStock = document.getElementById("product-stock");
    const productDescription = document.getElementById("product-description");
    const productFile = document.getElementById("product-file").files[0];
    const categoryId = document.getElementById("category");

    //Message element
    const messageElement = document.getElementById('product-message');

    function isValidString(value){
        return /^[a-zA-Z\s]+$/.test(value);
    }

    function isValidNameAndDescription(value){
        const hasLetters = /[a-zA-Z]/.test(value);
        const hasNumbers = /[0-9]/.test(value);
        return (hasLetters || hasNumbers) || (hasLetters && hasNumbers);
    }

    function isValidInteger(value){
        return /^[1-9]\d*$/.test(value);
    }

    function isValidImage(file) {
        const validExtensions = ['jpg', 'jpeg', 'png', 'gif', 'webp'];
        if (file) {
            const extension = file.name.split('.').pop().toLowerCase();
            return validExtensions.includes(extension);
        }
        return false;
    }

    //check empty
    if (!productName.value){
        messageElement.textContent = "Please enter the Name of product";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidNameAndDescription(productName.value)) {
        messageElement.textContent = "Product Name must be a string";
        messageElement.style.color = 'red';
        return;
    }

    //Brand
    if (!productBrand.value){
        messageElement.textContent = "Please enter the Brand of product";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidString(productBrand.value)) {
        messageElement.textContent = "Product Brand must be a string";
        messageElement.style.color = 'red';
        return;
    }

    if (!productPrice.value){
        messageElement.textContent = "Please enter the Price of product";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidInteger(productPrice.value)) {
        messageElement.textContent = "Product Price must be a positive number";
        messageElement.style.color = 'red';
        return;
    }

    if (!productStock.value){
        messageElement.textContent = "Please enter the Number of product";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidInteger(productStock.value)) {
        messageElement.textContent = "Number of product must be a positive number";
        messageElement.style.color = 'red';
        return;
    }

    if (!productFile){
        messageElement.textContent = "Please choose the Image of product";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidImage(productFile)) {
        messageElement.textContent = "Please select a valid image file (jpg, jpeg, png, gif, webp)";
        messageElement.style.color = 'red';
        return;
    }

    //Description
    if (!productDescription.value){
        messageElement.textContent = "Please enter the Description of product";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidNameAndDescription(productDescription.value)) {
        messageElement.textContent = "Product Description must be a string";
        messageElement.style.color = 'red';
        return;
    }

    // Create a product object
    const productObject = {
        id: productId.value,
        name: productName.value,
        brand: productBrand.value,
        price: parseFloat(productPrice.value),
        stock: parseInt(productStock.value, 10),
        description: productDescription.value,
        category: { id: categoryId.value }
    };

    //Create a form
    const formData = new FormData();
    formData.append("product", new Blob([JSON.stringify(productObject)], {type: 'application/json'}));
    formData.append("product-file", productFile);

    try{
        const response = await fetch("/api/product", {
            method: "POST",
            body: formData
        });

        try {
            const result = await response.json();//Get result from http

            if (response.ok) {
                messageElement.textContent = result.message;
                messageElement.style.color = 'green';

                productName.value = '';
                productBrand.value = '';
                productPrice.value = '';
                productStock.value = '';
                productDescription.value = '';

                await getAllProductManagement();
            } else {
                messageElement.textContent = result.message;
                messageElement.style.color = 'red';
            }

        }catch (error){
            console.error('There was a problem with result:', error);
        }

    }catch (error){
        console.error('There was a problem with the Add product operation:', error);
    }
}

async function showFormUpdate(id){
    //Modals
    const modal = document.getElementById("addAndEditModal");
    const done = document.getElementById('btn-done-add-new');

    //Input tags
    const productId = document.getElementById("product-id");
    const productName = document.getElementById("product-name");
    const productBrand = document.getElementById("product-brand");
    const productPrice = document.getElementById("product-price");
    const productStock = document.getElementById("product-stock");
    const productDescription = document.getElementById("product-description");

    try{
        const response = await fetch("/api/product/"+id);
        if (!response.ok){
            console.error("Product does not exist in show form edit!!!");
            return;
        }

        const productResponse = await response.json();

        modal.style.display = "block";
        setTimeout(function () {
            modal.classList.add("show");
        }, 10);

        productId.value = productResponse.id;
        productName.value = productResponse.name;
        productBrand.value = productResponse.brand;
        productPrice.value = productResponse.price;
        productStock.value = productResponse.stock;
        productDescription.value = productResponse.description;

    }catch (error){
        console.error('There was a problem with the show form edit product operation:', error);
    }
}

async function deleteProduct(productId){
    try{
        const response = await fetch('/api/product/'+productId, {
                method: "DELETE"
        });
        if (!response.ok){
            const errorMessage = await response.text();
            throw new Error(`Failed to delete product: ${errorMessage}`);
        }

        Swal.fire(
            'Deleted successful!'
        );

        await getAllProductManagement();
    }catch (error){
        console.error('There was a problem with the Delete Product operation:', error);
        Swal.fire(
            'Failed to delete!',
            'There was an error deleting the product.'
        );
    }
}


function showDeleteConfirmation(productId) {
    if (typeof Swal === 'undefined') {
        console.error('Swal is not defined. Ensure that SweetAlert2 is loaded correctly.');
        return;
    }

    Swal.fire({
        title: 'Do you want to delete it?',
        // text: "Bạn sẽ không thể hoàn nguyên hành động này!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            // Thực hiện hành động xóa, ví dụ như gửi yêu cầu xóa đến server
            deleteProduct(productId).then(r => {});
        }
    });
}





























