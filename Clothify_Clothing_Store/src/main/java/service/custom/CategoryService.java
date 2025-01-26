package service.custom;

import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Category;
import service.SuperService;

public interface CategoryService extends SuperService {
    ObservableList<Category> getAllCategories() throws RepositoryException;

    Category findByCategoryId(String categoryId) throws RepositoryException;

    void updateCategory(Category selectedCategoryToEdit) throws RepositoryException;

    void deleteCategory(String categoryId) throws RepositoryException;

    boolean isCategoryNameAvailable(String name) throws RepositoryException;

    void addCategory(Category category) throws RepositoryException;
}
