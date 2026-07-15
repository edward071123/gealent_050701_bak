CREATE DATABASE IF NOT EXISTS library_mysql
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE library_mysql;

DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    UNIQUE KEY uk_categories_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    UNIQUE KEY uk_items_category_name (category_id, name),
    UNIQUE KEY uk_items_id_category (id, category_id),
    CONSTRAINT fk_items_category
        FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    borrow_user VARCHAR(100) NULL,
    category_id INT NOT NULL,
    item_id INT NOT NULL,
    CONSTRAINT fk_books_category
        FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_books_item
        FOREIGN KEY (item_id, category_id)
        REFERENCES items (id, category_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO categories (id, code, name) VALUES
    (1, 'novel', '小說類'),
    (2, 'programming', '程式類')
ON DUPLICATE KEY UPDATE
    code = VALUES(code),
    name = VALUES(name);

INSERT INTO items (id, category_id, name) VALUES
    (1, 2, 'Java'),
    (2, 2, 'Python'),
    (3, 1, '奇幻'),
    (4, 1, '歷史')
ON DUPLICATE KEY UPDATE
    category_id = VALUES(category_id),
    name = VALUES(name);

INSERT INTO books (id, title, author, available, borrow_user, category_id, item_id) VALUES
    (1, 'Java入門', '張三', FALSE, 'aa', 2, 1),
    (2, 'Python程式設計', '王五', TRUE, 'NULL', 2, 2),
    (3, '哈利波特', 'J.K.Rowling', TRUE, 'NULL', 1, 3),
    (4, '達文西密碼', '丹布朗', TRUE, 'NULL', 1, 4)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    author = VALUES(author),
    available = VALUES(available),
    borrow_user = VALUES(borrow_user),
    category_id = VALUES(category_id),
    item_id = VALUES(item_id);
