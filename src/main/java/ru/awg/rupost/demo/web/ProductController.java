package ru.awg.rupost.demo.web;

import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.model.ProductEntity;
import ru.awg.rupost.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class ProductController
{
    @Autowired
    ProductService service;
 
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllEmployees() {
        List<ProductEntity> list = service.getAllEmployees();
 
        return new ResponseEntity<List<ProductEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getEmployeeById(@PathVariable("id") Long id)
                                                    throws RecordNotFoundException {
        ProductEntity entity = service.getEmployeeById(id);
 
        return new ResponseEntity<ProductEntity>(entity, new HttpHeaders(), HttpStatus.OK);
    }
 
    @PostMapping
    public ResponseEntity<ProductEntity> createOrUpdateEmployee(ProductEntity employee)
                                                    throws RecordNotFoundException {
        ProductEntity updated = service.createOrUpdateEmployee(employee);
        return new ResponseEntity<ProductEntity>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @DeleteMapping("/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable("id") Long id)
                                                    throws RecordNotFoundException {
        service.deleteEmployeeById(id);
        return HttpStatus.FORBIDDEN;
    }
 
}