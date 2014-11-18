/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        return "login";
    }
    
    @RequestMapping(value = "/logout")
    public String getLogout(){
        return Routes.INDEX_REDIRECT;
    }
    
    @RequestMapping(value = "/register")
    public String getRegister(){
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(@Valid @ModelAttribute User user, 
                            RedirectAttributes redirectAttributes){
        User created = userService.save(user);
        
        if(created == null){
            redirectAttributes.addFlashAttribute("errorMessage", "User was not created");
            return "redirect:/register";
        }
        
        redirectAttributes.addFlashAttribute("message", "User created");
        return "redirect:/login";
    }
}
