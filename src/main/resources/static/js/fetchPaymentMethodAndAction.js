$(document).ready(function() {

});

async function changePaymentMethod(){

    try {
        const response = await fetch("/api/payment/all");
        const data = await response.json();
        const inputOptions = data.reduce((options, method) => {
            options[method.id] = method.name;
            return options;
        }, {});


        const result = await Swal.fire({
            title: 'Select Payment Method',
            input: 'select',
            inputOptions: inputOptions,
            // inputPlaceholder: 'Select a payment method',
            showCancelButton: true,
            inputValidator: (value) => {
                return new Promise((resolve, reject) => {
                    if (value) {
                        resolve();
                    } else {
                        reject('You need to select a payment method!');
                    }
                });
            }
        });

        if (result.isConfirmed) {
            const selectedMethod = inputOptions[result.value];
            document.getElementById('selected-payment-method').innerText = selectedMethod;
        }
    }catch (error) {
        console.error('Error fetching payment methods:', error);
    }
}

