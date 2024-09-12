$(document).ready(function() {
    // updateFinalTotalAmount();
});

let selectedVoucherPercentage = 0;

function showVoucher() {
    var voucherModal = document.getElementById('voucher-modal');

    var btnCancel = document.getElementById('btn-cancel-voucher');

    voucherModal.style.display = "block";
    setTimeout(function () {
        voucherModal.classList.add("show");
    }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng

    // Ẩn modal khi click vào nút "done"
    btnCancel.addEventListener("click", function () {
        voucherModal.classList.remove("show");
        setTimeout(function () {
            voucherModal.style.display = "none";
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });

    //Ẩn modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target == voucherModal) {
            voucherModal.classList.remove("show");
            setTimeout(function () {
                voucherModal.style.display = "none";
                // studentForm.reset();
            }, 500); // Khớp với thời gian của transition
        }
    });

    //Get the voucher id when choose a voucher
    let selectedVoucherId = null;
    const voucherItems = document.querySelectorAll('.voucher-item');
    const okButton = document.getElementById('btn-voucher-ok');

    voucherItems.forEach(function (item) {
        item.addEventListener('click', function () {
            // Xóa class 'selected' từ tất cả các address-item khác
            voucherItems.forEach(function (el) {
                el.classList.remove('selected');
            });

            // Thêm class 'selected' cho address-item được click
            this.classList.add('selected');

            // Lấy ID của address được chọn
            selectedVoucherId = this.getAttribute('data-voucher-id');
            selectedVoucherPercentage = this.getAttribute('data-voucher-percent');

            // Mở khóa nút OK
            okButton.disabled = false;
        });
    });

    okButton.addEventListener('click', function () {
        if (selectedVoucherId) {
            chooseVoucher(selectedVoucherId).then(r => {});
        }

        voucherModal.classList.remove("show");
        setTimeout(function () {
            voucherModal.style.display = "none";
            // studentForm.reset();
        }, 500);
    });

}



async function chooseVoucher(selectedVoucherId){
    try{
        const response = await fetch("/api/voucher/"+selectedVoucherId);

        const voucher = await response.json();

        document.getElementById('selected-voucher').innerText = "-"+voucher.percentageIsReduced+"%";
        document.getElementById('data-voucher-id').value = voucher.id;
        // console.log("voucher id: "+optionResult.value);

        selectedVoucherPercentage = voucher.percentageIsReduced;

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


































