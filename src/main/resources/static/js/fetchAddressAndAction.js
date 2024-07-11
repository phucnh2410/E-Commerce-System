$(document).ready(function() {

});

async function changeAddress(event){
    event.preventDefault();
    try{
        const response = await fetch("/api/address/all");

        const addresses = await response.json();

        const addressOptions = addresses.reduce((options, address) => {
            options[address.id] =
                address.phoneNumber+", "+address.street +" street, "+ address.wardAndCommune +" ward, "+ address.district +" district, "+ address.city +" city, "+address.country +" country";
            return options;
        }, {});

        const { value: selectedAddressId, isConfirmed, isDenied } = await Swal.fire({
            title: 'Select Address',
            input: 'select',
            inputOptions: addressOptions,
            inputPlaceholder: 'Select an address',
            showCancelButton: true,
            showDenyButton: true,
            denyButtonText: 'Add New Address',
            inputValidator: (value) => {
                return new Promise((resolve, reject) => {
                    if (value) {
                        resolve();
                    } else {
                        reject('You need to select an address!');
                    }
                });
            }
        });

        if (isConfirmed && selectedAddressId) {
            const selectedAddress = addressOptions[selectedAddressId];
            document.getElementById('current-address').innerText = selectedAddress;
        } else if (isDenied){
            await addNewAddress();
        }else {

        }

    }catch (error) {
        console.error('Error fetching addresses:', error);
    }
}

async function addNewAddress(){
    try {
        const { value: phoneNumber } = await Swal.fire({
            title: 'Phone Number',
            input: 'text',
            inputPlaceholder: 'Enter your phone number',
            showCancelButton: true,
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
        document.getElementById('current-address').innerText = addedAddress.phoneNumber+", "+addedAddress.street +", "+ addedAddress.wardAndCommune +", "+ addedAddress.district +", "+ addedAddress.city +", "+addedAddress.country;;


    } catch (error) {
        console.error('Error saving address:', error);
        Swal.fire('Error', 'An error occurred while saving the address', 'error');
    }

}














