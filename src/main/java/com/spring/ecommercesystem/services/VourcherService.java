package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Vourcher;

import java.util.List;

public interface VourcherService {
    public List<Vourcher> findAll();
    public Vourcher findById(Long id);
    public void saveAndUpdate(Vourcher vourcher);
    public void deleteById(Long id);
}
