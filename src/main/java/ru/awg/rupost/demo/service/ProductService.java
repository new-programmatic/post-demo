package ru.awg.rupost.demo.service;

import ru.awg.rupost.demo.exception.RecordNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.awg.rupost.demo.model.ProductEntity;
import ru.awg.rupost.demo.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
     
    @Autowired
    ProductRepository repository;
     
    public List<ProductEntity> getAllEmployees()
    {
        List<ProductEntity> employeeList = repository.findAll();
         
        if(employeeList.size() > 0) {
            return employeeList;
        } else {
            return new ArrayList<ProductEntity>();
        }
    }
     
    public ProductEntity getEmployeeById(Long id) throws RecordNotFoundException
    {
        Optional<ProductEntity> employee = repository.findById(id);
         
        if(employee.isPresent()) {
            return employee.get();
        } else {
            throw new RecordNotFoundException("No employee record exist for given id");
        }
    }
     
    public ProductEntity createOrUpdateEmployee(ProductEntity entity) throws RecordNotFoundException
    {
        Optional<ProductEntity> employee = repository.findById(entity.getId());
         
        if(employee.isPresent())
        {
            ProductEntity newEntity = employee.get();
            newEntity.setName(entity.getName());
            newEntity.setDescription(entity.getDescription());

 
            newEntity = repository.save(newEntity);
             
            return newEntity;
        } else {
            entity = repository.save(entity);
             
            return entity;
        }
    }
     
    public void deleteEmployeeById(Long id) throws RecordNotFoundException
    {
        Optional<ProductEntity> employee = repository.findById(id);
         
        if(employee.isPresent())
        {
            repository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No employee record exist for given id");
        }
    }
}