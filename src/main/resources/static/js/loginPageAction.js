$(document).ready(function() {
    // const byId = (id) =>{
    //     return document.getElementById(id)
    // }

    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const forgotPassword = document.getElementById('forgot-password');
    const returnLogin = document.getElementById('return-login');
    const verifyEmail = document.getElementById('btn-verify-email');

    const container = document.getElementById('container');


    signUpButton.addEventListener(
        'click',
        ()=> {
            container.classList.add('right-panel-active');
            container.classList.remove('left-panel-active');
        },
    );

    signInButton.addEventListener(
        'click',
        ()=> {
            container.classList.remove('right-panel-active');
            container.classList.add('left-panel-active');
        },
    );

    forgotPassword.addEventListener(
        'click',
        ()=> {
            container.classList.add('forgotPassword-panel-active');
            container.classList.remove('left-panel-active');
        },
    );

    returnLogin.addEventListener(
        'click',
        ()=> {
            container.classList.remove('forgotPassword-panel-active');
            container.classList.add('left-panel-active');
            const verifyEmailAndCodeBtn = document.getElementById('btn-verify-email-and-code');
            const verifyEmailBtn = document.getElementById('btn-verify-email');
            const codeToVerifyInput = document.getElementById('code-to-verify');
            const messageElement = document.getElementById('forgot-password-message');

            messageElement.textContent = '';
            codeToVerifyInput.type = 'hidden';
            verifyEmailAndCodeBtn.style.display = 'none';
            verifyEmailBtn.style.display = 'block';
        },
    );

    //Khi bấm nút verify email thì 1 hiệu ứng load sẽ hiện ra trong quá trình gửi email
    //sau khi email đã được gửi thì ẩn hieeuj ứng load đi và chuyển input code từ 'hidden' sang 'text' để hiện ra, và display = 'block' cho button "Verify"


    verifyEmail.addEventListener(
        'click',
        (event)=> {
            event.preventDefault();
            const emailInput = document.getElementById('email-to-verify');
            const messageElement = document.getElementById('forgot-password-message');


            if (!emailInput.value){
                messageElement.textContent = 'Please enter your email to verify!!!';
                messageElement.style.color = 'red';
                return;
            }

            sendEmail(emailInput.value).then(r => {});
            messageElement.textContent = '';
        },
    );





});

async function sendEmail(email){
    const messageElement = document.getElementById('forgot-password-message');
    const loaderElement = document.getElementById('loader');

    console.log(email);
    try {
        loaderElement.style.display = 'block';
        messageElement.style.display = 'none';
        const response = await fetch('/api/register/send-OTP-by-email/' + email, {
            method: 'POST'
        });

        const result = await response.json();

        if (!response.ok) {
            messageElement.textContent = result.message;
            messageElement.style.color = 'red';
            loaderElement.style.display = 'none';
            return;
        }

        messageElement.textContent = result.message;
        messageElement.style.color = 'green';
        messageElement.style.display = 'block';
        loaderElement.style.display = 'none';

        const codeInputElement = document.getElementById('code-to-verify');
        const emailInputElement = document.getElementById('email-to-verify');
        const verifyEmailCodeBtn = document.getElementById('btn-verify-email-and-code');
        const verifyEmailBtn = document.getElementById('btn-verify-email');

        codeInputElement.type = 'text';
        verifyEmailCodeBtn.style.display = 'block'
        emailInputElement.readOnly = true;
        verifyEmailBtn.style.display = 'none'

    }catch (error){
        console.error('There was a problem with the fetch forgot password!!!:', error);
    }
}

async function verifyEmailAndOTP(event){
    event.preventDefault();
    const messageElement = document.getElementById('forgot-password-message');
    const loaderElement = document.getElementById('loader');

    const otpInputElement = document.getElementById('code-to-verify');
    const emailInputElement = document.getElementById('email-to-verify');

    console.log(emailInputElement.value);
    console.log(otpInputElement.value);

    if (!otpInputElement.value){
        messageElement.textContent = 'Please enter the OTP to verify!!!';
        messageElement.style.color = 'red';
        return;
    }
    try {
        loaderElement.style.display = 'block';
        const response = await fetch('/api/register/verify-OTP/'+ otpInputElement.value +'/'+ emailInputElement.value, {
            method: 'POST'
        });

        const data = await response.json();

        if (!response.ok) {
            messageElement.textContent = data.message;
            messageElement.style.color = 'red';
            loaderElement.style.display = 'none';
            return;
        }

        window.location.href = "/change-password-form";

    }catch (error){
        console.error('There was a problem with the fetch forgot password!!!:', error);
    }
}

async function saveChangePassword(event){
    event.preventDefault();
    const newPassword = document.getElementById('new-password');
    const confirmPassword = document.getElementById('confirm-password');
    const messageElement = document.getElementById('change-password-message');

    function isValidPassword(value) {
        // Example: At least 8 characters long, including at least one letter and one number
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        return passwordRegex.test(value);
    }

    if (!newPassword.value) {
        messageElement.textContent = 'Please enter the new to change!';
        messageElement.style.color = 'red';
        return;
    }else if (!isValidPassword(newPassword.value)) {
        messageElement.textContent = "New password must be at least 8 characters long and include at least one letter and one number";
        messageElement.style.color = 'red';
        return;
    }

    if (confirmPassword.value != newPassword.value){
        messageElement.textContent = 'Confirm password does not match, Please enter again!';
        messageElement.style.color = 'red';
        return;
    }

    try {
        const response = await fetch('/api/register/save-change-password/' + newPassword.value, {
            method: 'POST'
        });

        const data = await response.json();

        if (!response.ok) {
            messageElement.textContent = data.message;
            messageElement.style.color = 'red';
            return;
        }

        console.log("user" +data.user);

        window.location.href = "/login";

    } catch (error) {
        console.error('There was a problem with the fetch forgot password!!!:', error);
    }

}






















