CREATE TABLE admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    adminId VARCHAR(50) NOT NULL UNIQUE,
    firstName VARCHAR(100),
    lastName VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    userName VARCHAR(100) NOT NULL UNIQUE,
    phoneNumber VARCHAR(15),
    password VARCHAR(100) NOT NULL,
    INDEX (adminId)
);