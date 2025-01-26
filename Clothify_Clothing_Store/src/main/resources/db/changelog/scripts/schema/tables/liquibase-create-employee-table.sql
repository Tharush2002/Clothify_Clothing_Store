CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employeeId VARCHAR(50) NOT NULL UNIQUE,
    firstName VARCHAR(100),
    lastName VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    phoneNumber VARCHAR(20),
    userName VARCHAR(100) NOT NULL UNIQUE,
    nic VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    INDEX (employeeId)
);