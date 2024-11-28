package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Address;
import com.spring.ecommercesystem.services.AddressService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressRestController {
    private final AddressService addressService;
    private final UserService userService;

    @Autowired
    public AddressRestController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Address>> getAllAddresses (){
        List<Address> addresses = this.addressService.findAll();
        return ResponseEntity.ok().body(addresses);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addAddress(@RequestBody Address address){
        Map<String, Object> response = new HashMap<>();
        if (address == null){
            response.put("message", "Address object is nulll");
            return ResponseEntity.badRequest().body(response);
        }

        address.setUser(this.userService.getCurrentUser());
        this.addressService.saveAndUpdate(address);
        response.put("message", "New address was added successful");
        response.put("address", address);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id){
        try{
            this.addressService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
















