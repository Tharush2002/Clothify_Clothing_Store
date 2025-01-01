package service.custom.impl;

import entity.AdminEntity;
import exceptions.NoAdminFoundException;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Admin;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.RepositoryFactory;
import repository.custom.AdminRepository;
import service.custom.AdminService;
import util.Type;

import java.util.List;

import static repository.SuperRepository.sessionFactory;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository = RepositoryFactory.getInstance().getRepositoryType(Type.ADMIN);

    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    @Override
    public boolean isValidContactNumber(String contactNumber){
        String contactNumberPattern = "^(\\+\\d{1,3})?\\d{10}$";
        return contactNumber.matches(contactNumberPattern);
    }

    @Override
    public Admin findByEmail(String email) throws NoAdminFoundException, RepositoryException {
        AdminEntity adminEntity = adminRepository.findByEmail(email);
        if (adminEntity != null) {
            return new Admin(adminEntity.getAdminId(),
                    adminEntity.getFirstName(),
                    adminEntity.getLastName(),
                    adminEntity.getEmail(),
                    adminEntity.getUserName(),
                    adminEntity.getPhoneNumber(),
                    adminEntity.getPassword());
        } else {
            throw new NoAdminFoundException("No Admin found");
        }
    }

    @Override
    public void updateAdminPassword(String email, String password) throws RepositoryException {
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();

            AdminEntity adminEntity = adminRepository.findByEmail(session,email);
            if (adminEntity == null) {
                throw new NoAdminFoundException("Admin with email " + email + " not found.");
            }

            adminEntity.setPassword(password);

            adminRepository.update(session, adminEntity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on failure
            throw new RepositoryException("Failed to update AdminEntity password.");
        } finally {
            session.close();
        }
    }

    @Override
    public Admin findByUserName(String userName) throws NoAdminFoundException, RepositoryException {
        AdminEntity adminEntity = adminRepository.findByUserName(userName);
        if (adminEntity != null) {
            return new Admin(adminEntity.getAdminId(),
                    adminEntity.getFirstName(),
                    adminEntity.getLastName(),
                    adminEntity.getEmail(),
                    adminEntity.getUserName(),
                    adminEntity.getPhoneNumber(),
                    adminEntity.getPassword());
        } else {
            throw new NoAdminFoundException("No Admin found");
        }
    }

    @Override
    public boolean saveOrUpdate(Admin admin, int id) {
        adminRepository.saveOrUpdate(
                new AdminEntity(
                        id,
                        admin.getAdminId(),
                        admin.getFirstName(),
                        admin.getLastName(),
                        admin.getEmail(),
                        admin.getUserName(),
                        admin.getPhoneNumber(),
                        admin.getPassword()
                )
        );
        return true;
    }

    @Override
    public ObservableList<Admin> getAll() throws RepositoryException {
        List<AdminEntity> adminEntityList = adminRepository.findAll();
        ObservableList<Admin> adminObservableList= FXCollections.observableArrayList();
        if (adminEntityList!=null){
            adminEntityList.forEach(entity->adminObservableList.add(new Admin(
                    entity.getAdminId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    entity.getUserName(),
                    entity.getPhoneNumber(),
                    null
            )));
        }
        return adminObservableList;
    }

    @Override
    public void deleteById(String adminId) throws RepositoryException {
        adminRepository.deleteById(adminId);
    }

    @Override
    public void save(Admin admin) throws RepositoryException {
        adminRepository.save(new AdminEntity(
                null,
                null,
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                admin.getUserName(),
                admin.getPhoneNumber(),
                admin.getPassword()
        ));
    }

    @Override
    public void update(Admin admin) throws RepositoryException {
        adminRepository.update(new AdminEntity(
                null,
                admin.getAdminId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                admin.getUserName(),
                admin.getPhoneNumber(),
                null
        ));
    }
}
