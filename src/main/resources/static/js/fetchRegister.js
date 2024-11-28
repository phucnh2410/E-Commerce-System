$(document).ready(function() {

});

async function getCsrfToken(){
    try {
        const response = await fetch("/get-csrf-token");
        if (response.ok){
            console.log(response);
        }

    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }
}

// async function getRole(event){
//     try{
//         const response = await fetch("/api/register", {
//             method: "GET"
//             });
//         if (!response.ok) {
//             throw new Error('Network response was not ok ' + response.statusText);
//         }
//
//         const roles = await response.json();
//         const selectElement = document.getElementById('role');
//         selectElement.innerHTML = '';
//         // console.log(roles);
//
//         roles.forEach(role => {
//             const option = document.createElement('option');
//             option.value = role.id; // Assuming role has an 'id' property
//             option.textContent = role.name; // Assuming role has a 'name' property
//             selectElement.appendChild(option);
//         });
//
//     }catch (error){
//         console.error('There was a problem with the fetch operation:', error);
//     }
// }

async function register(event){
    event.preventDefault();

    const fullName = document.getElementById("name");
    const email = document.getElementById("email");
    const password = document.getElementById("password");
    const roleId = document.getElementById("role");

    const messageElement = document.getElementById('register-message');

    // Helper function to check if a string contains only letters and spaces
    function isValidString(value) {
        return /^[a-zA-Z\s]+$/.test(value);
    }

// Helper function to validate email format
    function isValidEmail(value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(value);
    }

// Helper function to validate password
    function isValidPassword(value) {
        // Example: At least 8 characters long, including at least one letter and one number
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        return passwordRegex.test(value);
    }


    if (!fullName.value){
        messageElement.textContent = "Please enter the your full name";
        messageElement.style.color = 'red';
        return;
    }
    //
    // else if (!isValidString(fullName.value)) {
    //     messageElement.textContent = "Your full name must be a string";
    //     messageElement.style.color = 'red';
    //     return;
    // }

    if (!email.value){
        messageElement.textContent = "Please enter the your email";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidEmail(email.value)) {
        messageElement.textContent = "Please enter a valid email address";
        messageElement.style.color = 'red';
        return;
    }

    //password
    if (!password.value){
        messageElement.textContent = "Please enter the your Password";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidPassword(password.value)) {
        messageElement.textContent = "Your Password must be at least 8 characters long and include at least one letter and one number";
        messageElement.style.color = 'red';
        return;
    }



    const formData = {
        fullName: fullName.value,
        email: email.value,
        password: password.value,
        role: { id: 2}
    };

    try{
        const response = await fetch("/api/register", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
                // 'Accept': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        try{
            const result = await response.json();//Get result from http


            //Successfully
            if (response.ok){
                messageElement.textContent = result.message;
                messageElement.style.color = 'green';
                console.log("Successfully!!!", result.user);
            }else{
                //Unsuccessfully
                messageElement.textContent = result.message;
                messageElement.style.color = 'red';
                console.log("Unsuccessfully!!!");
            }
        }catch (error){
            console.error('Cannot handle the json!!!:', error);
        }



    }catch (error){
        console.error('There was a problem with the fetch operation!!!:', error);
    }
}

async function forgotPassword(){

}