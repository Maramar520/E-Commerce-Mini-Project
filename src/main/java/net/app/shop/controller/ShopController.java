package net.app.shop.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.app.shop.model.Cart;
import net.app.shop.service.ProductService;
import net.app.shop.service.ShopService;
import net.app.user.model.AppUser;
import net.app.user.model.UserEntity;
import net.app.user.service.UserService;

@Controller
@RequestMapping("/Customer")
public class ShopController {
	
	private final ShopService shopService;
    private final UserService userService;
    private final ProductService productService;
    
    @Autowired
    public ShopController(ShopService shopService, UserService userService, ProductService productService) {
        this.shopService = shopService;
        this.userService = userService;
        this.productService = productService;
    }
    
    @GetMapping("/index")
    public String userIndexView(Model model,
                                @RequestParam(value = "user", required = false,defaultValue = "") String email) {


        String Email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(Email);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("user", user);
        model.addAttribute("shop", shopService.getShoppingCartByUser((AppUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "index";
    }
    
    @GetMapping("/cart")
    public String cartView(Model model, @RequestParam("id") Optional<Long> id, Authentication auth){
        if(id.isPresent()){
            model.addAttribute("shop", shopService.getShoppingCartById(id.get()));
        }else{
            model.addAttribute("shop", shopService.creatEmptyCart((AppUser)auth.getPrincipal()));
        }

        model.addAttribute("user", userService.getUserByEmail(auth.getName()));
        return "cart";
    }
    
    @PostMapping("/add-cart")
    public String addToCart(Model model, Authentication auth, @RequestParam("id") Long id,
                            @RequestParam("quantity") Integer quantity ) {
        shopService.addItem((AppUser) auth.getPrincipal(),id,quantity);
        return "redirect:/Customer/index";
    }
    
    @GetMapping("/remove-cartItem/{id}")
    public String addToCart(Model model, Authentication auth, @PathVariable("id") Long id) {
        Cart shop = shopService.removeItem((AppUser) auth.getPrincipal(),id);
        model.addAttribute("shop", shop);
        model.addAttribute("user", userService.getUserByEmail(((AppUser) ((AppUser) auth.getPrincipal())).getUsername()));
        return "cart";
    }

    @GetMapping("/update-cartItem")
    public String updateItemQuantityCart(Model model, Authentication auth, @RequestParam("id") Long id,
                            @RequestParam("quantity") Integer quantity ) {
        Cart shop = shopService.updateItemQuantity((AppUser) auth.getPrincipal(),id,quantity);
        model.addAttribute("shop", shop);
        System.out.println( ((AppUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
        model.addAttribute("user", userService.getUserByEmail(((AppUser) ((AppUser) auth.getPrincipal())).getUsername()));
        return "cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model, Authentication auth) {
        AppUser appUser = (AppUser) auth.getPrincipal();

        shopService.clearCart(appUser);

        // Display a success message to the user
        model.addAttribute("user", userService.getUserByEmail(auth.getName()));
        model.addAttribute("message", "Thank you for your purchase!");

        return "checkout-success";
    }
}
