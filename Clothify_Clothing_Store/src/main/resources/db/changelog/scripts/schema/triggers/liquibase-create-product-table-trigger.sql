CREATE TRIGGER before_insert_product
BEFORE INSERT ON product
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(productId, 2) AS UNSIGNED)), 0) + 1 FROM product);
    SET NEW.productId = CONCAT('P', LPAD(next_id, 4, '0'));
END;