package net.app.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.app.shop.dto.ProductDto;
import net.app.shop.model.Order;
import net.app.shop.model.Product;
import net.app.shop.repo.OrderRepository;
import net.app.shop.repo.ProductRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {
	
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    
   public Product addProduct(ProductDto productDto)  {
       String image = null;
       try {
           image = Base64.getEncoder().encodeToString(productDto.getImageFile().getBytes());
       } catch (IOException e) {
           e.printStackTrace();
       }
       Product product = new Product(productDto.getName(),productDto.getDescription(),
                productDto.getPrice(),productDto.getStock(),productDto.getReduction(),
                image);
        return productRepository.save(product);
   }
   
    public Product updateProduct(Long id ,ProductDto productDto)  {
        String image = null;
        Product product = this.getProductById(id);
       if(!productDto.getImageFile().getOriginalFilename().equals("") ){
           try {
               image = Base64.getEncoder().encodeToString(productDto.getImageFile().getBytes());
               product.setImage(image);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setReduction(productDto.getReduction());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }   
    
    public Product getProductByName(String name){
        return productRepository.findByName(name);
    }
    
    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }
    
    public List<Product> getProductsLike(String keyWord){
        return productRepository.findByNameContaining(keyWord);
    }

    public List<Product> getProductsByPrice(Double price){
        return productRepository.findByPrice(price);
    }
    
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
