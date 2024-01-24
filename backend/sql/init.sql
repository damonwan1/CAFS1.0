SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for forecast_result
-- ----------------------------
DROP TABLE IF EXISTS `forecast_result`;
CREATE TABLE `forecast_result`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `site_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Site_code',
                                    `year` int NOT NULL DEFAULT 0 COMMENT 'Year',
                                    `month` int NOT NULL DEFAULT 0 COMMENT 'Month',
                                    `day` int NOT NULL DEFAULT 0 COMMENT 'Day',
                                    `ta` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Ta',
                                    `par` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'PAR',
                                    `rh` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'RH',
                                    `vpd` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'VPD',
                                    `swc` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SWC',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 176112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预测结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for history_drive
-- ----------------------------
DROP TABLE IF EXISTS `history_drive`;
CREATE TABLE `history_drive`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `site_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Site_code',
                                  `year` int NOT NULL DEFAULT 0 COMMENT 'Year',
                                  `month` int NOT NULL DEFAULT 0 COMMENT 'Month',
                                  `day` int NOT NULL DEFAULT 0 COMMENT 'Day',
                                  `ta` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Ta',
                                  `par` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'PAR',
                                  `rh` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'RH',
                                  `swc` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SWC',
                                  `mock` tinyint(1) NULL DEFAULT NULL COMMENT 'mock',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76413 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '历史数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation`  (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间',
                              `longitude` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                              `latitude` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                              `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                              `city` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                              `open` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                              `code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for show_result
-- ----------------------------
DROP TABLE IF EXISTS `show_result`;
CREATE TABLE `show_result`  (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `site_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Site_code',
                                `year` int NOT NULL DEFAULT 0 COMMENT 'Year',
                                `month` int NOT NULL DEFAULT 0 COMMENT 'Month',
                                `day` int NOT NULL DEFAULT 0 COMMENT 'Day',
                                `gpp` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'GPP',
                                `npp` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'NPP',
                                `nep` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'NEP',
                                `ra` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Ra',
                                `rh` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Rh',
                                `cf` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Cf',
                                `cw` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Cw',
                                `cr` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Cr',
                                `cveg` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Cveg',
                                `csom` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Csom',
                                `af` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Af',
                                `aw` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Aw',
                                `ar` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Ar',
                                `tveg` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Tveg',
                                `tsoil` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Tsoil',
                                `swc` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'SWC',
                                `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11131279 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预测结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for show_statistics
-- ----------------------------
DROP TABLE IF EXISTS `show_statistics`;
CREATE TABLE `show_statistics`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `site_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Site_code',
                                    `statistics_date` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'statistics_date',
                                    `nep_max` double NULL DEFAULT NULL COMMENT 'nep_max',
                                    `nep_min` double NULL DEFAULT NULL COMMENT 'nep_min',
                                    `npp_max` double NULL DEFAULT NULL COMMENT 'npp_max',
                                    `npp_min` double NULL DEFAULT NULL COMMENT 'npp_min',
                                    `gpp_max` double NULL DEFAULT NULL COMMENT 'gpp_max',
                                    `gpp_min` double NULL DEFAULT NULL COMMENT 'gpp_min',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107455 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for zoology_history_drive
-- ----------------------------
DROP TABLE IF EXISTS `zoology_history_drive`;
CREATE TABLE `zoology_history_drive`  (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `site_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Site_code',
                                          `year` int NOT NULL DEFAULT 0 COMMENT 'Year',
                                          `month` int NOT NULL DEFAULT 0 COMMENT 'Month',
                                          `day` int NOT NULL DEFAULT 0 COMMENT 'Day',
                                          `nep` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Ta',
                                          `gpp` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'PAR',
                                          `npp` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'RH',
                                          `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                          `mock` tinyint(1) NULL DEFAULT NULL COMMENT 'mock',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54079 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '历史数据表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
