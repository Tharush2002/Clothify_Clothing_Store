package service.custom.impl;

import entity.SupplierEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Supplier;
import repository.RepositoryFactory;
import repository.custom.SupplierRepository;
import service.custom.SupplierService;
import util.Type;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository = RepositoryFactory.getInstance().getRepositoryType(Type.SUPPLIER);

    @Override
    public ObservableList<Supplier> getAllSuppliers() {
        List<SupplierEntity> supplierEntityList = supplierRepository.findAll();
        ObservableList<Supplier> supplierObservableList= FXCollections.observableArrayList();
        if (supplierEntityList!=null){
            supplierEntityList.forEach(entity->{
                supplierObservableList.add(new Supplier(entity.getSupplierId(),entity.getName(), entity.getCompany(), entity.getEmail()));
            });
        }
        return supplierObservableList;
    }

    @Override
    public Supplier findSupplierByName(String name) {
        SupplierEntity supplierEntity = supplierRepository.findByName(name);
        if(supplierEntity !=null){
            return new Supplier(supplierEntity.getSupplierId(), supplierEntity.getName(), supplierEntity.getCompany(), supplierEntity.getEmail());
        }
        return new Supplier();
    }
}
