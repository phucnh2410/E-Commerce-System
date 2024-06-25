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
        const response = await fetch("/api/categories", {
            method: "GET"
        });

        if (!response.ok){
            throw new Error(response.statusText);
        }

        const categories = await response.json();
        const selecElement = document.getElementById('product-category');
        selecElement.innerHTML = '';

        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            selecElement.appendChild(option);
        })
    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }
}



async function addProduct(event, csrfToken){
    event.preventDefault();

    // const product_name = document.getElementById("product-name").value;
    // const product_brand = document.getElementById("product-brand").value;
    // const product_price = document.getElementById("product-price").value;
    // const product_stock = document.getElementById("product-stock").value;
    // const product_category = document.getElementById("product-category").value;
    // const product_image = document.getElementById("product-image").value;
    // const product_description = document.getElementById("product-description").value;
    //
    // const formData ={
    //     name: product_name,
    //     brand: product_brand,
    //     price: product_price,
    //     stock: product_stock,
    //     category: {id: product_category},
    //     description: product_description,
    //     productImg: product_image
    // }

    const formElement = document.getElementById('product-form');
    const formData = new FormData(formElement);


    try{
        const response = await fetch("/api/product", {
            method: "POST",
            headers: {
                // 'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(formData)
        });

        if(response.ok){

        }else {

        }




    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }


}






















