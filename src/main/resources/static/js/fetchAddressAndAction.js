$(document).ready(function() {
    // changeAddress(event).then(r => {});
});

async function fetchAllAddress(){
    const addressPopupBody = document.querySelector('#address-body');

    try{
        const response = await fetch('/addresses/fetch_fragment');

        if (!response.ok){
            throw new Error(response.statusText);
        }

        const addresses = await response.text();

        if (!addressPopupBody){
            console.log('Element #addressPopupBody not found in the response');
        }

        addressPopupBody.innerHTML = "";
        addressPopupBody.innerHTML = addresses;

        bindAddressSelection();
    }catch (error) {
        console.error('Error fetching addresses:', error);
    }
}

async function saveNewAddress(){
    const messageElement = document.getElementById('address-modal-message');

    const fullNameInput = document.getElementById('address-fullName-input');
    const phoneInput = document.getElementById('address-phone-input');
    const streetInput = document.getElementById('address-street-input');
    const wardInput = document.getElementById('address-ward-input');
    const districtInput = document.getElementById('address-district-input');
    const cityInput = document.getElementById('address-city-input');
    const countryInput = document.getElementById('address-country-input');


    function isValidInteger(value){
        return /^[1-9]\d*$/.test(value);
    }

    //check empty
    if (!fullNameInput.value){
        messageElement.textContent = "Please enter the recipient's Name";
        messageElement.style.color = 'red';
        fullNameInput.focus();
        return false;
    }

    if (!phoneInput.value){
        messageElement.textContent = "Please enter the recipient's Phone number";
        messageElement.style.color = 'red';
        phoneInput.focus();
        return false;
    }else if (!isValidInteger(phoneInput.value)){
        messageElement.textContent = "Phone number only contain number";
        messageElement.style.color = 'red';
        phoneInput.focus();
        return false;
    }

    if (!streetInput.value){
        messageElement.textContent = "Please enter the Street";
        messageElement.style.color = 'red';
        streetInput.focus();
        return false;
    }if (!wardInput.value){
        messageElement.textContent = "Please enter the Ward or Commune";
        messageElement.style.color = 'red';
        wardInput.focus();
        return false;
    }if (!districtInput.value){
        messageElement.textContent = "Please enter the District";
        messageElement.style.color = 'red';
        districtInput.focus();
        return false;
    }if (!cityInput.value){
        messageElement.textContent = "Please enter the City or Province";
        messageElement.style.color = 'red';
        cityInput.focus();
        return false;
    }if (!countryInput.value){
        messageElement.textContent = "Please enter the Country";
        messageElement.style.color = 'red';
        countryInput.focus();
        return false;
    }

    const address = {
        recipientName: fullNameInput.value,
        phoneNumber: phoneInput.value,
        street: streetInput.value,
        wardAndCommune: wardInput.value,
        district: districtInput.value,
        city: cityInput.value,
        country: countryInput.value
    }

    try {
        const response = await fetch('/api/address/add', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(address)
        });

        if (!response){
            throw new Error(response.statusText);
            return false;
        }

        const result = await response.json();
        messageElement.textContent = '';
        messageElement.textContent = result.message;
        messageElement.style.color = 'green';

        await fetchAllAddress();
        return true;

    }catch (error){
        console.error('Error with add new addresses:', error);
        return false;
    }
}

async function removeAddressSelected(selectedAddressId){
    console.log("Address id selected on remove function: "+selectedAddressId);
    if (selectedAddressId){
        try{
            const response = await fetch('/api/address/delete/'+selectedAddressId, {
                method: 'DELETE'
            });

            if (!response.ok) {
                throw new Error(response.statusText);
                return false;
            }

            await fetchAllAddress();
            return true;
        }catch (error){
            console.error('There was a problem with the delete address selected operation:', error);
            return false;
        }
    }
}

//gán lại sự kiện khi address data được update
function bindAddressSelection() {
    const addressItems = document.querySelectorAll('.address-item');
    let selectedAddressId = null;

    addressItems.forEach(function (item) {
        item.addEventListener('click', function () {
            // Xóa class 'selected' từ tất cả các address-item khác
            addressItems.forEach(function (el) {
                el.classList.remove('selected');
            });

            // Thêm class 'selected' cho address-item được click
            this.classList.add('selected');

            // Lấy ID của address được chọn
            selectedAddressId = this.getAttribute('data-address-id');

            // Mở khóa nút OK
            const btnOkSelected = document.getElementById('btn-address-ok');
            btnOkSelected.disabled = false;

            // Xử lý sự kiện OK
            btnOkSelected.addEventListener('click', function () {
                if (selectedAddressId) {
                    console.log("Address id selected: " + selectedAddressId);
                    changeAddressAction(selectedAddressId).then(r => {
                    });
                }
            });

            const btnDeleteSelected = document.getElementById('btn-address-delete');
            btnDeleteSelected.disabled = false;

            btnDeleteSelected.addEventListener('click', function (){
                const messageElement = document.getElementById('address-modal-message');
                messageElement.textContent = '';
                if (selectedAddressId == null){
                    messageElement.textContent = 'Please select an address to delete';
                    messageElement.style.color = 'red';
                    return;
                }

                const isDeleted = removeAddressSelected(selectedAddressId).then(r => {});
                if (!isDeleted){
                    messageElement.textContent = 'Some thing went wrong, please try again!';
                    messageElement.style.color = 'red';
                    return;
                }
            });

        });
    });
}


function showAddressPopup(){
    // fetchAllAddress().then(r => {});
    const addressModal = document.getElementById('address-modal');

    // const message = document.getElementById('address-modal-message');
    const messageElement = document.getElementById('address-modal-message');

    const btnAddMore = document.getElementById('btn-address-add');
    const btnNext = document.getElementById('btn-address-add-next');
    const btnSave = document.getElementById('btn-address-add-save');
    const btnCancel = document.getElementById('btn-address-add-cancel');


    const btnOkSelected = document.getElementById('btn-address-ok');
    const btnDeleteSelected = document.getElementById('btn-address-delete');
    const btnCancelSelected = document.getElementById('btn-cancel-address');

    const inputs = document.querySelectorAll('.address-add-input');

    let currentInputIndex = 0;



    addressModal.style.display = "block";
    setTimeout(function () {
        addressModal.classList.add("show");
    }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng

    // Ẩn modal khi click vào nút "done"
    btnCancelSelected.addEventListener("click", function () {
        messageElement.textContent = '';
        selectedAddressId = null;
        addressModal.classList.remove("show");
        setTimeout(function () {
            addressModal.style.display = "none";
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });


    //Get the address id when choose another address
    let selectedAddressId = null;
    const addressItems = document.querySelectorAll('.address-item');

    addressItems.forEach(function (item) {
        item.addEventListener('click', function () {
            // Xóa class 'selected' từ tất cả các address-item khác
            addressItems.forEach(function (el) {
                el.classList.remove('selected');
            });

            // Thêm class 'selected' cho address-item được click
            this.classList.add('selected');

            // Lấy ID của address được chọn
            selectedAddressId = this.getAttribute('data-address-id');

            // Mở khóa nút OK
            btnOkSelected.disabled = false;
        });
    });

    if (btnOkSelected) {
        btnOkSelected.addEventListener('click', function () {
            messageElement.textContent = '';
            if (selectedAddressId) {
                console.log("Address id selected: "+selectedAddressId);
                changeAddressAction(selectedAddressId).then(r => {});

                selectedAddressId = null;
            }
        });
    }

    if (btnDeleteSelected){
        btnDeleteSelected.addEventListener('click', function (){
            messageElement.textContent = '';
            if (selectedAddressId == null){
                messageElement.textContent = 'Please select an address to delete';
                messageElement.style.color = 'red';
            }else {
                Swal.fire({
                    title: 'Are you sure?',
                    text: 'Do you really want to delete this address? This action cannot be undone.',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Gọi hàm xóa địa chỉ nếu người dùng xác nhận
                        removeAddressSelected(selectedAddressId)
                            .then((isDeleted) => {
                                if (isDeleted) {
                                    Swal.fire(
                                        'Deleted!',
                                        'The address has been deleted.',
                                        'success'
                                    );
                                    selectedAddressId = null;
                                } else {
                                    messageElement.textContent = 'Something went wrong, please try again!';
                                    messageElement.style.color = 'red';
                                }
                            })
                            .catch(() => {
                                messageElement.textContent = 'Something went wrong, please try again!';
                                messageElement.style.color = 'red';
                            });
                    }
                });

                selectedAddressId = null;
            }

        });

    }


    if (btnAddMore) {
        btnAddMore.addEventListener("click", function () {
            messageElement.textContent = '';
            selectedAddressId = null;
            // addForm.style.display = 'block';
            if (inputs.length > 0) {
                inputs[currentInputIndex].style.display = 'inline-block';
                inputs[currentInputIndex].focus();

                btnOkSelected.style.display = 'none';
                btnCancelSelected.style.display = 'none';
                btnAddMore.style.display = 'none';
                btnDeleteSelected.style.display = 'none';

                btnNext.style.display = 'inline-block';
                btnSave.style.display = 'inline-block';
                btnCancel.style.display = 'inline-block';

                currentInputIndex += 1;
            }
        });
    }

    if (btnNext){
        btnNext.addEventListener("click", function (){
            if (currentInputIndex < inputs.length) {
                inputs[currentInputIndex].style.display = 'inline-block';
                inputs[currentInputIndex].focus();
            }
            if (currentInputIndex === inputs.length - 1) {
                btnNext.style.display = 'none';
            }

            currentInputIndex += 1;
        });
    }

    if (btnSave){
        btnSave.addEventListener("click", function (){
            messageElement.textContent = '';
            const isSuccess = saveNewAddress().then((isSuccess) => {
                if (isSuccess) {
                    currentInputIndex = 0;
                    for (let i = 0; i < inputs.length; i++) {
                        inputs[i].style.display = 'none';
                        inputs[i].value = '';
                    }
                    btnNext.style.display = 'none';
                    btnSave.style.display = 'none';
                    btnCancel.style.display = 'none';

                    btnOkSelected.style.display = 'inline-block';
                    btnCancelSelected.style.display = 'inline-block';
                    btnAddMore.style.display = 'inline-block';
                    btnDeleteSelected.style.display = 'inline-block';
                }
                }).catch(() => {
                    messageElement.textContent = 'Something went wrong, please try again!';
                    messageElement.style.color = 'red';
                });

        });
    }

    if (btnCancel){
        btnCancel.addEventListener("click", function (){
            messageElement.textContent = '';
            currentInputIndex = 0;
            for (let i = 0; i < inputs.length ; i++){
                inputs[i].style.display = 'none';
                inputs[i].value = '';
            }
            btnNext.style.display = 'none';
            btnSave.style.display = 'none';
            btnCancel.style.display = 'none';

            btnOkSelected.style.display = 'inline-block';
            btnCancelSelected.style.display = 'inline-block';
            btnAddMore.style.display = 'inline-block';
            btnDeleteSelected.style.display = 'inline-block';
        });
    }
}

async function changeAddressAction(selectedAddressId){
    console.log("Address id selected: "+selectedAddressId);
    if (selectedAddressId){
        const currentAddressElement = document.querySelector('#current-address');
        const dataAddressIdElement = document.getElementById('data-address-id');
        var addressModal = document.getElementById('address-modal');
        try{
            const response = await fetch('/shopping_cart/address_fragment/'+selectedAddressId);

            if (!response.ok) {
                throw new Error(response.statusText);
            }

            const address = await response.text();
            if (!currentAddressElement) {
                console.log('Element #currentAddressElement not found in the response');
            }

            currentAddressElement.innerHTML = "";
            currentAddressElement.innerHTML = address;
            dataAddressIdElement.value = selectedAddressId;
            //Hide the popup
            addressModal.classList.remove("show");
            setTimeout(function () {
                addressModal.style.display = "none";
                // studentForm.reset();
            }, 500);


        }catch (error){
            console.error('There was a problem with the fetch current address operation:', error);
        }
    }
}














