package wepa.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wepa.controller.parameter.AnimalPictureFile;
import wepa.domain.Album;
import wepa.domain.User;
import wepa.helpers.CurrentUserProvider;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;


@Controller
@RequestMapping("/albums/")
public class AlbumController {
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AnimalPictureService animalPictureService;
    
    @Autowired
    private CurrentUserProvider currentUserProvider;

    // Get albums
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("albums", albumService.getAll());
        return Routes.ALBUMS_TEMPLATE;
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    public String addNewAlbum(@ModelAttribute Album album, BindingResult result,
                            RedirectAttributes redirectAttributes) throws Exception {
        try {
            albumService.save(album);
            redirectAttributes.addFlashAttribute("message", "New album has been created!");
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("description", album.getDescription());
            return "redirect:/albums/#submitAlbumModal";
        }
        
        return "redirect:/albums/" + album.getId();
    }
    
    @ModelAttribute("album")
    private Album getAlbum() {
        return new Album();
    }
    // Get Album by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getAlbum(@PathVariable Long id, Model model) {
        Album album= albumService.find(id);
        model.addAttribute("album", album);
        return Routes.ALBUM_TEMPLATE;
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{albumId}", method = RequestMethod.POST)
    public String addNewAnimalPictureToAlbum(@PathVariable Long albumId,
            @Valid @ModelAttribute AnimalPictureFile animalPictureFile, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws IOException {
        User user = currentUserProvider.getUser();
        Album album = albumService.find(albumId);
        if (album == null || bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            if (album == null) {
                errors.add("No such album: " + albumId);
            }
            addBindingErrors(errors, bindingResult);
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("description", animalPictureFile.getDescription());
            redirectAttributes.addFlashAttribute("title", animalPictureFile.getTitle());
            redirectAttributes.addFlashAttribute("file", animalPictureFile.getFile());
            redirectAttributes.addAttribute("id", albumId);
            return Routes.ALBUM_BY_ID_REDIRECT + "/#submitAnimalPicModal";
        } else {
            albumService.addPictureToAlbum(
                    animalPictureFile.getFile(), animalPictureFile.getTitle(), user,
                    animalPictureFile.getDescription(), album);
            redirectAttributes.addFlashAttribute("message", "Your picture has been saved successfully");
            redirectAttributes.addAttribute("id", albumId);
            return Routes.ALBUM_BY_ID_REDIRECT;
        }
    }
    
    @ModelAttribute("animalPictureFile")
    private AnimalPictureFile getAnimalPictureFile() {
        return new AnimalPictureFile();
    }
    
    private void addBindingErrors(List<String> errors, BindingResult bindingResult) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            errors.add(error.getDefaultMessage()); // TODO: i18n-resolved messages?
        }
    }
}
