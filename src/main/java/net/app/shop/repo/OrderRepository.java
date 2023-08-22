package net.app.shop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.app.shop.model.Order;
import net.app.user.model.AppUser;
import net.app.user.model.UserEntity;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserOrderByOrderDateDesc(UserEntity userEntity);

	List<Order> findByUser(UserEntity user);
}
