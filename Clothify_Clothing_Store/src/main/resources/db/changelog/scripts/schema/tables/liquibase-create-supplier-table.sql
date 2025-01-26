CREATE TABLE supplier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    supplierId VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255),
    company VARCHAR(255),
    email VARCHAR(255)
);