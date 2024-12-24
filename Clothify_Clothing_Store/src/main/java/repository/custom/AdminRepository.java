package repository.custom;

import entity.AdminEntity;
import repository.SuperRepository;

public interface AdminRepository extends SuperRepository {
    AdminEntity findByEmail(String email);

    AdminEntity findByUserName(String userName);

    boolean update(AdminEntity adminEntity);

    boolean saveOrUpdate(AdminEntity adminEntity);
}
