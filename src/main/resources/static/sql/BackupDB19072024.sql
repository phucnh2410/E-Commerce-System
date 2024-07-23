-- MySQL dump 10.13  Distrib 8.0.26, for macos11 (x86_64)
--
-- Host: localhost    Database: e_commerce_system
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `ward_and_commune` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1fa36y2oqhao3wgg2rw1pi459` (`user_id`),
  CONSTRAINT `FK1fa36y2oqhao3wgg2rw1pi459` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (2,'Can Tho','Vietnam','Ninh Kieu','0355548621','Nguyen Van Linh',7,NULL),(3,'Can Tho','Vietnam','Ninh Kieu','0366648627','NVL',7,'Hung Loi'),(4,'Kien Giang','Viet Nam','Giong Rieng','0366648528','Huynh Ky',7,'Thanh Phuoc'),(5,'Binh Duong','Vietnam','Ben Cat','0366648526','N5',7,'My Phuoc');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (2,'Printed and digital books','Books'),(3,'Men and women clothing','Clothing'),(4,'Household and kitchen items','Home & Kitchen'),(7,'Toys and games for children','Toys & Games'),(11,'A powerful and versatile portable electronic device.','Laptop'),(12,'A smart mobile device with many features.','Smartphone'),(13,'A lightweight and convenient electronic device.','Tablet'),(14,'Things that can replace and repair electronic devices such as laptops,...','Electronic components'),(15,'Various types of shoes and sandals for different occasions.','Footwear'),(16,'A device to measure and keep track of time, often worn on the wrist.','Watch'),(17,'Decorative items worn for personal adornment, such as rings, necklaces, and bracelets.','Jewelry');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `end_date` date DEFAULT NULL,
  `price_is_reduced` double DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedbacks`
--

DROP TABLE IF EXISTS `feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedbacks` (
  `feedback_date` date DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKti2ywtwc29ys1i591rmmaveyc` (`product_id`),
  KEY `FK312drfl5lquu37mu4trk8jkwx` (`user_id`),
  CONSTRAINT `FK312drfl5lquu37mu4trk8jkwx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKti2ywtwc29ys1i591rmmaveyc` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `product_quantity` int DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `FKc7q42e9tu0hslx6w4wxgomhvn` (`product_id`),
  CONSTRAINT `FKc7q42e9tu0hslx6w4wxgomhvn` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKrws2q0si6oyd6il8gqe2aennc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `delivered_date` date DEFAULT NULL,
  `ordered_date` date DEFAULT NULL,
  `received_date` date DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `payment_method_id` bigint DEFAULT NULL,
  `voucher_id` bigint DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  KEY `FKa03ljb6t6oa6mqtoifuwkb0kw` (`payment_method_id`),
  KEY `FK7dgv2k9krjucwoeetftok0r1o` (`voucher_id`),
  KEY `FKhlglkvf5i60dv6dn397ethgpt` (`address_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK7dgv2k9krjucwoeetftok0r1o` FOREIGN KEY (`voucher_id`) REFERENCES `vouchers` (`id`),
  CONSTRAINT `FKa03ljb6t6oa6mqtoifuwkb0kw` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`),
  CONSTRAINT `FKhlglkvf5i60dv6dn397ethgpt` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`),
  CONSTRAINT `orders_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_methods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_methods`
--

LOCK TABLES `payment_methods` WRITE;
/*!40000 ALTER TABLE `payment_methods` DISABLE KEYS */;
INSERT INTO `payment_methods` VALUES (1,'Cash on Delivery\n'),(2,'Credit Card'),(3,'PayPal'),(4,'Bank Transfer'),(5,'Google Pay'),(6,'Apple Pay');
/*!40000 ALTER TABLE `payment_methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `price` double DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `product_image` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `discount_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  KEY `FK5cyj7v7fvm60i2ubciqfo2wxm` (`discount_id`),
  KEY `FKdb050tk37qryv15hd932626th` (`user_id`),
  CONSTRAINT `FK5cyj7v7fvm60i2ubciqfo2wxm` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`id`),
  CONSTRAINT `FKdb050tk37qryv15hd932626th` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1100,99,23,'Apple','The iPhone is a line of smartphones designed by Apple Inc. Known for its sleek design, powerful performance, and user-friendly iOS, the iPhone offers features like high-quality cameras, seamless connectivity, and a vast ecosystem of apps','iPhone 14 Pro Max','ip14.webp',12,NULL,6),(600,99,24,'Apple','The iPhone is a line of smartphones designed by Apple Inc. Known for its sleek design, powerful performance, and user-friendly iOS, the iPhone offers features like high-quality cameras, seamless connectivity, and a vast ecosystem of apps','iPhone 13','ip13.jpeg',12,NULL,6),(650,99,25,'Apple','The iPhone is a line of smartphones designed by Apple Inc. Known for its sleek design, powerful performance, and user-friendly iOS, the iPhone offers features like high-quality cameras, seamless connectivity, and a vast ecosystem of apps','iPhone 12 Pro Max','ip12.jpg',12,NULL,6),(300,99,26,'Apple','The iPhone is a line of smartphones designed by Apple Inc. Known for its sleek design, powerful performance, and user-friendly iOS, the iPhone offers features like high-quality cameras, seamless connectivity, and a vast ecosystem of apps','iPhone 11','ip11.webp',12,NULL,6),(200,99,27,'Apple','The iPhone is a line of smartphones designed by Apple Inc. Known for its sleek design, powerful performance, and user-friendly iOS, the iPhone offers features like high-quality cameras, seamless connectivity, and a vast ecosystem of apps','iPhone Xs','ipXs.jpg',12,NULL,6),(200,99,28,'Nike','Nike is a global brand renowned for athletic footwear, apparel, and equipment. Known for innovation and quality, Nike products cater to athletes and fitness enthusiasts worldwide, offering comfort, performance, and style.','Air Force 1','nikeJordanAir.jpg',15,NULL,5),(250,99,29,'Nike','Nike is a global brand renowned for athletic footwear, apparel, and equipment. Known for innovation and quality, Nike products cater to athletes and fitness enthusiasts worldwide, offering comfort, performance, and style.','Air Max','nikeAirMax.jpg',15,NULL,5),(300,99,30,'Nike','Nike is a global brand renowned for athletic footwear, apparel, and equipment. Known for innovation and quality, Nike products cater to athletes and fitness enthusiasts worldwide, offering comfort, performance, and style.','Roshe Run','nikeRosheRun.jpg',15,NULL,5),(2000,99,31,'Apple','Apple\'s MacBook features a sleek, lightweight design with a stunning Retina display, powerful M1 chip, and all-day battery life. Perfect for professionals and students, it combines performance, portability, and style.','Macbook Pro 16 inch 2021 M1 Max','macbookPro16inch2021.webp',11,NULL,6),(1700,99,32,'Apple','Apple\'s MacBook features a sleek, lightweight design with a stunning Retina display, powerful M1 chip, and all-day battery life. Perfect for professionals and students, it combines performance, portability, and style.','Macbook Pro 14 inch 2021 M1 Pro','macbookPro14inch2021.webp',11,NULL,6),(1000,99,34,'Apple','The iPhone is a line of smartphones designed by Apple Inc. Known for its sleek design, powerful performance, and user-friendly iOS, the iPhone offers features like high-quality cameras, seamless connectivity, and a vast ecosystem of apps','iPhone 15','ip15.jpeg',12,NULL,6),(1000,99,36,'Apple','Apple\'s MacBook features a sleek, lightweight design with a stunning Retina display, powerful M1 chip, and all-day battery life. Perfect for professionals and students, it combines performance, portability, and style.','Macbook Pro 16 inch 2019 Intel Core i9','macbookPro16-inch2019.jpg',11,NULL,6);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_CUSTOMER'),(3,'ROLE_SELLER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `date_of_birth` date DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('2024-06-22',1,'z5109467921412_d362edfd11c3d052e9ace49e38f7a2e8 - Copy.png','phuc97336@gmail.com','Ngo Huynh Phuc','$2a$10$gDqFDdvYyOehq6RHvayACuxE8v25JffPlDXbiZ7ilsIUQPzjdnHRu','0355548621',1),('2024-07-10',5,'NikeLogo.jpg','nike@gmail.com','Nike','$2a$10$LtED/clv7RAl9wKNnGBAbOtYJepFuKfwV0cTUpDbHCfRLID19L2uu','0355548621',3),('2024-07-20',6,'AppleLogo.png','apple@gmail.com','Apple Store','$2a$10$oZqJNVlOTttViMGo/hqKMeIywp3G9EUq.HPdLJeQmNfZGFJNqeIL.','0999999999',3),('2024-07-21',7,'corgiDog.jpg','khangngo@gmail.com','Khang Ngo','$2a$10$mSvdt6Fwi3tvqSwucRs0ZO3KBEqJsMNTOLugHc0.s6EMaIbMmAaJ6','',2),(NULL,8,NULL,'customer@gmail.com','Customer','$2a$10$QHmLtcQTIxCQyssCA2B0/e2HuIHoGGRAUosTYmlaPZ60JAEbLxgn2',NULL,2),(NULL,9,NULL,'customer2@gmail.com','Customer 2','$2a$10$asCE37BxBF8Ege1YX4WHNupJV1/5IHXmGH8J4eiF1wqVCnKZKSpS2',NULL,2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vouchers`
--

DROP TABLE IF EXISTS `vouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vouchers` (
  `percentage_is_reduced` int DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vouchers`
--

LOCK TABLES `vouchers` WRITE;
/*!40000 ALTER TABLE `vouchers` DISABLE KEYS */;
INSERT INTO `vouchers` VALUES (10,1,'10% off discount','2024-07-18',100),(20,2,'20% off discount','2024-07-18',100),(50,4,'Buy one get one free','2024-07-18',100),(15,5,'15% off on all electronics','2024-07-18',100),(25,6,'25% off on your next purchase','2024-07-18',100),(30,7,'30% off on selected items','2024-07-18',100),(5,9,'5% off for new customers','2024-07-18',100),(40,10,'40% off on clearance items','2024-07-18',100),(50,12,'50% off on second item','2024-07-18',100),(10,13,'10% off for students','2024-07-18',100),(33,14,'Buy 2 get 1 free','2024-07-18',100),(35,16,'Special 35% off weekend sale','2024-07-18',100),(10,17,'Extra 10% off with promo code','2024-07-18',100),(25,18,'25% off on holiday sale','2024-07-18',100),(45,20,'Exclusive 45% off for members','2024-07-18',100);
/*!40000 ALTER TABLE `vouchers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-19 13:00:18
