package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Feedback;
import com.spring.ecommercesystem.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<Feedback> findAll() {
        return this.feedbackRepository.findAll();
    }

    @Override
    public Feedback findById(Long id) {
        return this.feedbackRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Feedback feedback) {
        this.feedbackRepository.saveAndFlush(feedback);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.feedbackRepository.deleteById(id);
    }
}
