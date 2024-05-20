create database triptogether;
use triptogether;
CREATE TABLE `Member` (
    `member_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` VARCHAR(30) NOT NULL,
    `member_pw` VARCHAR(50) NOT NULL,
    `alarm_status` TINYINT NOT NULL DEFAULT 1,
    `member_login_pw` VARCHAR(6) NULL,
    `member_name` VARCHAR(20) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `deleted_at` DATETIME(6) NULL,
    PRIMARY KEY (`member_idx`)
);
CREATE TABLE `Account` (
    `acc_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `member_idx` BIGINT NOT NULL,
    `acc_number` VARCHAR(255) NOT NULL,
    `acc_balance` DECIMAL(20, 2) NOT NULL DEFAULT 0,
    `card_status` TINYINT NOT NULL DEFAULT 0,
    `acc_name` VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `deleted_at` DATETIME(6) NULL,
    PRIMARY KEY (`acc_idx`),
    FOREIGN KEY (`member_idx`) REFERENCES `Member` (`member_idx`)
);
CREATE TABLE `Account_transaction_details` (
    `trans_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `acc_idx` BIGINT NOT NULL,
    `trans_amount` DECIMAL(20, 2) NOT NULL DEFAULT 0,
    `trans_type` ENUM('입금', '출금') NOT NULL,
    `trans_date` DATETIME(6) NOT NULL,
    `trans_name` VARCHAR(255) NULL,
    `trans_from` VARCHAR(255) NOT NULL,
    `trans_to` VARCHAR(255) NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`trans_idx`),
    FOREIGN KEY (`acc_idx`) REFERENCES `Account` (`acc_idx`)
);
CREATE TABLE `Team` (
    `team_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `acc_idx` BIGINT NOT NULL,
    `team_name` VARCHAR(30) NOT NULL,
    `team_type` ENUM('취미', '친구', '데이트', '여행') NOT NULL,
    `preference_type` ENUM('국내', '해외', '모두') NULL,
    `team_notice` VARCHAR(50) NULL,
    `prefer_trip` BIGINT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`team_idx`),
    FOREIGN KEY (`acc_idx`) REFERENCES `Account` (`acc_idx`)
);
CREATE TABLE `Team_member` (
    `team_member_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `team_idx` BIGINT NOT NULL,
    `member_idx` BIGINT NOT NULL,
    `team_member_state` ENUM('총무', '모임원', '요청중', '수락대기') NOT NULL DEFAULT '요청중',
    `created_at` DATETIME(6) NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`team_member_idx`),
    FOREIGN KEY (`team_idx`) REFERENCES `Team` (`team_idx`),
    FOREIGN KEY (`member_idx`) REFERENCES `Member` (`member_idx`)
);
CREATE TABLE `Country` (
    `country_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `country_name_ko` VARCHAR(100) NOT NULL,
    `country_name_eng` VARCHAR(100) NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`country_idx`)
);
CREATE TABLE `City` (
    `city_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `country_idx` BIGINT NOT NULL,
    `city_name_ko` VARCHAR(100) NOT NULL,
    `city_name_eng` VARCHAR(100) NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`city_idx`),
    FOREIGN KEY (`country_idx`) REFERENCES `Country` (`country_idx`)
);
CREATE TABLE `Place` (
    `place_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `city_idx` BIGINT NOT NULL,
    `place_name_ko` VARCHAR(100) NOT NULL,
    `place_name_eng` VARCHAR(255) NULL,
    `place_img` VARCHAR(255) NULL,
    `category_idx` BIGINT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`place_idx`),
    FOREIGN KEY (`city_idx`) REFERENCES `City` (`city_idx`)
);
CREATE TABLE `Dues` (
    `dues_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `team_idx` BIGINT NOT NULL,
    `dues_date` INT NOT NULL,
    `dues_amount` DECIMAL(20, 2) NOT NULL DEFAULT 0,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`dues_idx`),
    FOREIGN KEY (`team_idx`) REFERENCES `Team` (`team_idx`)
);
CREATE TABLE `Trip` (
    `trip_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `team_idx` BIGINT NOT NULL,
    `country_idx` BIGINT NOT NULL,
    `trip_name` VARCHAR(30) NOT NULL,
    `trip_content` VARCHAR(30) NULL,
    `trip_goal_amount` DECIMAL(20, 2) NOT NULL DEFAULT 0,
    `trip_day` INT NOT NULL DEFAULT 1,
    `trip_start_day` DATE NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`trip_idx`),
    FOREIGN KEY (`team_idx`) REFERENCES `Team` (`team_idx`),
    FOREIGN KEY (`country_idx`) REFERENCES `Country` (`country_idx`)
);
CREATE TABLE `Exchange_rate` (
    `cur_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `cur_cd` VARCHAR(10) NOT NULL,
    `rate` DECIMAL(20, 2) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `deleted_at` DATETIME(6) NULL,
    PRIMARY KEY (`cur_idx`)
);
CREATE TABLE `Exchange_rate_alarm` (
    `alarm_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `team_idx` BIGINT NOT NULL,
    `cur_idx` BIGINT NOT NULL,
    `max_rate` DECIMAL(20, 2) NOT NULL,
    `min_rate` DECIMAL(20, 2) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`alarm_idx`),
    FOREIGN KEY (`team_idx`) REFERENCES `Team` (`team_idx`),
    FOREIGN KEY (`cur_idx`) REFERENCES `Exchange_rate` (`cur_idx`)
);
CREATE TABLE `Trip_place` (
    `trip_place_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `trip_idx` BIGINT NOT NULL,
    `trip_date` INT NOT NULL,
    `place_order` INT NOT NULL,
    `place_idx` BIGINT NULL,
    `place_amount` DECIMAL(20, 2) NULL,
    `place_memo` VARCHAR(255) NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`trip_place_idx`),
    FOREIGN KEY (`trip_idx`) REFERENCES `Trip` (`trip_idx`)
);
CREATE TABLE `Trip_reply` (
    `trip_reply_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `trip_place_idx` BIGINT NOT NULL,
    `team_member_idx` BIGINT NOT NULL,
    `trip_reply_content` VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `deleted_at` DATETIME(6) NULL,
    PRIMARY KEY (`trip_reply_idx`),
    FOREIGN KEY (`trip_place_idx`) REFERENCES `Trip_place` (`trip_place_idx`),
    FOREIGN KEY (`team_member_idx`) REFERENCES `Team_member` (`team_member_idx`)
);
CREATE TABLE `Category` (
    `category_idx` BIGINT NOT NULL AUTO_INCREMENT,
    `category_name` VARCHAR(100) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `created_by` BIGINT NOT NULL,
    `last_modified_at` DATETIME(6) NULL,
    `last_modified_by` BIGINT NULL,
    `deleted_at` DATETIME(6) NULL,
    `deleted_by` BIGINT NULL,
    PRIMARY KEY (`category_idx`)
);