-- Insert data into the 'supplier' table
INSERT INTO supplier (id, company, email, name, supplierID) VALUES
  (1, 'Nike', 'nike@nike.com', 'Nike Inc.','S001'),
  (2, 'Adidas', 'adidas@adidas.com', 'Adidas AG','S002'),
  (3, 'Puma', 'puma@puma.com', 'Puma SE','S003'),
  (4, 'Under Armour', 'underarmour@underarmour.com', 'Under Armour, Inc.','S004');

-- Insert data into the 'category' table
INSERT INTO category (id, categoryID, name) VALUES
  (1,'C001', 'Shoes'),
  (2,'C002', 'Apparel'),
  (3,'C003', 'Accessories');

-- Insert data into the 'product' table
INSERT INTO product (id, name, productID, quantity, unitPrice, category_id, supplier_id) VALUES
  (1, 'Nike Air Max 1','P001', 100, 120.00, 'C001', 'S001'),
  (2, 'Adidas Ultraboost','P002', 150, 180.00, 'C001', 'S002'),
  (3, 'Puma Suede','P003', 80, 80.00, 'C001', 'S003'),
  (4, 'Under Armour Curry 8','P004', 120, 150.00, 'C001', 'S004'),
  (5, 'Nike Dri-FIT T-Shirt','P005', 200, 25.00, 'C002', 'S001'),
  (6, 'Adidas Originals Hoodie','P006', 180, 50.00, 'C002', 'S002'),
  (7, 'Puma Track Pants','P007', 150, 40.00, 'C002', 'S003'),
  (8, 'Under Armour Tech Shorts','P008', 100, 30.00, 'C002', 'S002');