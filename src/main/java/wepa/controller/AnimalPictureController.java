package wepa.controller;

import javax.validation.Valid;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wepa.controller.parameter.AnimalPictureFile;
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

    @RequestMapping(method = RequestMethod.GET)
    public String getLatestPictures(Model model) {
        model.addAttribute("images", animalPictureService.getLatest(10));
        return Routes.ANIMALPICTURES_TEMPLATE;
    }

    @RequestMapping(value = "/{id}/src", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPicture(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AnimalPicture animalPicture = animalPictureService.getById(id);
        if (animalPicture == null) {
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getPicturePage(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        AnimalPicture animalPicture = animalPictureService.getById(id);

        if (animalPicture == null) {
            redirectAttributes.addFlashAttribute("error", "Animal picture was not found!");
            return Routes.INDEX_REDIRECT;
        }

        model.addAttribute("picture", animalPicture);
        model.addAttribute("comments", commentService.getLatestComments(animalPicture, 5));
        return Routes.ANIMALPICTURE_TEMPLATE;
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
    public String postComment(@PathVariable Long id, @RequestParam String comment, RedirectAttributes redirectAttributes) {
        try {
            commentService.addComment(id, comment);
            redirectAttributes.addFlashAttribute("message", "Comment added!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        redirectAttributes.addAttribute("id", id);
        return Routes.ANIMAL_PICTURE_BY_ID_REDIRECT;
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/{id}/like", method = RequestMethod.GET)
    @ResponseBody
    public int likeImage(@PathVariable Long id) {
        return animalPictureService.likeAnimalPicture(id);
    }

    @PreAuthorize("authenticated")
    @RequestMapping(value = "/{id}/dislike", method = RequestMethod.GET)
    @ResponseBody
    public int dislikeImage(@PathVariable Long id) {
        return animalPictureService.dislikeAnimalPicture(id);
    }

    @PreAuthorize("authenticated")
    @RequestMapping(method = RequestMethod.POST)
    public String addNewAnimalPicture(@Valid @ModelAttribute AnimalPictureFile animalPictureFile,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            redirectAttributes.addFlashAttribute("description", animalPictureFile.getDescription());
            redirectAttributes.addFlashAttribute("title", animalPictureFile.getTitle());
            return "redirect:/pictures/#submitAnimalPicModal";
        }
        User user = currentUserProvider.getUser();
        animalPictureService.add(animalPictureFile.getFile(), animalPictureFile.getTitle(), user,
                animalPictureFile.getDescription(), null);
        return Routes.ANIMALPICTURES_REDIRECT;
    }

    @ModelAttribute("animalPictureFile")
    private AnimalPictureFile getAnimalPictureFile() {
        return new AnimalPictureFile();
    }
}
