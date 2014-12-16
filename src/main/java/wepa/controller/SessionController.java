package wepa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("password", "salt", "comments", "albums", "animalPictures", "likedPictures");
    }
    
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
    public String getRegister(@ModelAttribute("user") User user) {
        return Routes.REGISTER_TEMPLATE;
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(@Valid @ModelAttribute User user,  BindingResult bindingResult,
    		RedirectAttributes redirectAttributes, Model model) {
    	if (userService.findUserByEmail(user.getEmail())!=null){
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email");
        } else if (userService.findUserByUsername(user.getUsername())!=null){
            bindingResult.rejectValue("username", "error.user", "An account already exists for this username");
        }
        if (bindingResult.hasErrors()) {
    		return Routes.REGISTER_TEMPLATE;
    	}
        try {
            User created = userService.save(user);
            redirectAttributes.addFlashAttribute("message", "User created: " + created.getUsername());
            return Routes.LOGIN_REDIRECT;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return Routes.REGISTER_TEMPLATE;      
        }
        
    }
}
