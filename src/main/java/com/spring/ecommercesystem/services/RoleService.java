package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Role;

import java.util.List;

public interface RoleService {
    public List<Role> findAll();
    public Role findByName(String name);
    public Role findById(Long id);
    public void saveAndUpdate(Role role);
    public void deleteById(Long id);
}
