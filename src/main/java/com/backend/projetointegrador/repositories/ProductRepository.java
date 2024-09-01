package com.backend.projetointegrador.repositories;

import com.backend.projetointegrador.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
