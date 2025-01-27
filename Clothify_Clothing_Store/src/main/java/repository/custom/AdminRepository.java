package repository.custom;

import entity.AdminEntity;
import exceptions.RepositoryException;
import org.hibernate.Session;
import repository.SuperRepository;

import java.util.List;

public interface AdminRepository extends SuperRepository {
    AdminEntity findByEmail(String email) throws RepositoryException;

    AdminEntity findByUserName(String userName) throws RepositoryException;

    List<AdminEntity> findAll() throws RepositoryException;

    void deleteById(String adminId) throws RepositoryException;

    void update(AdminEntity adminEntity) throws RepositoryException;

    void save(AdminEntity adminEntity) throws RepositoryException;

    AdminEntity findByEmail(Session session, String email);

    void update(Session session, AdminEntity adminEntity);
}
