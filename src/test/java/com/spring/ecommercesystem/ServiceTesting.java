package com.spring.ecommercesystem;

import com.spring.ecommercesystem.services.SaleService;
import com.spring.ecommercesystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ServiceTesting {
    @Mock
    private UserService userService;

    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTotalRevenueAndQuantityByMonths(){
//        Map<Object, Object> revenueAndQuantityByMonths = this.saleService.totalRevenueAndQuantityByMonths();
//
//        Map<String, Double> revenue = (Map<String, Double>) revenueAndQuantityByMonths.get("revenue");
//        Map<String, Integer> quantity = (Map<String, Integer>) revenueAndQuantityByMonths.get("quantity");
//
//        List<Map.Entry<String, Double>> showRevenue = (List<Map.Entry<String, Double>>) revenue;
//        List<Map.Entry<String, Integer>> showQuantity = (List<Map.Entry<String, Integer>>) quantity;
//
//        showRevenue.forEach(stringDoubleEntry -> System.out.println("Date: "+stringDoubleEntry.getKey() +" -> "+ "Revenue: "+stringDoubleEntry.getValue()));
//        showQuantity.forEach(stringIntegerEntry -> System.out.println("Date: "+stringIntegerEntry.getKey() +" -> "+"Quantity: "+stringIntegerEntry.getValue()));
    }
}
