CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderId VARCHAR(50) NOT NULL UNIQUE,
    date DATE NOT NULL,
    time TIME NOT NULL,
    total DOUBLE NOT NULL,
    paymentType VARCHAR(50) NOT NULL,
    orderItemCount INT NOT NULL,
    customer_id VARCHAR(50),
    INDEX (orderId),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer(customerId)
);