package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String addNewAlbum(@ModelAttribute Album album,
                            RedirectAttributes redirectAttributes) throws Exception {
        try {
            albumService.save(album);
            redirectAttributes.addFlashAttribute("message", "New album has been created!");
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
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
    @Transactional
    @RequestMapping(value="/{albumId}", method = RequestMethod.POST)
    public String addNewAnimalPictureToAlbum(@RequestParam MultipartFile file, @PathVariable Long albumId, @RequestParam String title, @RequestParam String description,
            RedirectAttributes redirectAttributes) throws Exception {
        User user = currentUserProvider.getUser();
        try {
            albumService.addPictureToAlbum(file, title, user, description, albumId);
            redirectAttributes.addFlashAttribute("message", "Your picture has been saved successfuly");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/albums/" + albumId;
        
    }
}
