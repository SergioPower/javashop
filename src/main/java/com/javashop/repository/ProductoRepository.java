package com.javashop.repository;

import org.springframework.data.repository.CrudRepository;

import com.javashop.entity.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Long> {
    

}
