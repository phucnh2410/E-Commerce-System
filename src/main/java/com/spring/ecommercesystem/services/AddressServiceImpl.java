package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Address;
import com.spring.ecommercesystem.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> findAll() {
        return this.addressRepository.findAll();
    }

    @Override
    public Address findById(Long id) {
        return this.addressRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Address address) {
        this.addressRepository.saveAndFlush(address);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.addressRepository.deleteById(id);
    }
}
