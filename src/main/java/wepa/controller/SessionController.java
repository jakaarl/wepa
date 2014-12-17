package wepa.controller;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wepa.domain.User;
import wepa.helpers.Routes;
import wepa.service.UserService;

@Controller
public class SessionController {
    
    @Autowired
    UserService userService;
    
    @RequestMapping(value = "/login")
    public String getLogin(){
        return Routes.LOGIN_TEMPLATE;
    }
    
    @RequestMapping(value = "/logout")
    public String getLogout(){
        SecurityContextHolder.clearContext();
        return Routes.INDEX_REDIRECT;
    }
    
    @RequestMapping(value = "/register")
    public String getRegister() {
        return Routes.REGISTER_TEMPLATE;
    }
    
    @ModelAttribute("user")
    private Registration getRegistration() {
        return new Registration();
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(@Valid @ModelAttribute("user") Registration registration,  BindingResult bindingResult,
    		RedirectAttributes redirectAttributes, Model model) {
    	if (userService.findUserByEmail(registration.email) != null) {
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email");
        } else if (userService.findUserByUsername(registration.username) != null) {
            bindingResult.rejectValue("username", "error.user", "An account already exists for this username");
        }
        if (bindingResult.hasErrors()) {
    		return Routes.REGISTER_TEMPLATE;
    	}
        try {
            User created = userService.save(registration.toUser());
            redirectAttributes.addFlashAttribute("message", "User created: " + created.getUsername());
            return Routes.LOGIN_REDIRECT;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return Routes.REGISTER_TEMPLATE;      
        }
        
    }
    
    public static class Registration {
        @NotBlank @Length(min = 2, max = 32)
        private String username;
        @NotBlank @Length(min = 5, max = 256)
        private String email;
        @NotBlank @Length(min = 2, max = 64)
        private String firstName;
        @NotBlank @Length(min = 2, max = 64)
        private String lastName;
        @NotBlank @Length(min = 8, max = 64)
        private String password;
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public User toUser() {
            User user = new User();
            user.setSalt(BCrypt.gensalt());
            user.setPassword(BCrypt.hashpw(password, user.getSalt()));
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            return user;
        }
    }
}