CREATE TRIGGER before_insert_returnOrder
BEFORE INSERT ON returnOrder
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(returnOrderId, 2) AS UNSIGNED)), 0) + 1 FROM returnOrder);
    SET NEW.returnOrderId = CONCAT('R', LPAD(next_id, 4, '0'));
END;