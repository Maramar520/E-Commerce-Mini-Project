package net.app.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.app.shop.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}