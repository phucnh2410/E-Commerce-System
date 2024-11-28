package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Address;
import com.spring.ecommercesystem.services.AddressService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;

    @Autowired
    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping("/fetch_fragment")
    public String fetchAddressFragment(Model model){
//        List<Address> addresses = ;

        model.addAttribute("addresses", this.userService.getCurrentUser().getAddresses());
        return "Checkout/checkoutPage :: addressPopupFragmentForm";
    }
}


















