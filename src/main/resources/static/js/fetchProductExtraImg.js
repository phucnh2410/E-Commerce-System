$(document).ready(function() {
    // updateFinalTotalAmount();
});


function isValidImage(file) {
    const validExtensions = ['jpg', 'jpeg', 'png', 'gif', 'webp'];
    if (file) {
        const extension = file.name.split('.').pop().toLowerCase();
        return validExtensions.includes(extension);
    }
    return false;
}


function showFormAddMoreProductImg(){
    var modal = document.getElementById("extraImgModal");
    var done = document.getElementsByClassName("more-img-btn-done")[0];

    modal.style.display = "block";
    setTimeout(function () {
        modal.classList.add("show");
    }, 10);

    // Ẩn modal khi click vào nút "done"
    done.addEventListener("click", function () {
        modal.classList.remove("show");
        modal.style.display = "none";
        setTimeout(function () {
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });

}

async function addMoreProductImg(event){
    event.preventDefault();

    const productIdElement = document.getElementById('product-id');
    const messageElement = document.getElementById('product-extra-form-message');

    const productImgFileInput = document.getElementById("product-extra-img-file");


    const listImgFiles = productImgFileInput.files;
    if (listImgFiles.length <= 0){
        messageElement.textContent = "Please choose the Image of product";
        messageElement.style.color = 'red';
        return;
    }

    const form = new FormData();
    for (let i = 0; i < listImgFiles.length; i++){
        form.append("list-product-img-files", listImgFiles[i]);
    }

    try{
        const response = await fetch('/api/extra_img/save/'+productIdElement.value, {
            method: 'POST',
            body: form
        });

        if (!response.ok){
            console.error('There was a problem with result:', await response.text());
            messageElement.textContent = 'Error saving images';
            messageElement.style.color = 'red';
            return;
        }

        const result = await response.json();

        messageElement.textContent = result.message;
        messageElement.style.color = 'green';

    }catch(error) {
        console.error('Error adding more product images:', error);
        messageElement.textContent = 'Error adding images';
        messageElement.style.color = 'red';
    }


}




































