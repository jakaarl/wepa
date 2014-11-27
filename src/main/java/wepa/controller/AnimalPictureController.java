package wepa.controller;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.AnimalPicture;
import wepa.domain.User;
import wepa.helpers.CurrentUserProvider;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;
import wepa.service.CommentService;

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
    private CurrentUserProvider currentUserProvider;
    
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
    
    @PreAuthorize("authenticated")
    @RequestMapping(method = RequestMethod.POST)
    public String addNewAnimalPicture(@Valid @ModelAttribute AnimalPictureFile file,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {
        if (bindingResult.hasErrors()) {
            return Routes.ANIMALPICTURES_TEMPLATE;
        }
        User user = currentUserProvider.getUser();
        animalPictureService.add(file.getFile(), file.getTitle(), user, file.getDescription(), null);
        return Routes.ANIMALPICTURES_REDIRECT;
    }
    
    protected static class AnimalPictureFile {
        
        @NotBlank
        private String title;
        @NotBlank
        private String description;
        private MultipartFile file;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        
        public MultipartFile getFile() {
            return file;
        }

        public void setFile(MultipartFile file) {
            this.file = file;
        }
        
        @AssertFalse(message = "File cannot be empty")
        public boolean isEmpty() {
            return file.isEmpty();
        }
        
        @AssertFalse(message = "File size must be less than 5MB")
        public boolean isOversized() {
            return file.getSize() > (5*1024*1024);
        }
        
        @AssertTrue(message = "Only image files allowed")
        public boolean isImageFile() {
            return file.getContentType().startsWith("image/");
        }

        
    }
}
