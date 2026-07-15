CREATE DATABASE IF NOT EXISTS car_rental_mysql
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE car_rental_mysql;

DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS cars (
    id INT PRIMARY KEY,
    plate VARCHAR(255) NOT NULL, -- 車牌號碼
    brand VARCHAR(100) NOT NULL, -- 品牌
    available BOOLEAN NOT NULL DEFAULT TRUE,
    borrow_user VARCHAR(100) NULL,
    category_id INT NOT NULL,
    CONSTRAINT fk_cars_category
        FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO categories (id, name) VALUES
    (1, '休旅車'),
    (2, '轎車'),
    (3, '貨車')
ON DUPLICATE KEY UPDATE
    name = VALUES(name);

INSERT INTO cars (id, plate, brand, available, borrow_user, category_id) VALUES
    (1, 'AAA-0001', 'BMW', FALSE, 'aa', 2),
    (2, 'BBB-0002', 'Benz', TRUE, 'NULL', 2),
    (3, 'CCC-0003', 'Ford', TRUE, 'NULL', 3),
    (4, 'DDD-0004', 'Toyota', TRUE, 'NULL', 1)
ON DUPLICATE KEY UPDATE
    plate = VALUES(plate),
    brand = VALUES(brand),
    available = VALUES(available),
    borrow_user = VALUES(borrow_user),
    category_id = VALUES(category_id)