package net.app.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.app.shop.exception.NotFoundException;
import net.app.shop.model.Product;
import net.app.user.model.Role;
import net.app.user.model.UserEntity;
import net.app.user.repo.RoleRepository;
import net.app.user.repo.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
	
	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    public UserEntity createUser(UserEntity userEntity) {
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        UserEntity account = new UserEntity(userEntity.getUserName(), passwordEncoder.encode(userEntity.getPassword())
                , userEntity.getEmail(),roles);
        return userRepository.save(account);
    }
    
    public UserEntity getUser(long id) throws NotFoundException {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }
    
    public UserEntity updateUser(long id, UserEntity newUser) throws NotFoundException {
        UserEntity user = userRepository.findById(id).get();
        user.setUserName(newUser.getUserName());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(user);
    }
    
    public void deleteUser(long id) {

        userRepository.deleteById(id);
    }

   public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email);
   }


}
