package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.Address;

import java.util.List;

public interface AddressService {
    public List<Address> findAll();
    public Address findById(Long id);
    public void saveAndUpdate(Address address);
    public void deleteById(Long id);

}
