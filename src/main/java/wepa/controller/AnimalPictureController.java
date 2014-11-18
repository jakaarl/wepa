package wepa.controller;

import javax.transaction.Transactional;
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
import static wepa.controller.AlbumController.INDEX_TEMPLATE;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.domain.User;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;
import wepa.service.UserService;

@Controller
@RequestMapping("/")
public class AnimalPictureController {
    
   // static final String ALBUM_TEMPLATE = "album";
    static final String INDEX_REDIRECT = "redirect:/";

    @Autowired
    private AnimalPictureService animalPictureService;
    
    @Autowired 
    private AlbumService albumService;
    
    @Autowired
    private UserService userService;
    
    // Index
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("images", animalPictureService.getLatest(5));
        return INDEX_TEMPLATE;
    }
    
    // Get AnimalPicture by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        AnimalPicture pic = animalPictureService.getById(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pic.getContentType()));
        headers.setContentLength(pic.getImage().length);
        headers.setCacheControl("public");
        headers.setExpires(Long.MAX_VALUE);

        return new ResponseEntity<>(pic.getImage(), headers, HttpStatus.CREATED);
    }
    
    // Like a picture
    @RequestMapping(value = "/{id}/like", method = RequestMethod.GET)
    @ResponseBody
    public int likeImage(@PathVariable Long id) {
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
            RedirectAttributes redirectAttributes, Model model) throws Exception {
        
        try {
            AnimalPicture picture = animalPictureService.add(file, title, description, null);
            redirectAttributes.addFlashAttribute("message", "Your picture has been saved successfuly");
            return INDEX_REDIRECT;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("albums", albumService.getAll());
            model.addAttribute("images", animalPictureService.getLatest(5));
            return INDEX_TEMPLATE;
        }
    }
}
