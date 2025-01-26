CREATE TRIGGER before_insert_customer
BEFORE INSERT ON customer
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(customerId, 2) AS UNSIGNED)), 0) + 1 FROM customer);
    SET NEW.customerId = CONCAT('U', LPAD(next_id, 4, '0'));
END;