package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final UserService userService;
    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private final ProductService productService;

    private final CategoryService categoryService;

    private Map<String, Integer> monthMapping;

    @Autowired
    public SaleService(UserService userService, OrderService orderService, OrderDetailService orderDetailService, ProductService productService, CategoryService categoryService) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.putMonthMapping();
    }

    public int getMonthMappingByKey(String key) {
        return monthMapping.get(key);
    }

    public SaleService putMonthMapping() {
        Map<String, Integer> monthData = new HashMap<>();
        monthData.put("Jan", 0);
        monthData.put("Feb", 1);
        monthData.put("Mar", 2);
        monthData.put("Apr", 3);
        monthData.put("May", 4);
        monthData.put("Jun", 5);
        monthData.put("Jul", 6);
        monthData.put("Aug", 7);
        monthData.put("Sep", 8);
        monthData.put("Oct", 9);
        monthData.put("Nov", 10);
        monthData.put("Dec", 11);


        this.monthMapping = monthData;
        return this;
    }

    public Map<String, Object> totalRevenueAndQuantityForSellerByMonths(Long userId){
        // Create a map to store revenue and quantity per month
        Map<String, Object> revenueAndQuantityByMonthsSels = new HashMap<>();

        User seller = this.userService.findById(userId);

        if (seller == null){
            System.out.println("User in the totalRevenueAndQuantityByMonths()  is null!!!");
            return (Map<String, Object>) revenueAndQuantityByMonthsSels.put("null", "User in the totalRevenueAndQuantityByMonths()  is null!!!");
        }


        // Create two maps to store revenue and quantity
        Map<String, Double> revenueByMonths = new HashMap<>();
        Map<String, Integer> quantityByMonths = new HashMap<>();

        //Lấy tất cả sản phẩm đang  được quản lí bởi seller này
        List<Product> productsBySeller = seller.getProducts();

        //Lấy all orders đang tồn tại
        List<Order> orders = this.orderService.findAll();

        for (Order order: orders){
            Date receivedDate = order.getReceivedDate();
            if (receivedDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receivedDate);

                String monthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + calendar.get(Calendar.YEAR);

                //map.merge(key, value, remappingFunction);
                // nếu key đã có trong map thì map sẽ lấy giá trị đã tồn tại ra để tính toán với giá trị mới được đưa vào, 'remappingFunction': hàm tính toán, vd:Sum

                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    Product product = orderDetail.getProduct();
                    if (productsBySeller.contains(product)) {
                        //get Revenue by months
                        revenueByMonths.merge(monthYear, (orderDetail.getProductQuantity() * orderDetail.getProduct().getPrice()), Double::sum);
                        //get Quantity by months
                        quantityByMonths.merge(monthYear, orderDetail.getProductQuantity(), Integer::sum);
                    }
                }
            }
        }

        revenueAndQuantityByMonthsSels.put("revenue", revenueByMonths);
        revenueAndQuantityByMonthsSels.put("quantity", quantityByMonths);

        return revenueAndQuantityByMonthsSels;
    }

    public Map<String, Object>totalRevenueAndQuantityForSellerByQuarters(Long userId){
        Map<String, Object> monthlyData = this.totalRevenueAndQuantityForSellerByMonths(userId);

        //Lấy dữ liêu cho các Map Month
        Map<String, Double> revenueByMonths = (Map<String, Double>) monthlyData.get("revenue");
        Map<String, Integer> quantityByMonths = (Map<String, Integer>) monthlyData.get("quantity");

        //Tạo các Map cho Quarter
        Map<String, Double> revenueByQuarters = new HashMap<>();
        Map<String, Integer> quantityByQuarters = new HashMap<>();

        //revenue by Quarter
        for (Map.Entry<String, Double> entry: revenueByMonths.entrySet()){
            String monthYear = entry.getKey();
            Double revenue = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);//get from 0 to 11 (from Jan -> Dec)
            int year = Integer.parseInt(parts[1]);
            String monthString = parts[0];
            // Chuyển đổi tháng từ chuỗi sang số nguyên
            int month = this.getMonthMappingByKey(monthString);

            //Calculate the quarter
            int quarter = (month / 3) + 1;
            String quarterYear = "Q" + quarter + " " + year;

            revenueByQuarters.merge(quarterYear, revenue, Double::sum);
        }

        //quantity by Quarter
        for (Map.Entry<String, Integer> entry: quantityByMonths.entrySet()){
            String monthYear = entry.getKey();
            Integer quantity = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);
            int year = Integer.parseInt(parts[1]);
            String monthString = parts[0];
            // Chuyển đổi tháng từ chuỗi sang số nguyên
            int month = this.getMonthMappingByKey(monthString);
            int quarter = (month / 3) + 1;
            String quarterYear = "Q" + quarter + " " + year;

            quantityByQuarters.merge(quarterYear, quantity, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("revenue", revenueByQuarters);
        result.put("quantity", quantityByQuarters);

        return result;
    }


    public Map<String, Object>totalRevenueAndQuantityForSellerByYears(Long userId){
        Map<String, Object> monthlyData = this.totalRevenueAndQuantityForSellerByMonths(userId);

        //Lấy dữ liêu cho các Map Month
        Map<String, Double> revenueByMonths = (Map<String, Double>) monthlyData.get("revenue");
        Map<String, Integer> quantityByMonths = (Map<String, Integer>) monthlyData.get("quantity");

        //Tạo các Map cho Quarter
        Map<String, Double> revenueByYears = new HashMap<>();
        Map<String, Integer> quantityByYears = new HashMap<>();

        //revenue by Quarter
        for (Map.Entry<String, Double> entry: revenueByMonths.entrySet()){
            String monthYear = entry.getKey();
            Double revenue = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);//get from 0 to 11 (from Jan -> Dec)
            String year = parts[1];

            revenueByYears.merge(year, revenue, Double::sum);
        }

        //quantity by Quarter
        for (Map.Entry<String, Integer> entry: quantityByMonths.entrySet()){
            String monthYear = entry.getKey();
            Integer quantity = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);
            String year = parts[1];

            quantityByYears.merge(year, quantity, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("revenue", revenueByYears);
        result.put("quantity", quantityByYears);

        return result;
    }

//////////////////////////////////////////////////////////////*CUSTOMER*////////////////////////////////////////////////////////////////////

    public Map<String, Object> totalAmountAndQuantityForCustomerByMonths(Long userId){
        // Create a map to store revenue and quantity per month
        Map<String, Object> totalAmountAndQuantityByMonthsCus = new HashMap<>();
        Map<String, Double> totalAmountByMonths = new HashMap<>();
        Map<String, Integer> quantityByMonths = new HashMap<>();

        User customer = this.userService.findById(userId);
        if (customer == null){
            System.out.println("User in the totalRevenueAndQuantityByMonths()  is null!!!");
            return (Map<String, Object>) totalAmountAndQuantityByMonthsCus.put("null", "User in the totalRevenueAndQuantityByMonths()  is null!!!");
        }
        List<Order> orders = customer.getOrders();

        for (Order order: orders){
            Date receivedDate = order.getReceivedDate();
            if (receivedDate != null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receivedDate);

                String monthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)+ " " +calendar.get(Calendar.YEAR);

                //map.merge(key, value, remappingFunction);
                // nếu key đã có trong map thì map sẽ lấy giá trị đã tồn tại ra để tính toán với giá trị mới được đưa vào, 'remappingFunction': hàm tính toán, vd:Sum
                //get Revenue by months
                totalAmountByMonths.merge(monthYear, order.getTotalAmount(), Double::sum);


                //get Quantity by months
                for (OrderDetail orderDetail: order.getOrderDetails()){
                    quantityByMonths.merge(monthYear, orderDetail.getProductQuantity(), Integer::sum);
                }
            }
        }

        totalAmountAndQuantityByMonthsCus.put("totalAmount", totalAmountByMonths);
        totalAmountAndQuantityByMonthsCus.put("quantity", quantityByMonths);

        return totalAmountAndQuantityByMonthsCus;
    }

    public Map<String, Object>totalAmountAndQuantityForCustomerByQuarters(Long userId){
        Map<String, Object> monthlyData = this.totalAmountAndQuantityForCustomerByMonths(userId);

        //Lấy dữ liêu cho các Map Month
        Map<String, Double> totalAmountByMonths = (Map<String, Double>) monthlyData.get("totalAmount");
        Map<String, Integer> quantityByMonths = (Map<String, Integer>) monthlyData.get("quantity");

        //Tạo các Map cho Quarter
        Map<String, Double> totalAmountByQuarters = new HashMap<>();
        Map<String, Integer> quantityByQuarters = new HashMap<>();

        //revenue by Quarter
        for (Map.Entry<String, Double> entry: totalAmountByMonths.entrySet()){
            String monthYear = entry.getKey();
            Double totalAmount = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);//get from 0 to 11 (from Jan -> Dec)
            int year = Integer.parseInt(parts[1]);
            String monthString = parts[0];
            // Chuyển đổi tháng từ chuỗi sang số nguyên
            int month = this.getMonthMappingByKey(monthString);

            //Calculate the quarter
            int quarter = (month / 3) + 1;
            String quarterYear = "Q" + quarter + " " + year;

            totalAmountByQuarters.merge(quarterYear, totalAmount, Double::sum);
        }

        //quantity by Quarter
        for (Map.Entry<String, Integer> entry: quantityByMonths.entrySet()){
            String monthYear = entry.getKey();
            Integer quantity = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);
            int year = Integer.parseInt(parts[1]);
            String monthString = parts[0];
            // Chuyển đổi tháng từ chuỗi sang số nguyên
            int month = this.getMonthMappingByKey(monthString);
            int quarter = (month / 3) + 1;
            String quarterYear = "Q" + quarter + " " + year;

            quantityByQuarters.merge(quarterYear, quantity, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount", totalAmountByQuarters);
        result.put("quantity", quantityByQuarters);

        return result;
    }

    public Map<String, Object>totalAmountAndQuantityForCustomerByYears(Long userId){
        Map<String, Object> monthlyData = this.totalAmountAndQuantityForCustomerByMonths(userId);

        //Lấy dữ liêu cho các Map Month
        Map<String, Double> totalAmountByMonths = (Map<String, Double>) monthlyData.get("totalAmount");
        Map<String, Integer> quantityByMonths = (Map<String, Integer>) monthlyData.get("quantity");

        //Tạo các Map cho Quarter
        Map<String, Double> totalAmountByYears = new HashMap<>();
        Map<String, Integer> quantityByYears = new HashMap<>();

        //revenue by Quarter
        for (Map.Entry<String, Double> entry: totalAmountByMonths.entrySet()){
            String monthYear = entry.getKey();
            Double totalAmount = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);//get from 0 to 11 (from Jan -> Dec)
            String year = parts[1];

            totalAmountByYears.merge(year, totalAmount, Double::sum);
        }

        //quantity by Quarter
        for (Map.Entry<String, Integer> entry: quantityByMonths.entrySet()){
            String monthYear = entry.getKey();
            Integer quantity = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);
            String year = parts[1];

            quantityByYears.merge(year, quantity, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalAmount", totalAmountByYears);
        result.put("quantity", quantityByYears);

        return result;
    }


//////////////////////////////////////////////////////////////*CATEGORY*////////////////////////////////////////////////////////////////////

    public Map<String, Object> totalRevenueAndQuantityForCategoryByMonths(Long categoryId){
        // Create a map to store revenue and quantity per month
        Map<String, Object> revenueAndQuantityForCategoryByMonths = new HashMap<>();

        Category category = this.categoryService.findById(categoryId);
        if (category == null) {
            System.out.println("Category in the totalRevenueAndQuantityByMonths()  is null!!!");
            revenueAndQuantityForCategoryByMonths.put("null", "Category in the totalRevenueAndQuantityByMonths()  is null!!!");
            return revenueAndQuantityForCategoryByMonths;
        }

        // Create two maps to store revenue and quantity
        Map<String, Double> revenueByMonths = new HashMap<>();
        Map<String, Integer> quantityByMonths = new HashMap<>();

        //Lấy tất cả sản phẩm đang  được quản lí bởi category này
        List<Product> productsBySeller = category.getProducts();

        //Lấy all orders đang tồn tại
        List<Order> orders = this.orderService.findAll();

        for (Order order: orders){
            Date receivedDate = order.getReceivedDate();
            if (receivedDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receivedDate);

                String monthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + calendar.get(Calendar.YEAR);

                //map.merge(key, value, remappingFunction);
                // nếu key đã có trong map thì map sẽ lấy giá trị đã tồn tại ra để tính toán với giá trị mới được đưa vào, 'remappingFunction': hàm tính toán, vd:Sum

                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    Product product = orderDetail.getProduct();
                    if (productsBySeller.contains(product)) {
                        //get Revenue by months
                        revenueByMonths.merge(monthYear, (orderDetail.getProductQuantity() * orderDetail.getProduct().getPrice()), Double::sum);
                        //get Quantity by months
                        quantityByMonths.merge(monthYear, orderDetail.getProductQuantity(), Integer::sum);
                    }
                }
            }
        }

        revenueAndQuantityForCategoryByMonths.put("revenue", revenueByMonths);
        revenueAndQuantityForCategoryByMonths.put("quantity", quantityByMonths);

        return revenueAndQuantityForCategoryByMonths;
    }

    public Map<String, Object>totalRevenueAndQuantityForCategoryByQuarters(Long categoryId){
        Map<String, Object> monthlyData = this.totalRevenueAndQuantityForCategoryByMonths(categoryId);

        //Lấy dữ liêu cho các Map Month
        Map<String, Double> revenueByMonths = (Map<String, Double>) monthlyData.get("revenue");
        Map<String, Integer> quantityByMonths = (Map<String, Integer>) monthlyData.get("quantity");

        //Tạo các Map cho Quarter
        Map<String, Double> revenueByQuarters = new HashMap<>();
        Map<String, Integer> quantityByQuarters = new HashMap<>();

        //revenue by Quarter
        for (Map.Entry<String, Double> entry: revenueByMonths.entrySet()){
            String monthYear = entry.getKey();
            Double revenue = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);//get from 0 to 11 (from Jan -> Dec)
            int year = Integer.parseInt(parts[1]);
            String monthString = parts[0];
            // Chuyển đổi tháng từ chuỗi sang số nguyên
            int month = this.getMonthMappingByKey(monthString);

            //Calculate the quarter
            int quarter = (month / 3) + 1;
            String quarterYear = "Q" + quarter + " " + year;

            revenueByQuarters.merge(quarterYear, revenue, Double::sum);
        }

        //quantity by Quarter
        for (Map.Entry<String, Integer> entry: quantityByMonths.entrySet()){
            String monthYear = entry.getKey();
            Integer quantity = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);
            int year = Integer.parseInt(parts[1]);
            String monthString = parts[0];
            // Chuyển đổi tháng từ chuỗi sang số nguyên
            int month = this.getMonthMappingByKey(monthString);
            int quarter = (month / 3) + 1;
            String quarterYear = "Q" + quarter + " " + year;

            quantityByQuarters.merge(quarterYear, quantity, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("revenue", revenueByQuarters);
        result.put("quantity", quantityByQuarters);

        return result;
    }


    public Map<String, Object>totalRevenueAndQuantityForCategoryByYears(Long categoryId){
        Map<String, Object> monthlyData = this.totalRevenueAndQuantityForCategoryByMonths(categoryId);

        //Lấy dữ liêu cho các Map Month
        Map<String, Double> revenueByMonths = (Map<String, Double>) monthlyData.get("revenue");
        Map<String, Integer> quantityByMonths = (Map<String, Integer>) monthlyData.get("quantity");

        //Tạo các Map cho Quarter
        Map<String, Double> revenueByYears = new HashMap<>();
        Map<String, Integer> quantityByYears = new HashMap<>();

        //revenue by Quarter
        for (Map.Entry<String, Double> entry: revenueByMonths.entrySet()){
            String monthYear = entry.getKey();
            Double revenue = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);//get from 0 to 11 (from Jan -> Dec)
            String year = parts[1];

            revenueByYears.merge(year, revenue, Double::sum);
        }

        //quantity by Quarter
        for (Map.Entry<String, Integer> entry: quantityByMonths.entrySet()){
            String monthYear = entry.getKey();
            Integer quantity = entry.getValue();
            String[] parts = monthYear.split(" ");
//            int month = Calendar.getInstance().get(Calendar.MONTH);
            String year = parts[1];

            quantityByYears.merge(year, quantity, Integer::sum);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("revenue", revenueByYears);
        result.put("quantity", quantityByYears);

        return result;
    }








}
































