package repository.custom;

import entity.CategoryEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface CategoryRepository extends SuperRepository {
    List<CategoryEntity> findAll() throws RepositoryException;

    CategoryEntity findByName(String name) throws RepositoryException;

    CategoryEntity findByName(Session session, String name);

    CategoryEntity findByCategoryID(Session session, String categoryId);
}
