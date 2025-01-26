package service.custom.impl;

import entity.CategoryEntity;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import repository.RepositoryFactory;
import repository.custom.CategoryRepository;
import service.custom.CategoryService;
import util.Type;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository= RepositoryFactory.getInstance().getRepositoryType(Type.CATEGORY);

    @Override
    public ObservableList<Category> getAllCategories() throws RepositoryException {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        ObservableList<Category> categoryObservableList= FXCollections.observableArrayList();
        if (categoryEntityList!=null){
            categoryEntityList.forEach(entity-> categoryObservableList.add(new Category(entity.getCategoryId(), entity.getName())));
        }
        return categoryObservableList;
    }

    @Override
    public Category findByCategoryId(String categoryId) throws RepositoryException {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryID(categoryId);
        return new Category(categoryEntity.getCategoryId(),categoryEntity.getName());
    }

    @Override
    public void updateCategory(Category selectedCategoryToEdit) throws RepositoryException {
        categoryRepository.update(new CategoryEntity(null, selectedCategoryToEdit.getCategoryId(), selectedCategoryToEdit.getName()));
    }

    @Override
    public void deleteCategory(String categoryId) throws RepositoryException {
        categoryRepository.deleteByCategoryId(categoryId);
    }

    @Override
    public boolean isCategoryNameAvailable(String name) throws RepositoryException {
        return categoryRepository.findByName(name) != null;
    }

    @Override
    public void addCategory(Category category) throws RepositoryException {
        categoryRepository.add(new CategoryEntity(null, null, category.getName()));
    }

}
