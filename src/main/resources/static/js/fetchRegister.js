$(document).ready(function() {

    // $('#btn-register').click(function (event){
    //     event.preventDefault();
    //     register().then(r => {});
    // })

    getRole().then(r => {})




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

async function getRole(event){
    try{
        const response = await fetch("/api/register", {
            method: "GET"
            });
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }

        const roles = await response.json();
        const selectElement = document.getElementById('role');
        selectElement.innerHTML = '';
        console.log(roles);

        roles.forEach(role => {
            const option = document.createElement('option');
            option.value = role.id; // Assuming role has an 'id' property
            option.textContent = role.name; // Assuming role has a 'name' property
            selectElement.appendChild(option);
        });

    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }
}

async function register(event){
    event.preventDefault();

    const fullName = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const roleId = document.getElementById("role").value;

    const formData = {
        fullName: fullName,
        email: email,
        password: password,
        role: { id: roleId}
    };

    console.log("Form data: "+formData.fullName);
    console.log("Form data: "+formData.email);
    console.log("Form data: "+formData.password);
    console.log("Form data: "+formData.role);

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
            const messageElement = document.getElementById('message');

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