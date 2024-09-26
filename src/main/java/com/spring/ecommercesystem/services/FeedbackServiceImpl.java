package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Feedback;
import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, ProductService productService, UserService userService, OrderService orderService) {
        this.feedbackRepository = feedbackRepository;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
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
    public Boolean saveAndUpdate(Feedback feedbackRequest) {
        Product productRequest = this.productService.findById(feedbackRequest.getProduct().getId());
        Order orderRequest = this.orderService.findById(feedbackRequest.getOrder().getId());

        List<Feedback> feedbacksInProduct = productRequest.getFeedbacks();
        List<Feedback> feedbacksInOrder = orderRequest.getFeedbacks();
        //If product does not contain any feedbacks
        if (feedbacksInProduct.size() == 0) {
            //set current date
            feedbackRequest.setFeedbackDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
            //set Current user
            feedbackRequest.setUser(this.userService.getCurrentUser());
            this.feedbackRepository.saveAndFlush(feedbackRequest);
            return false;
        }

        for (Feedback feedback : feedbacksInProduct) {
            //Nếu hợp lệ thì có nghĩa là user này đã feedback cho sp này rồi nếu có feedback thêm nữa thì update lại feedback cũ
            if (feedback.getProduct().getId().equals(productRequest.getId())) {
                if (feedback.getUser().getId().equals(this.userService.getCurrentUser().getId())) {

                    if (feedback.getProduct().getId().equals(productRequest.getId()) && feedback.getUser().getId().equals(this.userService.getCurrentUser().getId()) && feedbacksInOrder.contains(feedback)){
                        return true;
                    }
//                    if (feedbacksInOrder.contains(feedback)) {//User này đã feedback cho product nay trong đơn hàng này rồi
//
//                    }

                    //product này đã được feedback ở 1 đơn hàng khác trước đó rồi,
                    // nhưng tới đơn hàng mới này thì customer có thể update lại feedback cho sp này
                    Feedback feedbackToUpdate = this.feedbackRepository.findById(feedback.getId()).get();
                    feedbackToUpdate.setFeedbackDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
                    feedbackToUpdate.setUser(this.userService.getCurrentUser());
                    feedbackToUpdate.setProduct(productRequest);
                    feedbackToUpdate.setOrder(orderRequest);
                    feedbackToUpdate.setFeedbackRating(feedbackRequest.getFeedbackRating());
                    feedbackToUpdate.setContent(feedbackRequest.getContent());

                    this.feedbackRepository.saveAndFlush(feedbackToUpdate);
                    return false;
                }
            }
        }
        feedbackRequest.setFeedbackDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
        //set Current user
        feedbackRequest.setUser(this.userService.getCurrentUser());
        this.feedbackRepository.saveAndFlush(feedbackRequest);
        return false;
    }


    //////////////// Code toi uu Hieu Suat///////////////////

/*
*
* public void saveAndUpdate(Feedback feedbackRequest, Boolean isDone) {
    Product productRequest = this.productService.findById(feedbackRequest.getProduct().getId());
    Order orderRequest = this.orderService.findById(feedbackRequest.getOrder().getId());

    Set<Feedback> feedbacksInProduct = new HashSet<>(productRequest.getFeedbacks());
    Set<Feedback> feedbacksInOrder = new HashSet<>(orderRequest.getFeedbacks());

    // Nếu không có feedback nào cho sản phẩm
    if (feedbacksInProduct.isEmpty()) {
        feedbackRequest.setFeedbackDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
        feedbackRequest.setUser(this.userService.getCurrentUser());
        this.feedbackRepository.saveAndFlush(feedbackRequest);
        return;
    }

    for (Feedback feedback : feedbacksInProduct) {
        if (feedback.getUser().equals(this.userService.getCurrentUser()) &&
            feedback.getProduct().equals(productRequest)) {

            if (feedbacksInOrder.isEmpty()) {
                feedback.setFeedbackDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
                this.feedbackRepository.saveAndFlush(feedback);
                return;
            }

            if (feedbacksInOrder.contains(feedback)) {
                isDone = true;
                return;
            }
        }
    }
}

*
*
* */

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.feedbackRepository.deleteById(id);
    }



}















