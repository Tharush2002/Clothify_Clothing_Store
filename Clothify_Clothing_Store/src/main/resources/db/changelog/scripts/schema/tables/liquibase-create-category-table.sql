CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoryId VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100),
    INDEX (categoryId)
);

