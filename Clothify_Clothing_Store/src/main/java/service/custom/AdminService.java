package service.custom;

import exceptions.NoAdminFoundException;
import exceptions.NoEmployeeFoundException;
import model.Admin;
import service.SuperService;

public interface AdminService extends SuperService {
    boolean isValidEmail(String email);

    Admin findByEmail(String email) throws NoAdminFoundException;

    boolean updateAdminPassword(String email, String password);

    Admin findByUserName(String trim) throws NoAdminFoundException;

    boolean saveOrUpdate(Admin admin, int i);
}
