$(document).ready(function() {

    // $('#btn-register').click(function (event){
    //     event.preventDefault();
    //     register().then(r => {});
    // })

    getCategory().then(r => {})
    getAllProduct().then(r => {})




});

async function getCsrfToken(){
    try {
        const response = await fetch("/get-csrf-token");
        if (!response.ok){
            throw new Error(response.statusText);
        }
        const csrfToken = await response.json();

        return csrfToken.token;

    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }
}

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

async function getAllProduct(){
    const tbody = document.querySelector("#productTable")
    try {
        const response = await fetch("/products/fragment");
        if (!response.ok) {
            throw new Error(response.statusText);
        }

        const products = await response.text();
        if (!tbody) {
            console.log('Element #productTable not found in the response');
        }

        tbody.innerHTML = "";
        tbody.innerHTML = products;
    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }

}


async function saveProduct(event){
    event.preventDefault();
    const formProduct = document.getElementById("");

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

    //check empty
    if (!productName.value){
        messageElement.textContent = "Please enter the Name of product";
        messageElement.style.color = 'red';
        return;
    }if (!productBrand.value){
        messageElement.textContent = "Please enter the Brand of product";
        messageElement.style.color = 'red';
        return;
    }if (!productPrice.value){
        messageElement.textContent = "Please enter the Price of product";
        messageElement.style.color = 'red';
        return;
    }if (!productStock.value){
        messageElement.textContent = "Please enter the Number of product";
        messageElement.style.color = 'red';
        return;
    }if (!productFile){
        messageElement.textContent = "Please the Image of product";
        messageElement.style.color = 'red';
        return;
    }if (!productDescription.value){
        messageElement.textContent = "Please enter the Description of product";
        messageElement.style.color = 'red';
        return;
    }

    // Create a product object
    const productObject = {
        id: productId.value,
        name: productName.value,
        brand: productBrand.value,
        price: productPrice.value,
        stock: productStock.value,
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

                await getAllProduct();
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
    const modal = document.getElementById("myModal");
    const done = document.getElementsByClassName("btn-done")[0];
    const studentForm = document.getElementById("studentForm");

    const editButtons = document.querySelectorAll(".btn-edit");

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

        //Show data just have fetched to input tags
        editButtons.forEach(function (editButton){
            editButton.addEventListener("click", function (){
                modal.style.display = "block";
                setTimeout(function () {
                    modal.classList.add("show");
                }, 10);
            });
        });

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

        await getAllProduct();
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





























