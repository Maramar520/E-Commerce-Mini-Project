package net.app.shop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.app.shop.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
   Product findByName(String name);
   List<Product> findByPrice(Double price);
   List<Product> findByNameContaining(String name);
}
