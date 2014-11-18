package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.User;
import wepa.helpers.Routes;
import wepa.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    
    @RequestMapping("/{id}")
    public String getUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        User user = userService.findUser(id);
        
        if(user == null){
            redirectAttributes.addFlashAttribute("error", "User not found, id: " + id);
            return Routes.INDEX_REDIRECT;
        }
        
        model.addAttribute("user", user);
        return Routes.PROFILE_TEMPLATE;
    }
}
