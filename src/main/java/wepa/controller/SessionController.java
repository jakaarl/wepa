/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        return Routes.INDEX_REDIRECT;
    }
    
    @RequestMapping(value = "/register")
    public String getRegister(@ModelAttribute("user") User user) {
        return Routes.REGISTER_TEMPLATE;
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(@Valid @ModelAttribute User user,  BindingResult bindingResult,
    		RedirectAttributes redirectAttributes) {
    	if (bindingResult.hasErrors()) {
    		return Routes.REGISTER_TEMPLATE;
    	}
        User created = userService.save(user);
        
        if(created == null){
            redirectAttributes.addFlashAttribute("errorMessage", "User was not created");
            return Routes.REGISTER_REDIRECT; // Hmm? Should we return to the template instead?
        }
        
        redirectAttributes.addFlashAttribute("message", "User created: " + user.getUsername());
        return Routes.LOGIN_REDIRECT;
    }
}
