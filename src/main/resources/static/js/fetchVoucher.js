$(document).ready(function() {
    // updateFinalTotalAmount();
});

let selectedVoucherPercentage = 0;

async function chooseVoucher(){
    try{
        const response = await fetch("/api/voucher/all");

        const data = await response.json();

        const inputOptions = data.reduce((options, voucher) => {
            options[voucher.id] = voucher.description;
            return options;
        }, {});

        const voucherMap = data.reduce((map, voucher) => {
            map[voucher.id] = voucher.percentageIsReduced;
            return map;
        }, {});

        const optionResult = await Swal.fire({
            title: 'Select Vouchers',
            input: 'select',
            inputOptions: inputOptions,
            // inputPlaceholder: 'Select a payment method',
            showCancelButton: true,
            inputValidator: (value) => {
                return new Promise((resolve, reject) => {
                    if (value) {
                        resolve();
                    } else {
                        reject('You need to select a voucher!');
                    }
                });
            }
        });

        if (optionResult.isConfirmed) {
            const selectedVoucher = voucherMap[optionResult.value];
            document.getElementById('selected-voucher').innerText = "-"+selectedVoucher+"%";
            // document.getElementById('selected-voucher').innerText = "-"+selectedVoucher+"%";

            selectedVoucherPercentage = selectedVoucher;
        }

        await updateFinalTotalAmount(selectedVoucherPercentage);

    }catch(error) {
        console.error('Error fetching choose voucher action:', error);
    }
}

async function updateFinalTotalAmount(numberOfPercentage){
    try {
        const getTotalAmountElement = document.getElementById("total-amount");
        const totalAmountText = getTotalAmountElement.innerText.replace(/[^\d.-]/g, '');

        const currentTotalAmount = parseFloat(totalAmountText);

        const discountAmount = currentTotalAmount * numberOfPercentage / 100;

        const finalTotal = currentTotalAmount - (currentTotalAmount * numberOfPercentage / 100);

        if (isNaN(currentTotalAmount)) {
            throw new Error('currentTotalAmount invalid number detected.');
        }

        if (isNaN(numberOfPercentage)){
            throw new Error('numberOfPercentage invalid number detected.');
        }

        document.getElementById("discount-amount").innerText = "-$"+discountAmount.toFixed(1);
        document.getElementById("payment-total").innerText = "$"+finalTotal.toFixed(1);


    }catch (error) {
        console.error('Error updating final total amount operation:', error);
    }

}


































