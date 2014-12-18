package wepa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;

import wepa.domain.Comment;
import wepa.domain.User;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;
import wepa.service.CommentService;
import wepa.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    UserService userService;
    
    @Autowired
    CommentService commentService;
    
    @Autowired
    AlbumService albumService;
    
    @Autowired
    AnimalPictureService animalPictureService;
    
    @RequestMapping("/{id}")
    public String getUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        User user = userService.findUser(id);
        if(user == null){
            throw new IllegalArgumentException("No such user, id: " + id);
        }
        model.addAttribute("user", user);
        
        List<Comment> latestComments = commentService.getLatestComments(user, 5);
        model.addAttribute("latestComments", latestComments);
        
        List<Album> latestAlbums = albumService.getLatestAlbums(user, 5);
        model.addAttribute("latestAlbums", latestAlbums);
        
        List<AnimalPicture> latestAnimalPictures = animalPictureService.getLatestAnimalPictures(user, 5);
        model.addAttribute("latestAnimalPictures", latestAnimalPictures);
        
        return Routes.PROFILE_TEMPLATE;
    }
    
    // Get all Albums
    @RequestMapping("/{id}/albums")
    public String getUserAlbums(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        User user = userService.findUser(id);
        if(user == null){
            throw new IllegalArgumentException("No such user, id: " + id);
        }
        model.addAttribute("user", user);
        model.addAttribute("albums", user.getAlbums());
        
        return Routes.PROFILE_ALBUMS_TEMPLATE;
    }
    
    // Get all AnimalPictures
    @RequestMapping("/{id}/pictures")
    public String getUserAnimalPictures(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        User user = userService.findUser(id);
        if(user == null){
            throw new IllegalArgumentException("No such user, id: " + id);
        }
        model.addAttribute("user", user);
        model.addAttribute("images", animalPictureService.getAllAnimalPicturesByUser(user));
        
        return Routes.PROFILE_ANIMALPICTURES_TEMPLATE;
    }
    
    // Get all Comments
    @RequestMapping("/{id}/comments")
    public String getUserComments(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        User user = userService.findUser(id);
        if(user == null){
            throw new IllegalArgumentException("No such user, id: " + id);
        }
        model.addAttribute("user", user);
        model.addAttribute("comments", user.getComments());
        
        return Routes.PROFILE_COMMENTS_TEMPLATE;
    }
}
