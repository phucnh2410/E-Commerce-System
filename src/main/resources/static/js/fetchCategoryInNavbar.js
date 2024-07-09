$(document).ready(function() {
    const categoryListElement = document.getElementById("category-list");
    showNumberOfProductInCartIcon().then(r => {});

    if (categoryListElement){
        showCategoryInNavbar(categoryListElement).then(r => {});
    }


});

async function showCategoryInNavbar(categoryElement){
    try{
        const response = await fetch("/api/categories");

        if (!response){
            console.error("Bad response");
        }
        const categories = await response.json();

        categoryElement.innerHTML = '';

        categories.forEach(category => {
           const li_Element = document.createElement('li');
           // const i_Element = document.createElement('i');
           const a_Element = document.createElement('a');

           a_Element.href = "/categories/"+category.id;
           a_Element.textContent = category.name;

           li_Element.appendChild(a_Element);

            categoryElement.appendChild(li_Element);
        });

    }catch (error) {
        console.error(`There was a problem with the show category operation in navbar:`, error);
    }
}

async function showNumberOfProductInCartIcon(){
    const cartNumberElement = document.getElementById("cart-number-badge");
    try {
        const response = await fetch("/api/cart/number");

        if (!response.ok){
            throw new Error("Cart is null!!!");
        }

        const cartNumber = await response.text();
        cartNumberElement.innerText = cartNumber;

    }catch (error) {
        console.error(`There was a problem with the show show cart number operation in navbar:`, error);
    }
}






















