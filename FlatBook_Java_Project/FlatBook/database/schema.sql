-- FlatBook Database Schema (MySQL)
-- Run this if you prefer to create tables manually

CREATE DATABASE IF NOT EXISTS flatbook;
USE flatbook;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mobile VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE flats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    city VARCHAR(50) NOT NULL,
    locality VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    bhk_type VARCHAR(20) NOT NULL,
    area_sqft INT,
    price DECIMAL(12,2) NOT NULL,
    price_type VARCHAR(20) DEFAULT 'MONTHLY',
    furnished_status VARCHAR(50),
    amenities TEXT,
    image_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    owner_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id VARCHAR(30) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    flat_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    occupants INT DEFAULT 1,
    total_amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    special_requests VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cancelled_at DATETIME,
    cancellation_reason VARCHAR(300),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (flat_id) REFERENCES flats(id)
);

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id VARCHAR(40) NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_method VARCHAR(30),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    transaction_ref VARCHAR(100),
    paid_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);
