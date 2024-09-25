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

function showFormAddVoucher(){
    // var addButton = document.getElementById("add-voucher-btn");
    // var editButtons = document.querySelectorAll(".btn-edit");
    var modal = document.getElementById("voucher-modal");
    var done = document.getElementsByClassName("voucher-done-btn")[0];
    const messageElement = document.getElementById('voucher-message');

    modal.style.display = "block";
    setTimeout(function () {
        modal.classList.add("show");
    }, 10);

    // Ẩn modal khi click vào nút "done"
    done.addEventListener("click", function () {
        modal.classList.remove("show");
        modal.style.display = "none";
        setTimeout(function () {
            messageElement.textContent = '';
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });

    // // Ẩn modal khi click ra ngoài modal
    // window.addEventListener("click", function (event) {
    //     if (event.target == modal) {
    //         modal.classList.remove("show");
    //         setTimeout(function () {
    //             modal.style.display = "none";
    //             // studentForm.reset();
    //         }, 500); // Khớp với thời gian của transition
    //     }
    // });
}

async function publishAllVoucher(){
    const confirm = await Swal.fire({
        title: 'Do you want to publish all voucher?',
        // text: "Bạn sẽ không thể hoàn nguyên hành động này!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, publish all!'
    });

    if (confirm.isConfirmed){//if user comfirm delete
        try{
            const response = await fetch('/api/voucher/publish_all',{
                method: 'PUT'
            });

            const result = await response.json();

            if (!response.ok){
                const errorMessage = result.message;
                throw new Error(`Failed to publish all voucher: ${errorMessage}`);
            }

            Swal.fire(
                'Published successful!'
            );
            await getVoucherByStatus('New');
        }catch (error) {
            console.error('Error with publish all voucher operation:', error);
        }
    }


}

async function publishVoucherById(voucherId){
    const confirm = await Swal.fire({
        title: 'Do you want to publish this voucher?',
        // text: "Bạn sẽ không thể hoàn nguyên hành động này!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, publish it!'
    });

    if (confirm.isConfirmed){//if user comfirm delete
        try{
            const response = await fetch('/api/voucher/publish/'+voucherId,{
                method: 'PUT'
            });

            const result = await response.json();

            if (!response.ok){
                const errorMessage = result.message;
                throw new Error(`Failed to publish all voucher: ${errorMessage}`);
            }

            Swal.fire(
                'Published successful!'
            );
            await getVoucherByStatus('New');
        }catch (error) {
            console.error('Error with publish all voucher operation:', error);
        }
    }
}

async function addNewVoucher(event){
    event.preventDefault();

    const id = document.getElementById('voucher-id');
    const voucher_status = document.getElementById('voucher-status');
    const description = document.getElementById('voucher-description');
    const quantity = document.getElementById('voucher-quantity');
    const discount = document.getElementById('voucher-discount');
    const customerType = document.getElementById('voucher-customer-type');
    const startDate = document.getElementById('voucher-start-date');
    const expiryDate = document.getElementById('voucher-expiry-date');

    //Message element
    const messageElement = document.getElementById('voucher-message');

    function isValidNameAndDescription(value){
        const hasLetters = /[a-zA-Z]/.test(value);
        const hasNumbers = /[0-9]/.test(value);
        return (hasLetters || hasNumbers) || (hasLetters && hasNumbers);
    }

    function isValidInteger(value){
        return /^[1-9]\d*$/.test(value);
    }

    function isValidDate(startDate, expiryDate) {
        // Chuyển đổi giá trị từ các input thành đối tượng Date
        const start = new Date(startDate);
        const expiry = new Date(expiryDate);

        // Kiểm tra tính hợp lệ của ngày
        const isStartValid = !isNaN(start.getTime()); // Kiểm tra ngày bắt đầu có hợp lệ không
        const isExpiryValid = !isNaN(expiry.getTime()); // Kiểm tra ngày hết hạn có hợp lệ không

        // Kiểm tra xem ngày bắt đầu có trước ngày hết hạn không
        const isDateOrderValid = start < expiry;

        return isStartValid && isExpiryValid && isDateOrderValid;
    }

    //check empty
    if (!description.value){
        messageElement.textContent = "Please enter the Description of voucher";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidNameAndDescription(description.value)) {
        messageElement.textContent = "Description must be a string";
        messageElement.style.color = 'red';
        return;
    }

    //Brand
    if (!quantity.value){
        messageElement.textContent = "Please enter the Quantity of voucher";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidInteger(quantity.value)) {
        messageElement.textContent = "Quantity must be a number";
        messageElement.style.color = 'red';
        return;
    }

    //Brand
    if (!discount.value){
        messageElement.textContent = "Please enter the Discount of voucher";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidInteger(discount.value)) {
        messageElement.textContent = "Discount must be a number";
        messageElement.style.color = 'red';
        return;
    }

    //Brand
    if (!startDate.value){
        messageElement.textContent = "Please choose the date to start of voucher";
        messageElement.style.color = 'red';
        return;
    } else if (!expiryDate.value){
        messageElement.textContent = "Please choose the date to end of voucher";
        messageElement.style.color = 'red';
        return;
    }else if (!isValidDate(startDate.value, expiryDate.value)) {
        messageElement.textContent = "Expiry date must be after start date";
        messageElement.style.color = 'red';
        return;
    }

    const voucher = {
        id: id.value,
        status: (voucher_status.value) ? voucher_status.value : null,
        quantity: quantity.value,
        description: description.value,
        customerType: customerType.value,
        percentageIsReduced: discount.value,
        expiryDate: expiryDate.value,
        startDate: startDate.value
    }

    try{
        const response = await fetch('/api/voucher/save', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(voucher)
        });

        const result = await response.json();
        if (!response.ok){
            messageElement.textContent = result.message;
            messageElement.style.color = 'red';
            return;
        }

        id.value = '';
        voucher_status.value = '';
        description.value = '';
        quantity.value = '';
        discount.value = '';
        customerType.value = '';
        startDate.value = '';
        expiryDate.value = '';

        messageElement.textContent = result.message;
        messageElement.style.color = 'green';

        await getVoucherByStatus('New');

    }catch (error){
        console.error('There was a problem with the Add voucher operation:', error);
    }
}

async function showFormUpdateVoucher(voucherId){
    var modal = document.getElementById("voucher-modal");
    var done = document.getElementsByClassName("voucher-done-btn")[0];
    const messageElement = document.getElementById('voucher-message');

    const id = document.getElementById('voucher-id');
    const voucher_status = document.getElementById('voucher-status');
    const description = document.getElementById('voucher-description');
    const quantity = document.getElementById('voucher-quantity');
    const discount = document.getElementById('voucher-discount');
    const customerType = document.getElementById('voucher-customer-type');
    const startDate = document.getElementById('voucher-start-date');
    const expiryDate = document.getElementById('voucher-expiry-date');


    //check voucher
    try{
        const response = await fetch('/api/voucher/'+voucherId);

        if (!response.ok){
            console.error("Voucher does not exist in show form edit!!!");
            return;
        }

        const voucherResponse = await response.json();

        //show form and set value
        modal.style.display = "block";
        setTimeout(function () {
            modal.classList.add("show");
        }, 10);


        id.value = voucherResponse.id;
        voucher_status.value = voucherResponse.status;
        description.value = voucherResponse.description;
        quantity.value = voucherResponse.quantity;
        discount.value = voucherResponse.percentageIsReduced;
        customerType.value = voucherResponse.customerType;
        startDate.value = voucherResponse.startDate;
        expiryDate.value = voucherResponse.expiryDate;


        done.addEventListener("click", function () {
            modal.classList.remove("show");
            modal.style.display = "none";
            setTimeout(function () {
                messageElement.textContent = '';
            }, 500);
        });

    }catch (error){
        console.error('There was a problem with the find voucher to update operation:', error);
    }
}

async function deleteVoucher(voucherId){
    const confirm = await Swal.fire({
        title: 'Do you want to delete it?',
        // text: "Bạn sẽ không thể hoàn nguyên hành động này!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    });

    if (confirm.isConfirmed){//if user comfirm delete
        try{
            const response = await fetch('/api/voucher/'+voucherId,{
                method: 'DELETE'
            });

            if (!response.ok){
                const errorMessage = await response.text();
                throw new Error(`Failed to delete product: ${errorMessage}`);
            }

            Swal.fire(
                'Deleted successful!'
            );

            await getVoucherByStatus('New');
        }catch (error){
            console.error('There was a problem with the delete voucher operation:', error);
        }
    }
}




































