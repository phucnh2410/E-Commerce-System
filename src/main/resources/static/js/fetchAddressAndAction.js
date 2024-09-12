$(document).ready(function() {
    // changeAddress(event).then(r => {});
});

function showAddress(){
    var addressModal = document.getElementById('address-modal');

    var btnCancel = document.getElementById('btn-cancel-address');

    addressModal.style.display = "block";
    setTimeout(function () {
        addressModal.classList.add("show");
    }, 10); // Đảm bảo rằng lớp 'show' được thêm sau khi display được áp dụng

    // Ẩn modal khi click vào nút "done"
    btnCancel.addEventListener("click", function () {
        console.log('btn done was clicked');
        addressModal.classList.remove("show");
        setTimeout(function () {
            addressModal.style.display = "none";
            // studentForm.reset();
        }, 500); // Khớp với thời gian của transition
    });


    //Get the address id when choose another address
    let selectedAddressId = null;
    const addressItems = document.querySelectorAll('.address-item');
    const okButton = document.getElementById('btn-address-ok');

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
            okButton.disabled = false;
        });
    });

    okButton.addEventListener('click', function () {
        if (selectedAddressId) {
            changeAddressAction(selectedAddressId).then(r => {});
        }
    });

    // try{
    //     const response = await fetch("/api/address/all");
    //
    //     const addresses = await response.json();
    //
    //     const addressOptions = addresses.reduce((options, address) => {
    //         options[address.id] =
    //             address.phoneNumber+", "+address.street +" street, "+ address.wardAndCommune +" ward, "+ address.district +" district, "+ address.city +" city, "+address.country;
    //         return options;
    //     }, {});
    //
    //     const { value: selectedAddressId, isConfirmed, isDenied } = await Swal.fire({
    //         title: 'Select Address',
    //         input: 'select',
    //         inputOptions: addressOptions,
    //         // inputPlaceholder: 'Select an address',
    //         showCancelButton: true,
    //         showDenyButton: true,
    //         denyButtonText: 'Add New Address',
    //         inputValidator: (value) => {
    //             return new Promise((resolve, reject) => {
    //                 if (value) {
    //                     resolve();
    //                 } else {
    //                     reject('You need to select an address!');
    //                 }
    //             });
    //         }
    //     });
    //
    //     if (isConfirmed && selectedAddressId) {
    //         const selectedAddress = addressOptions[selectedAddressId];
    //         document.getElementById('current-address').innerText = selectedAddress;
    //         document.getElementById('data-address-id').value = selectedAddressId;
    //     } else if (isDenied){
    //         await addNewAddress();
    //     }else {
    //
    //     }
    //
    // }catch (error) {
    //     console.error('Error fetching addresses:', error);
    // }
}

async function changeAddressAction(selectedAddressId){
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

async function addNewAddress(){
    try {
        const { value: phoneNumber } = await Swal.fire({
            title: 'Phone Number',
            input: 'text',
            inputPlaceholder: 'Enter your phone number',
            showCancelButton: true,
            customClass: {
                popup: 'custom-swal-popup-phoneNumber',
                title: 'custom-swal-title-phoneNumber',
                input: 'custom-swal-input-phoneNumber'
            },
            didOpen: () => {
                document.querySelector('.custom-swal-popup-phoneNumber').style.cssText = `
                    width: 50%;
                    // max-width: 500px;
                    margin: auto;
                    font-size: 18px;
                `;
            },
            inputValidator: (value) => {
                if (!value) {
                    return 'Phone number is required!';
                }
            }
        });

        if (!phoneNumber) return;

        const { value: street } = await Swal.fire({
            title: 'Street',
            input: 'text',
            inputPlaceholder: 'Enter your street',
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return 'Street is required!';
                }
            }
        });

        if (!street) return;

        const { value: wardAndCommune } = await Swal.fire({
            title: 'Ward and Commune',
            input: 'text',
            inputPlaceholder: 'Enter your ward and commune',
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return 'ZIP code is required!';
                }
            }
        });

        if (!wardAndCommune) return;

        const { value: district } = await Swal.fire({
            title: 'District',
            input: 'text',
            inputPlaceholder: 'Enter your district',
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return 'District is required!';
                }
            }
        });

        if (!district) return;

        const { value: city } = await Swal.fire({
            title: 'City or Province',
            input: 'text',
            inputPlaceholder: 'Enter your city or province',
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return 'City is required!';
                }
            }
        });

        if (!city) return;


        const { value: country } = await Swal.fire({
            title: 'Country',
            input: 'text',
            inputPlaceholder: 'Enter your country',
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return 'Country is required!';
                }
            }
        });

        if (!country) return;

        const address = {
            phoneNumber,
            street,
            district,
            city,
            wardAndCommune,
            country
        };

        const response = await fetch("/api/address/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(address)
        });

        if (!response.ok) {
            const result = await response.json();
            Swal.fire('Error', result.message || 'Failed to add address', 'error');
            return;
        }

        const addedAddress = await response.json();
        Swal.fire('Success', 'New address added successfully!', 'success');
        document.getElementById('current-address').innerText = addedAddress.phoneNumber+", "+addedAddress.street +", "+ addedAddress.wardAndCommune +", "+ addedAddress.district +", "+ addedAddress.city +", "+addedAddress.country;


    } catch (error) {
        console.error('Error saving address:', error);
        Swal.fire('Error', 'An error occurred while saving the address', 'error');
    }

}














