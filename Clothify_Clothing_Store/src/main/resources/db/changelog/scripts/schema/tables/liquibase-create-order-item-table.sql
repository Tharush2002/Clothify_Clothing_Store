CREATE TABLE orderItems (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL ,
    productName VARCHAR(255) NOT NULL,
    productId VARCHAR(50) NOT NULL,
    categoryId VARCHAR(50),
    categoryName VARCHAR(255),
    supplierId VARCHAR(50),
    supplierName VARCHAR(255),
    unitPrice DOUBLE NOT NULL,
    size VARCHAR(50) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(orderId)
);