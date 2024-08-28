$(document).ready(function() {

});

//Khi seller nhập category mới thì bắt đầu lưu lại với status là preparing
async function categoryRequest(sellerId, event) {
    event.preventDefault();

    const categoryName = document.getElementById("category-name");
    const loader = document.querySelector('.loader');
    //Message element
    const categoryMessage = document.querySelector('.category-message');
    const messageElement = document.getElementById('category-message');

    function isValidNameAndDescription(value){
        const hasLetters = /[a-zA-Z]/.test(value);
        const hasNumbers = /[0-9]/.test(value);
        return (hasLetters || hasNumbers) || (hasLetters && hasNumbers);
    }

    //check empty
    if (!categoryName.value){
        messageElement.textContent = "Please enter the Name of category";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidNameAndDescription(categoryName.value)) {
        messageElement.textContent = "Category Name must be a string";
        messageElement.style.color = 'red';
        return;
    }

    //create a category object
    const categoryObject = {
        name: categoryName.value
    }

    try{
        loader.style.display = "block";

        const response = await fetch("/api/categories/add/"+sellerId, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(categoryObject)
        });

        const result = await response.json();

        if (response.ok) {
            messageElement.textContent = result.message;
            messageElement.style.color = 'green';

            //show message
            categoryMessage.style.display = "block";

            // Hide loader
            loader.style.display = "none";

            categoryName.value = '';
        }else {
            messageElement.textContent = result.message;
            messageElement.style.color = 'red';
        }
    }catch (error) {
        console.error('There is a error with the request a new category:', error);
    }

}

async function updateCategoryStatus(categoryId, status){
    const messageElement = document.getElementById('category-status-message');

    try {
        const response = await fetch("/api/categories/update_status/"+categoryId+"/"+status, {
            method: "PUT",
            headers: {"Content-Type": "application/json"}
        });

        const result = await response.json();

        if (response.ok) {
            messageElement.textContent = result.message;
            messageElement.style.color = 'green';

            await getAllCategories();

            const categoryListElement = document.getElementById("category-list");
            await showCategoryInNavbar(categoryListElement);
        }else {
            messageElement.textContent = result.message;
            messageElement.style.color = 'red';
        }


    }catch (error) {
        console.error('There is a error with the update the category status:', error);
    }


}

//Gửi đến admin 1 new category

//trang admin sẽ có nút bấm vào và 1 popup sẽ hiển ra những category mới được đề xuất để admin có thể approve hoặc reject

//approve thì sẽ gọi hàm lưu category mới

//reject thì sẽ xoá new category mới đó