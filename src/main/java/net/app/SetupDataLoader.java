package net.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.app.user.model.Role;
import net.app.user.model.RoleEnum;
import net.app.user.model.UserEntity;
import net.app.user.repo.RoleRepository;
import net.app.user.repo.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>{
	
	boolean alreadySetup = false;

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		SecurityContextHolder.clearContext();
		if (alreadySetup)
			return;
		
		createRoleIfNotFound(RoleEnum.ROLE_ADMIN.name());
		createRoleIfNotFound(RoleEnum.ROLE_CUSTOMER.name());


		Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN.name());
		Role customerRole = roleRepository.findByName(RoleEnum.ROLE_CUSTOMER.name());

		
		creatUserIfNotFound("mara@gmail.com", "123", new Role[]{adminRole,customerRole});
		creatUserIfNotFound("indar@gmail.com", "12", new Role[]{customerRole});


		alreadySetup = true;
	}
	
	@Transactional
	public void creatUserIfNotFound(String userName, String password,Role[] role) {
		if(userRepository.findByEmail(userName) == null) {
			UserEntity user = new UserEntity();
			user.setUserName("Mara Indar Pramesti");
			user.setPassword(passwordEncoder.encode(password));
			user.setEmail(userName);

			Set<Role> roles = new HashSet<Role>(Arrays.asList(role));
			user.setRoles(roles);
			userRepository.save(user);
		}
		
	}
	
	@Transactional
	public void createRoleIfNotFound(String name) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role();
			role.setName(name);
			roleRepository.save(role);
		}
	}

}
