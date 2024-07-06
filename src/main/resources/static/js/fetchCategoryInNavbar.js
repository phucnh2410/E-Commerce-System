$(document).ready(function() {
    const categoryListElement = document.getElementById("category-list");

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
        console.error(`There was a problem with the show category operation for navbar:`, error);
    }
}
