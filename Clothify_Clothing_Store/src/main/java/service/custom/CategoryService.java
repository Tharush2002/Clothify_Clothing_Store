package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Category;
import service.SuperService;

public interface CategoryService extends SuperService {
    ObservableList<Category> getAllCategories() throws RepositoryException;

    Category findCategoryByName(String name) throws RepositoryException;
}
