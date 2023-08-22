package net.app.shop.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.app.shop.model.Cart;
import net.app.shop.model.CartItem;
import net.app.shop.repo.CartItemRepository;
import net.app.shop.repo.CartRepository;
import net.app.shop.repo.ProductRepository;
import net.app.user.model.AppUser;
import net.app.user.model.UserEntity;
import net.app.user.repo.UserRepository;

@Service
public class ShopService {
	
	private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository CartRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public ShopService(CartItemRepository cartItemRepository, ProductRepository productRepository,
                       CartRepository CartRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.CartRepository = CartRepository;
        this.userRepository = userRepository;
    }
    
    public void addItem(AppUser user, Long product_id, Integer quantity){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity account = userRepository.findByEmail(email);
        Cart shoppingCart = CartRepository.findByUser(account);
        if(shoppingCart != null ){
            if(itemIsnotThere(shoppingCart.getItems(),product_id)){
                CartItem item = new CartItem(productRepository.getById(product_id),quantity);
                shoppingCart.getItems().add(item);
                CartRepository.save(shoppingCart);
            }else{

                for (CartItem it : shoppingCart.getItems()) {
                 if(it.getProduct().getId().equals(product_id)){
                     it.setQuantity(it.getQuantity()+quantity);
                     cartItemRepository.save(it);
                 }
                }
            }
        }else{
            Cart shopping = new Cart();
            shopping.setUser(account);
            CartItem item = new CartItem(productRepository.getById(product_id),quantity);
            cartItemRepository.save(item);
            shopping.getItems().add(item);
            CartRepository.save(shopping);

        }


    }

    private boolean itemIsnotThere(Set<CartItem> items,Long product_id) {
        for (CartItem item : items){
            if(item.getProduct().getId().equals(product_id)){
                return false;
            }
        }
        return true;
    }

    public Cart removeItem(AppUser user, Long item_id){
        Cart shoppingCart = getShoppingCartByUser(user);
        Set<CartItem> items = shoppingCart.getItems().stream().filter(item -> !item.getId().equals(item_id) ).collect(Collectors.toSet());
        shoppingCart.setItems(items);
        cartItemRepository.deleteById(item_id);
        return CartRepository.save(shoppingCart);
    }
    public Cart updateItemQuantity(AppUser user, Long item_id,Integer newQuantity){
        Cart shoppingCart = getShoppingCartByUser(user);
        for (CartItem item : shoppingCart.getItems()) {
            if(item.getId().equals(item_id)){
                item.setQuantity(newQuantity);
                cartItemRepository.save(item);
                break;
            }
        }
        return getShoppingCartByUser(user);
    }
    public void deleteShoppingCart(AppUser user){
        Cart shoppingCart = getShoppingCartByUser(user);
        CartRepository.delete(shoppingCart);
    }
    public Cart getShoppingCartByUser(AppUser user){
        UserEntity acount = userRepository.findByEmail(user.getUsername());
        return  CartRepository.findByUser(acount);
    }

    public Cart getShoppingCartById(Long id) {
        return  CartRepository.findById(id).get();
    }

    public Cart creatEmptyCart(AppUser principal) {
        UserEntity acount = userRepository.findByEmail(principal.getUsername());
        if(CartRepository.findByUser(acount) == null){
            Cart shope = new Cart();
            shope.setUser(acount);
            return CartRepository.save(shope);
        }
        else{
            return CartRepository.findByUser(acount);
        }

    }
    
    public void clearCart(AppUser user) {
        Cart shoppingCart = getShoppingCartByUser(user);
        
        // Remove all items from the shopping cart
        shoppingCart.getItems().clear();
        
        // Save the updated shopping cart
        CartRepository.save(shoppingCart);
    }
    

}
