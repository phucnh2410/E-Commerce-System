$(document).ready(function() {

});

function showPaymentMethod() {
    const paymentModal = document.getElementById('payment-modal');

    const btnCancel = document.getElementById('btn-cancel-payment');

    paymentModal.style.display = "block";
    setTimeout(function () {
        paymentModal.classList.add("show");
    }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng

    // Ẩn modal khi click vào nút "done"
    btnCancel.addEventListener("click", function () {
        paymentModal.classList.remove("show");
        setTimeout(function () {
            paymentModal.style.display = "none";
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });


    //Get the voucher id when choose a voucher
    let selectedPaymentId = null;
    const paymentItems = document.querySelectorAll('.payment-item');
    const okButton = document.getElementById('btn-payment-ok');

    paymentItems.forEach(function (item) {
        item.addEventListener('click', function () {
            // Xóa class 'selected' từ tất cả các address-item khác
            paymentItems.forEach(function (el) {
                el.classList.remove('selected');
            });

            // Thêm class 'selected' cho address-item được click
            this.classList.add('selected');

            // Lấy ID của address được chọn
            selectedPaymentId = this.getAttribute('data-payment-id');

            // Mở khóa nút OK
            okButton.disabled = false;
        });
    });

    okButton.addEventListener('click', function () {
        if (selectedPaymentId) {
            changePaymentMethod(selectedPaymentId).then(r => {});
        }

        paymentModal.classList.remove("show");
        setTimeout(function () {
            paymentModal.style.display = "none";
            // studentForm.reset();
        }, 500);
    });

}

async function changePaymentMethod(selectedPaymentId){

    try {
        const response = await fetch("/api/payment/"+selectedPaymentId);
        const paymentMethod = await response.json();

        document.getElementById('selected-payment-method').innerText = paymentMethod.name;
        document.getElementById('data-payment-id').value = paymentMethod.id;

    }catch (error) {
        console.error('Error fetching payment methods:', error);
    }
}

