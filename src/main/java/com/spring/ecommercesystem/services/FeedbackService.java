package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.Feedback;

import java.util.List;

public interface FeedbackService {
    public List<Feedback> findAll();
    public Feedback findById(Long id);
    public void saveAndUpdate(Feedback feedback);
    public void deleteById(Long id);
}
