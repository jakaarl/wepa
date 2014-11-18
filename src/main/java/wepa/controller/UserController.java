package wepa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wepa.domain.Comment;
import wepa.domain.User;
import wepa.helpers.Routes;
import wepa.service.CommentService;
import wepa.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    UserService userService;
    
    @Autowired
    CommentService commentService;
    
    @RequestMapping("/{id}")
    public String getUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        User user = userService.findUser(id);
        if(user == null){
            throw new IllegalArgumentException("No such user, id: " + id);
        }
        model.addAttribute("user", user);
        
        List<Comment> latestComments = commentService.getLatestComments(user, 5);
        model.addAttribute("latestComments", latestComments);
        
        return Routes.PROFILE_TEMPLATE;
    }
}
