package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistic")
public class StatisticRestController {
    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private final UserService userService;

    private final ProductService productService;

    private final SaleService saleService;

    @Autowired
    public StatisticRestController(OrderService orderService, OrderDetailService orderDetailService, UserService userService, ProductService productService, SaleService saleService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.userService = userService;
        this.productService = productService;
        this.saleService = saleService;
    }

    @GetMapping("/seller/monthly/{id}")
    public ResponseEntity<Map<String, Object>> getMonthlySellerData(@PathVariable Long id) {

        Map<String, Object> data = this.saleService.totalRevenueAndQuantityByMonthsSellers(id);

        // Lấy giá trị revenue và quantity từ Map
        Map<String, Double> revenue = (Map<String, Double>) data.get("revenue");
        Map<String, Integer> quantity = (Map<String, Integer>) data.get("quantity");

        // Tạo danh sách tháng từ các khóa trong bản đồ doanh thu
        List<String> months = new ArrayList<>(revenue.keySet());
        Collections.sort(months, (a, b) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);
            try {
                Date dateA = sdf.parse(a);
                Date dateB = sdf.parse(b);
                return dateA.compareTo(dateB);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });

        // Tạo danh sách doanh thu và số lượng theo tháng
        List<Double> revenueList = months.stream().map(m -> revenue.getOrDefault(m, 0.0)).collect(Collectors.toList());
        List<Integer> quantityList = months.stream().map(m -> quantity.getOrDefault(m, 0)).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("months", months);
        result.put("revenue", revenueList);
        result.put("quantity", quantityList);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/seller/quarterly/{id}")
    public ResponseEntity<Map<String, Object>> getQuarterlySellerData(@PathVariable Long id) {
        Map<String, Object> data = this.saleService.totalRevenueAndQuantityByQuartersSeller(id);

        Map<String, Double> revenue = (Map<String, Double>) data.get("revenue");
        Map<String, Integer> quantity = (Map<String, Integer>) data.get("quantity");


        List<String> quarters = new ArrayList<>(revenue.keySet());
        Collections.sort(quarters, (a, b) -> {
            String[] partsA = a.split(" ");
            String[] partsB = b.split(" ");
            int yearA = Integer.parseInt(partsA[1]);
            int yearB = Integer.parseInt(partsB[1]);
            int quarterA = Integer.parseInt(partsA[0].substring(1));
            int quarterB = Integer.parseInt(partsB[0].substring(1));
            //So sánh năm của quý và sắp xếp lại năm nếu có sự khác nhau về năm
            if (yearA != yearB) {
                return yearA - yearB;
            } else {
                //ngược lại sẽ so sánh quý và sắp xếp lại quý
                return quarterA - quarterB;
            }
        });

        // Tạo danh sách doanh thu và số lượng tương ứng với các quý
        List<Double> revenueList = quarters.stream().map(m -> revenue.getOrDefault(m, 0.0)).collect(Collectors.toList());
        List<Integer> quantityList = quarters.stream().map(m -> quantity.getOrDefault(m, 0)).collect(Collectors.toList());



        // Dữ liệu cho biểu đồ cột: Doanh thu và số lượng sản phẩm mỗi quý
        Map<String, Object> result = new HashMap<>();
        result.put("quarters", quarters);
        result.put("revenue", revenueList);
        result.put("quantity", quantityList);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/seller/yearly/{id}")
    public ResponseEntity<Map<String, Object>> getYearlySellerData(@PathVariable Long id) {
        Map<String, Object> data = this.saleService.totalRevenueAndQuantityByYearsSeller(id);

        Map<String, Double> revenue = (Map<String, Double>) data.get("revenue");
        Map<String, Integer> quantity = (Map<String, Integer>) data.get("quantity");

        List<String> years = new ArrayList<>(revenue.keySet());
        Collections.sort(years, (a, b) -> {
            String[] partsA = a.split(" ");
            String[] partsB = b.split(" ");
            int yearA = Integer.parseInt(partsA[1]);
            int yearB = Integer.parseInt(partsB[1]);
            //So sánh năm của quý và sắp xếp lại năm nếu có sự khác nhau về năm
            if (yearA != yearB) {
                return yearA - yearB;
            } else {
                //ngược lại sẽ so sánh quý và sắp xếp lại quý
                return yearA;
            }
        });

        // Tạo danh sách doanh thu và số lượng tương ứng với các quý
        List<Double> revenueList = years.stream().map(m -> revenue.getOrDefault(m, 0.0)).collect(Collectors.toList());
        List<Integer> quantityList = years.stream().map(m -> quantity.getOrDefault(m, 0)).collect(Collectors.toList());


        Map<String, Object> result = new HashMap<>();

        //Arrays.asList("2024", "2025", "2026","2027","2028","2029","2030","2031","2032","2033","2034","2035")

        // Dữ liệu cho biểu đồ cột: Doanh thu và số lượng sản phẩm mỗi năm
        result.put("years", years);
        result.put("revenue", revenueList);
        result.put("quantity", quantityList);

        return ResponseEntity.ok(result);
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/customer/monthly/{id}")
    ResponseEntity<Map<String, Object>> getMonthlyCustomerData(@PathVariable Long id) {
        Map<String, Object> data = this.saleService.totalAmountAndQuantityByMonthsCustomers(id);

        // Lấy giá trị revenue và quantity từ Map
        Map<String, Double> totalAmount = (Map<String, Double>) data.get("totalAmount");
        Map<String, Integer> quantity = (Map<String, Integer>) data.get("quantity");

        // Tạo danh sách tháng từ các khóa trong bản đồ doanh thu
        List<String> months = new ArrayList<>(totalAmount.keySet());
        Collections.sort(months, (a, b) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.US);
            try {
                Date dateA = sdf.parse(a);
                Date dateB = sdf.parse(b);
                return dateA.compareTo(dateB);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });

        // Tạo danh sách doanh thu và số lượng theo tháng
        List<Double> totalAmountList = months.stream().map(m -> totalAmount.getOrDefault(m, 0.0)).collect(Collectors.toList());
        List<Integer> quantityList = months.stream().map(m -> quantity.getOrDefault(m, 0)).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("months", months);
        result.put("totalAmount", totalAmountList);
        result.put("quantity", quantityList);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/customer/quarterly/{id}")
    ResponseEntity<Map<String, Object>> getQuarterlyCustomerData(@PathVariable Long id) {
        return null;
    }
    @GetMapping("/customer/yearly/{id}")
    ResponseEntity<Map<String, Object>> getYearlyCustomerData(@PathVariable Long id) {
        return null;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/category/monthly/{id}")
    ResponseEntity<Map<String, Object>> getMonthlyCategoryData(@PathVariable Long id) {
        return null;
    }
    @GetMapping("/category/quarterly/{id}")
    ResponseEntity<Map<String, Object>> getQuarterlyCategoryData(@PathVariable Long id) {
        return null;
    }
    @GetMapping("/category/yearly/{id}")
    ResponseEntity<Map<String, Object>> getYearlyCategoryData(@PathVariable Long id) {
        return null;
    }


}
