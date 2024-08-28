package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Feedback;
import com.spring.ecommercesystem.services.FeedbackService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackRestController {
    private final FeedbackService feedbackService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public FeedbackRestController(FeedbackService feedbackService, ProductService productService, UserService userService) {
        this.feedbackService = feedbackService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveFeedback(@RequestBody Feedback feedback) {
        Map<String, Object> response = new HashMap<>();

        if (feedback == null) {
            response.put("message", "Feedback is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Boolean isDone = this.feedbackService.saveAndUpdate(feedback);
        if (isDone == true){
            response.put("message", "You have already rated this product in this order. Thank you for your feedback!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("message", "This feedback was sent successful!!!");
        response.put("feedback", feedback);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
