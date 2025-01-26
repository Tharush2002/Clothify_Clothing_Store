CREATE TRIGGER before_insert_supplier
BEFORE INSERT ON supplier
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(supplierId, 2) AS UNSIGNED)), 0) + 1 FROM supplier);
    SET NEW.supplierId = CONCAT('S', LPAD(next_id, 4, '0'));
END;