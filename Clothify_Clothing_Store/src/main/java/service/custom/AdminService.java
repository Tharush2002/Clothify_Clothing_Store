package service.custom;

import exceptions.NoAdminFoundException;
import exceptions.RepositoryException;
import javafx.collections.ObservableList;
import model.Admin;
import service.SuperService;

public interface AdminService extends SuperService {
    boolean isValidEmail(String email);

    boolean isValidContactNumber(String contactNumber);

    Admin findByEmail(String email) throws NoAdminFoundException, RepositoryException;

    void updateAdminPassword(String email, String password) throws RepositoryException;

    Admin findByUserName(String trim) throws NoAdminFoundException, RepositoryException;

    boolean saveOrUpdate(Admin admin, int i);

    ObservableList<Admin> getAll() throws RepositoryException;

    void deleteById(String adminId) throws RepositoryException;

    void save(Admin admin) throws RepositoryException;

    void update(Admin admin) throws RepositoryException;
}
