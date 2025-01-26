CREATE TRIGGER before_insert_employee
BEFORE INSERT ON employee
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(employeeId, 2) AS UNSIGNED)), 0) + 1 FROM employee);
    SET NEW.employeeId = CONCAT('E', LPAD(next_id, 4, '0'));
END;