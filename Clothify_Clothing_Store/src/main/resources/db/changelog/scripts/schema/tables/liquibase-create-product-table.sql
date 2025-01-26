CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productId VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    category_id VARCHAR(50),
    quantity INT NOT NULL,
    unitPrice DOUBLE NOT NULL,
    supplier_id VARCHAR(50),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(categoryId),
    CONSTRAINT fk_product_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplierId)
);