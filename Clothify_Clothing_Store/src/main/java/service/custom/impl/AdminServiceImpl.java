package service.custom.impl;

import com.sun.javafx.binding.StringFormatter;
import entity.AdminEntity;
import exceptions.NoAdminFoundException;
import model.Admin;
import repository.RepositoryFactory;
import repository.custom.AdminRepository;
import service.custom.AdminService;
import util.Type;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository = RepositoryFactory.getInstance().getRepositoryType(Type.ADMIN);

    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    @Override
    public Admin findByEmail(String email) throws NoAdminFoundException {
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
    public boolean updateAdminPassword(String email, String password){
        AdminEntity adminEntity = adminRepository.findByEmail(email);
        adminEntity.setPassword(password);
        return adminRepository.update(adminEntity);
    }

    @Override
    public Admin findByUserName(String userName) throws NoAdminFoundException {
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
}
