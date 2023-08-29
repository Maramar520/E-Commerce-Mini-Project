package net.app.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.app.user.model.UserEntity;
import net.app.user.service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping(value={"/login","/"})
    public String loginView(){
        return "login";
    }
    
    @GetMapping("/register")
    public String registerView(Model model){
        model.addAttribute("user",new UserEntity());
        return "createUser";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String createUser(@ModelAttribute UserEntity userEntity, Model model){
       System.out.println(userEntity.toString());
        userService.createUser(userEntity);
        model.addAttribute("regSucc","you have been registred successfully");
        return "login";
    }

}
