package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> findAll();
    Feedback findById(Long id);
    Boolean saveAndUpdate(Feedback feedback);
    void deleteById(Long id);
}
