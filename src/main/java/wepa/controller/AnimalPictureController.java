package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.AnimalPicture;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;
import wepa.service.CommentService;
import wepa.service.UserService;

@Controller
@RequestMapping("/pictures")
public class AnimalPictureController {

    @Autowired
    private AnimalPictureService animalPictureService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired 
    private AlbumService albumService;
    
    @Autowired
    private UserService userService;
    
    // Index
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("images", animalPictureService.getLatest(5));
        return Routes.ANIMALPICTURES_TEMPLATE;
    }
    
    // Get AnimalPicture by id
    @RequestMapping(value = "/{id}/src", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPicture(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AnimalPicture animalPicture = animalPictureService.getById(id);
        if (animalPicture==null){
            redirectAttributes.addFlashAttribute("error", "Animal picture was not found!");
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(animalPicture.getContentType()));
        headers.setContentLength(animalPicture.getImage().length);
        headers.setCacheControl("public");
        headers.setExpires(Long.MAX_VALUE);

        return new ResponseEntity<>(animalPicture.getImage(), headers, HttpStatus.CREATED);
    }
    
    // Get AnimalPicture page
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getPicturePage(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        AnimalPicture animalPicture = animalPictureService.getById(id);
        
        if(animalPicture == null){
            redirectAttributes.addFlashAttribute("error", "Animal picture was not found!");
            return Routes.INDEX_REDIRECT;
        }
        
        model.addAttribute("picture", animalPicture);
        model.addAttribute("comments", commentService.getLatestComments(animalPicture, 5));
        return Routes.ANIMALPICTURE_TEMPLATE;
    }
    
    // TODO: Comment AnimalPicture
    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
    public String postComment(@PathVariable Long id, @RequestParam String comment, RedirectAttributes redirectAttributes){
        try {
            commentService.addComment(id, comment);
            redirectAttributes.addFlashAttribute("message", "Comment added!");
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/pictures/" + id;
    }
    
    // Like a picture
    @RequestMapping(value = "/{id}/like", method = RequestMethod.GET)
    @ResponseBody
    public int likeImage(@PathVariable Long id) {
        System.out.println("\n\n\nUseo...");
        return animalPictureService.likeAnimalPicture(id);
    }
    // Dislike a picture
    @RequestMapping(value = "/{id}/dislike", method = RequestMethod.GET)
    @ResponseBody
    public int dislikeImage(@PathVariable Long id) {
        return animalPictureService.dislikeAnimalPicture(id);
    }
    
    // Add new AnimalPicture
    @RequestMapping(method = RequestMethod.POST)
    public String addNewAnimalPicture(@RequestParam MultipartFile file, @RequestParam String title, @RequestParam String description,
            RedirectAttributes redirectAttributes) throws Exception {
        
        try {
            AnimalPicture picture = animalPictureService.add(file, title, description, null);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return Routes.ANIMALPICTURES_REDIRECT;
    }
}
