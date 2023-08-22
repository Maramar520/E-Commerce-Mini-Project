package net.app.shop.repo;

import net.app.shop.model.Cart;
import net.app.user.model.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findBySessionToken(String  sessionToken);
    Cart findByUser(UserEntity user);
}
