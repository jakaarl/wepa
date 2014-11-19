package wepa.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
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

    // Get albums
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("albums", albumService.getAll());
        return Routes.ALBUMS_TEMPLATE;
    }
    
    // Post a new Album
    @RequestMapping(method = RequestMethod.POST)
    public String addNewAlbum(@ModelAttribute Album album, 
                            RedirectAttributes redirectAttributes) throws Exception {
        try {
            albumService.save(album);
            redirectAttributes.addFlashAttribute("message", "New album has been created!");
            return Routes.ALBUMS_REDIRECT; 
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return Routes.ALBUMS_REDIRECT;
        }
    }
    
    // Get Album by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getAlbum(@PathVariable Long id, Model model) {
        Album album= albumService.find(id);
        model.addAttribute("album", album);
       // model.addAttribute("animalPictures", album.getAnimalPictures());
        return Routes.ALBUM_TEMPLATE;
    }
    
    // Add image to album
    @Transactional
    @RequestMapping(value="/{albumId}", method = RequestMethod.POST)
    public String addNewAnimalPicture(@RequestParam MultipartFile file, @PathVariable Long albumId, @RequestParam String title, @RequestParam String description,
            RedirectAttributes redirectAttributes, Model model) throws Exception {      
        try {
            AnimalPicture picture = albumService.addPictureToAlbum(file, title, description, albumId);
            
            redirectAttributes.addFlashAttribute("message", "Your picture has been saved successfuly");
            redirectAttributes.addFlashAttribute("albumId", albumId);
            redirectAttributes.addFlashAttribute("id", picture.getId());
            redirectAttributes.addFlashAttribute("description", picture.getDescription());
            return Routes.ALBUMS_REDIRECT;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("album", albumService.find(albumId));
            return Routes.ALBUM_TEMPLATE;
        }
    }
}
