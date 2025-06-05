SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ads_book_popularity_rank
-- ----------------------------
DROP TABLE IF EXISTS `ads_book_popularity_rank`;
CREATE TABLE `ads_book_popularity_rank`  (
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ads_book_popularity_rank
-- ----------------------------
INSERT INTO `ads_book_popularity_rank` VALUES ('0-99', 5884);
INSERT INTO `ads_book_popularity_rank` VALUES ('100-199', 3141);
INSERT INTO `ads_book_popularity_rank` VALUES ('200-299', 1842);
INSERT INTO `ads_book_popularity_rank` VALUES ('300-399', 1055);
INSERT INTO `ads_book_popularity_rank` VALUES ('400-499', 837);
INSERT INTO `ads_book_popularity_rank` VALUES ('500-599', 692);
INSERT INTO `ads_book_popularity_rank` VALUES ('600-699', 598);
INSERT INTO `ads_book_popularity_rank` VALUES ('700-799', 508);
INSERT INTO `ads_book_popularity_rank` VALUES ('800-899', 396);
INSERT INTO `ads_book_popularity_rank` VALUES ('900-999', 302);
INSERT INTO `ads_book_popularity_rank` VALUES ('1000-1099', 235);
INSERT INTO `ads_book_popularity_rank` VALUES ('1100-1199', 196);
INSERT INTO `ads_book_popularity_rank` VALUES ('1200-1299', 181);
INSERT INTO `ads_book_popularity_rank` VALUES ('1300-1399', 120);
INSERT INTO `ads_book_popularity_rank` VALUES ('1400-1499', 119);
INSERT INTO `ads_book_popularity_rank` VALUES ('1500-1599', 96);
INSERT INTO `ads_book_popularity_rank` VALUES ('1600-1699', 87);
INSERT INTO `ads_book_popularity_rank` VALUES ('1700-1799', 53);
INSERT INTO `ads_book_popularity_rank` VALUES ('1800-1899', 46);
INSERT INTO `ads_book_popularity_rank` VALUES ('1900-1999', 48);
INSERT INTO `ads_book_popularity_rank` VALUES ('2000以上', 280);

-- ----------------------------
-- Table structure for ads_category_count_analysis
-- ----------------------------
DROP TABLE IF EXISTS `ads_category_count_analysis`;
CREATE TABLE `ads_category_count_analysis`  (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ads_category_count_analysis
-- ----------------------------
INSERT INTO `ads_category_count_analysis` VALUES ('A生命周期男性', 151);
INSERT INTO `ads_category_count_analysis` VALUES ('B生命周期男性', 232);
INSERT INTO `ads_category_count_analysis` VALUES ('C生命周期男性', 354);
INSERT INTO `ads_category_count_analysis` VALUES ('A生命周期女性', 2099);
INSERT INTO `ads_category_count_analysis` VALUES ('B生命周期女性', 3533);
INSERT INTO `ads_category_count_analysis` VALUES ('C生命周期女性', 11084);

-- ----------------------------
-- Table structure for ads_discount_ratio_analysis
-- ----------------------------
DROP TABLE IF EXISTS `ads_discount_ratio_analysis`;
CREATE TABLE `ads_discount_ratio_analysis`  (
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ads_discount_ratio_analysis
-- ----------------------------
INSERT INTO `ads_discount_ratio_analysis` VALUES ('0-100元', 57);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('2', 57);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('100-500元', 258);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('4', 240);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('500-1000元', 701);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('6', 715);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('1000元以上', 2079);
INSERT INTO `ads_discount_ratio_analysis` VALUES ('8', 1638);

-- ----------------------------
-- Table structure for ads_price_distribution_count
-- ----------------------------
DROP TABLE IF EXISTS `ads_price_distribution_count`;
CREATE TABLE `ads_price_distribution_count`  (
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ads_price_distribution_count
-- ----------------------------
INSERT INTO `ads_price_distribution_count` VALUES ('仅在自营商品中购买用户数', 734);
INSERT INTO `ads_price_distribution_count` VALUES ('第三方店铺有购买记录的用户数', 15982);

-- ----------------------------
-- Table structure for ads_publish_trend_analysis
-- ----------------------------
DROP TABLE IF EXISTS `ads_publish_trend_analysis`;
CREATE TABLE `ads_publish_trend_analysis`  (
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ads_publish_trend_analysis
-- ----------------------------
INSERT INTO `ads_publish_trend_analysis` VALUES ('0-4天', 36);
INSERT INTO `ads_publish_trend_analysis` VALUES ('4-8天', 70);
INSERT INTO `ads_publish_trend_analysis` VALUES ('8-12天', 98);
INSERT INTO `ads_publish_trend_analysis` VALUES ('12-16天', 40);
INSERT INTO `ads_publish_trend_analysis` VALUES ('16-20天', 15);
INSERT INTO `ads_publish_trend_analysis` VALUES ('20-24天', 13);
INSERT INTO `ads_publish_trend_analysis` VALUES ('24天以上', 10);

-- ----------------------------
-- Table structure for ads_publisher_popularity_rank
-- ----------------------------
DROP TABLE IF EXISTS `ads_publisher_popularity_rank`;
CREATE TABLE `ads_publisher_popularity_rank`  (
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of ads_publisher_popularity_rank
-- ----------------------------
INSERT INTO `ads_publisher_popularity_rank` VALUES ('人民邮电出版社', 212);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('北京联合出版有限公司', 161);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('清华大学出版社', 133);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('江苏凤凰文艺出版社', 131);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('湖南文艺出版社', 127);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('中信出版社', 125);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('机械工业出版社', 107);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('浙江教育出版社', 72);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('天津人民出版社', 70);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('商务印书馆', 67);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('外语教学与研究出版社', 67);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('世界图书出版公司', 66);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('台海出版社', 60);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('人民文学出版社', 58);
INSERT INTO `ads_publisher_popularity_rank` VALUES ('化学工业出版社', 58);

-- ----------------------------
-- Table structure for clearbook
-- ----------------------------
DROP TABLE IF EXISTS `clearbook`;
CREATE TABLE `clearbook`  (
  `1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of clearbook
-- ----------------------------

-- ----------------------------
-- Table structure for reader
-- ----------------------------
DROP TABLE IF EXISTS `reader`;
CREATE TABLE `reader`  (
  `userid` int NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `readername` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `readersex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `readertype` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` int NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of reader
-- ----------------------------
INSERT INTO `reader` VALUES (10010, '2665923759@qq.com', '李承旭', '男', '用户', 'user', '123456', 0);
INSERT INTO `reader` VALUES (10020, '23333@163.com', '郭子铭', '男', '管理员', 'admin', '123456', 1);
INSERT INTO `reader` VALUES (10086, 'acccc@163.com', '上官', '男', '用户', 'user3', '123456', 0);

SET FOREIGN_KEY_CHECKS = 1;
