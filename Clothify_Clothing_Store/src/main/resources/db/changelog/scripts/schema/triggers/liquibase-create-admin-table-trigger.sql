CREATE TRIGGER before_insert_admin
BEFORE INSERT ON admin
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(adminId, 2) AS UNSIGNED)), 0) + 1 FROM admin);
    SET NEW.adminId = CONCAT('A', LPAD(next_id, 4, '0'));
END;