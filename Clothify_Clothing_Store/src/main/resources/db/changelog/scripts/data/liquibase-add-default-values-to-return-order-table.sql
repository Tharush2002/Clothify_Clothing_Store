-- Insert return orders
INSERT INTO returnorder (returnOrderId, order_id, productName, productId, categoryId, categoryName, supplierId, supplierName, unitPrice, size, returnDate)
VALUES
('R0001', 'O0001', 'Casual Shirt', 'P0001', 'C0001', 'Shirts', 'S0001', 'John Doe', 3500.00, 'L', '2025-01-20'),
('R0002', 'O0002', 'Formal Shirt', 'P0002', 'C0001', 'Shirts', 'S0002', 'Jane Smith', 4500.00, 'M', '2025-01-21'),
('R0003', 'O0003', 'Evening Dress', 'P0005', 'C0003', 'Dresses', 'S0005', 'David Brown', 12000.00, 'S', '2025-01-22'),
('R0004', 'O0004', 'Running Shoes', 'P0007', 'C0004', 'Shoes', 'S0007', 'Liam Martinez', 10000.00, '9', '2025-01-23'),
('R0005', 'O0005', 'Cocktail Dress', 'P0006', 'C0003', 'Dresses', 'S0006', 'Emma Wilson', 15000.00, 'M', '2025-01-24');