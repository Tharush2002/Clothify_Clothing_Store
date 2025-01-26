CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customerId VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100),
    email VARCHAR(100),
    phoneNumber VARCHAR(20),
    INDEX (customerId)
);