CREATE TRIGGER before_insert_orders
BEFORE INSERT ON orders
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(orderId, 2) AS UNSIGNED)), 0) + 1 FROM orders);
    SET NEW.orderId = CONCAT('O', LPAD(next_id, 4, '0'));
END;