document.addEventListener('DOMContentLoaded', function () {
    console.log("DOM Content Loaded");
    getUserAuthentication();
});
async function getUserAuthentication(){
    const fullName = document.getElementById('fullName');

    try{
        const response = await fetch("/api/get-authentication");

        if (!response.ok){
            console.log("User is null!!!");
            return;
        }

        const user = await response.json();
        fullName.innerText = user.fullName;
    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }

}