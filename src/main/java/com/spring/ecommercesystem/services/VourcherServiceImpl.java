package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Vourcher;
import com.spring.ecommercesystem.repositories.VourcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class VourcherServiceImpl implements VourcherService {
    private final VourcherRepository vourcherRepository;

    @Autowired
    public VourcherServiceImpl(VourcherRepository vourcherRepository) {
        this.vourcherRepository = vourcherRepository;
    }

    @Override
    public List<Vourcher> findAll() {
        return this.vourcherRepository.findAll();
    }

    @Override
    public Vourcher findById(Long id) {
        return this.vourcherRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Vourcher vourcher) {
        this.vourcherRepository.saveAndFlush(vourcher);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.vourcherRepository.deleteById(id);
    }
}
