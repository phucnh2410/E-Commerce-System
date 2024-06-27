$(document).ready(function() {

    // $('#btn-register').click(function (event){
    //     event.preventDefault();
    //     register().then(r => {});
    // })

    getCategory().then(r => {})




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



async function addProduct(event){
    event.preventDefault();

    const product_name = document.getElementById("product-name").value;
    const product_brand = document.getElementById("product-brand").value;
    const product_price = document.getElementById("product-price").value;
    const product_stock = document.getElementById("product-stock").value;

    // const product_image = document.getElementById("product-image").value;
    const product_description = document.getElementById("product-description").value;
    const categoryId = document.getElementById("category").value;

    const formData = {
        name: product_name,
        brand: product_brand,
        price: product_price,
        stock: product_stock,

        description: product_description,
        category: {id: categoryId}
        // productImg: product_image
    };

    // const formElement = document.getElementById('product-form');
    // const formData = new FormData(formElement);


    try{
        const response = await fetch("/api/product", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        try {
            const result = await response.json();//Get result from http
            const messageElement = document.getElementById('product-message');

            if (response.ok) {
                messageElement.textContent = result.message;
                messageElement.style.color = 'green';
                console.log(csrfToken);
            } else {
                messageElement.textContent = result.message;
                messageElement.style.color = 'red';
            }

        }catch (error){
            console.error('There was a problem with result:', error);
        }

    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }


}






















