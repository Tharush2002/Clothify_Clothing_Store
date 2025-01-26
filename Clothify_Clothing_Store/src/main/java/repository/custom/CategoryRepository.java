package repository.custom;

import entity.CategoryEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface CategoryRepository extends SuperRepository {
    List<CategoryEntity> findAll() throws RepositoryException;

    CategoryEntity findByName(Session session, String name);

    CategoryEntity findByCategoryID(Session session, String categoryId);

    CategoryEntity findByCategoryID(String categoryId) throws RepositoryException;

    void update(CategoryEntity categoryEntity) throws RepositoryException;

    void deleteByCategoryId(String categoryId) throws RepositoryException;

    CategoryEntity findByName(String name) throws RepositoryException;

    void add(CategoryEntity categoryEntity) throws RepositoryException;
}
