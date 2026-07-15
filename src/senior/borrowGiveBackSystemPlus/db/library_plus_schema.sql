CREATE DATABASE IF NOT EXISTS library_plus
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE library_plus;

DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
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

CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(50) NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    password_hash CHAR(64) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE KEY uk_members_account (account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    borrow_member_id INT NULL,
    category_id INT NOT NULL,
    item_id INT NOT NULL,
    CONSTRAINT fk_books_borrow_member
        FOREIGN KEY (borrow_member_id)
        REFERENCES members (id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
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

INSERT INTO categories (id, name) VALUES
    (1, '小說類'),
    (2, '程式類')
ON DUPLICATE KEY UPDATE
    name = VALUES(name);

INSERT INTO items (id, category_id, name) VALUES
    (1, 2, 'Java'),
    (2, 2, 'Python'),
    (3, 1, '奇幻'),
    (4, 1, '歷史')
ON DUPLICATE KEY UPDATE
    category_id = VALUES(category_id),
    name = VALUES(name);

INSERT INTO members (id, account, display_name, password_hash, is_admin) VALUES
    (1, 'admin', '管理員', SHA2('admin123', 256), TRUE),
    (2, 'aa', 'AA會員', SHA2('aa123', 256), FALSE)
ON DUPLICATE KEY UPDATE
    account = VALUES(account),
    display_name = VALUES(display_name),
    password_hash = VALUES(password_hash),
    is_admin = VALUES(is_admin);

INSERT INTO books (id, title, author, available, borrow_member_id, category_id, item_id) VALUES
    (1, 'Java入門', '張三', FALSE, 2, 2, 1),
    (2, 'Python程式設計', '王五', TRUE, NULL, 2, 2),
    (3, '哈利波特', 'J.K.Rowling', TRUE, NULL, 1, 3),
    (4, '達文西密碼', '丹布朗', TRUE, NULL, 1, 4)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    author = VALUES(author),
    available = VALUES(available),
    borrow_member_id = VALUES(borrow_member_id),
    category_id = VALUES(category_id),
    item_id = VALUES(item_id);
