package net.app.shop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.app.shop.dto.ProductDto;
import net.app.shop.exception.NotFoundException;
import net.app.shop.model.Product;
import net.app.shop.service.ProductService;
import net.app.user.model.UserEntity;
import net.app.user.service.UserService;

@Controller
@RequestMapping("/Admin")
public class AdminProductController {
	
   private final ProductService productService;
   private final UserService userService;
    
   public AdminProductController(ProductService productService, UserService userService) {
       this.productService = productService;
       this.userService = userService;
   }
   
   @GetMapping("/index")
   public String indexView(final Model model, Authentication auth,
                           @RequestParam(value="user",required = false, defaultValue = "") String email){
       UserEntity user;
       if(!email.equals("")){
            user = userService.getUserByEmail(email);
       }else{
           String Email = auth.getName();
           user = userService.getUserByEmail(Email);
       }
       model.addAttribute("products",productService.getAllProducts());
       model.addAttribute("user",user);
       return "Admin/index";
   }
   
   @GetMapping("/addProduct")
   public String addProductView(Model model){
       model.addAttribute("productDto",new ProductDto());
       return "Admin/addproduct";
   }
   
   @PostMapping("/addProduct")
   public String addProduct(@ModelAttribute ProductDto productDto){
       System.out.println(productDto.toString());
       productService.addProduct(productDto);
       return "redirect:/Admin/index";
   }
   
   @GetMapping("/delete-product/{id}")
   public String delete(@PathVariable("id") Long id){
       productService.deleteProduct(id);
       return "redirect:/Admin/index";
   }
   
   @GetMapping("/editer-product/{id}")
   public String editView(@PathVariable("id") Long id,final Model model){
       Product product = productService.getProductById(id);
       ProductDto productDto = new ProductDto();
       productDto.setId(product.getId());
       productDto.setDescription(product.getDescription());
       productDto.setPrice(product.getPrice());
       productDto.setName(product.getName());
       productDto.setReduction(product.getReduction());
       productDto.setStock(product.getStock());
       model.addAttribute("product",productDto);
       return "Admin/editProduct";
   }
   
   @PostMapping("/editProduct")
   public String editProduct(@ModelAttribute ProductDto productDto){
       productService.updateProduct(productDto.getId(),productDto);
       return "redirect:/Admin/index";
   }
   
   @GetMapping("/indexUser")
   public String showUser(Model model){
       model.addAttribute("user",userService.getUsers());
       return "Admin/indexUser";
   }
   
   @GetMapping("/addUser")
   public String addUserView(Model model){
       model.addAttribute("userEntity",new UserEntity());
       return "Admin/addUser";
   }
   
   @PostMapping("/addUser")
   public String addUser(@ModelAttribute UserEntity userEntity){
       System.out.println(userEntity.toString());
       userService.createUser(userEntity);
       return "redirect:/Admin/indexUser";
   }
   
   @GetMapping("/delete-user/{id}")
   public String deleteUser(@PathVariable("id") Long id){
       userService.deleteUser(id);
       return "redirect:/Admin/indexUser";
   }
   
   @GetMapping("/editer-user/{id}")
   public String editUserView(@PathVariable("id") Long id,final Model model) throws NotFoundException{
       UserEntity user = userService.getUser(id);
       user.setId(user.getId());
       user.setUserName(user.getUserName());
       user.setPassword(user.getPassword());
       user.setEmail(user.getEmail());
       model.addAttribute("userEntity", user);
       return "Admin/editUser";
   }
   
   @PostMapping("/editUser")
   public String editUser(@ModelAttribute UserEntity userEntity) throws NotFoundException{
       userService.updateUser(userEntity.getId(), userEntity);
       return "redirect:/Admin/indexUser";
   }
   

}
