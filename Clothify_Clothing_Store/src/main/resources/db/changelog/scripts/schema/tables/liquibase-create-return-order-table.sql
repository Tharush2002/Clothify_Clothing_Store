CREATE TABLE returnOrder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    returnOrderId VARCHAR(50) NOT NULL UNIQUE,
    order_id VARCHAR(50) NOT NULL UNIQUE,
    productName VARCHAR(255) NOT NULL,
    productId VARCHAR(50) NOT NULL,
    categoryId VARCHAR(50) NOT NULL,
    categoryName VARCHAR(255) NOT NULL,
    supplierId VARCHAR(50) NOT NULL,
    supplierName VARCHAR(255) NOT NULL,
    unitPrice DOUBLE NOT NULL,
    size VARCHAR(50) NOT NULL,
    returnDate DATE NOT NULL,
    CONSTRAINT fk_return_order_order FOREIGN KEY (order_id) REFERENCES orders(orderId)
);