CREATE TRIGGER before_insert_category
BEFORE INSERT ON category
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    SET next_id = (SELECT IFNULL(MAX(CAST(SUBSTRING(categoryId, 2) AS UNSIGNED)), 0) + 1 FROM category);
    SET NEW.categoryId = CONCAT('C', LPAD(next_id, 4, '0'));
END;