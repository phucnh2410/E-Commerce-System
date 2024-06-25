package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.Role;
import com.spring.ecommercesystem.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }

    @Override
    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id).get();
    }

    @Override
    public void saveAndUpdate(Role role) {
        this.roleRepository.saveAndFlush(role);
    }

    @Override
    public void deleteById(Long id) {
        this.roleRepository.deleteById(id);
    }
}
