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
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;


@Controller
@RequestMapping("/albums/")
public class AlbumController {

    static final String INDEX_TEMPLATE = "index";
    static final String ALBUM_TEMPLATE = "album";
    static final String INDEX_REDIRECT = "redirect:/";

    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AnimalPictureService animalPictureService;

    @PostConstruct
    public void init(){
        albumService.save(new Album("Default album_007"));
    }
    
    // Post a new Album
    @RequestMapping(method = RequestMethod.POST)
    public String addNewALbum(@ModelAttribute Album album, Model model, 
                            RedirectAttributes redirectAttributes) throws Exception {
        try {
            System.out.println(album.getAlbumName() + "\n\n\n\n\n");
            albumService.save(album);
            redirectAttributes.addFlashAttribute("message", "New album has been created!");
            return INDEX_REDIRECT; 
        } catch (IllegalArgumentException e){
            model.addAttribute("albums", albumService.getAll());
            model.addAttribute("images", animalPictureService.getLatest(5));
            model.addAttribute("errorMessage", e.getMessage());
            return INDEX_TEMPLATE;
        }
    }
    
    // Get Album by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getAlbum(@PathVariable Long id, Model model) {
        Album album= albumService.find(id);
        model.addAttribute("album", album);
        model.addAttribute("animalPictures", album.getAnimalPictures());
        return ALBUM_TEMPLATE;
    }
    
    // Add image to album
    @Transactional
    @RequestMapping(value="/{albumId}", method = RequestMethod.POST)
    public String addNewAnimalPicture(@RequestParam MultipartFile file, @PathVariable Long albumId, @RequestParam String title, @RequestParam String description,
            RedirectAttributes redirectAttributes, Model model) throws Exception {
        Album album = albumService.find(albumId);
        
        try {
            AnimalPicture picture = animalPictureService.add(file, title, description, album);
            picture.setAlbum(album);
            album.getAnimalPictures().add(picture);
            albumService.save(album);
            redirectAttributes.addFlashAttribute("message", "Your picture has been saved successfuly");
            redirectAttributes.addFlashAttribute("albumId", album.getId());
            redirectAttributes.addFlashAttribute("id", picture.getId());
            redirectAttributes.addFlashAttribute("description", picture.getDescription());
            return INDEX_REDIRECT;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("album", album);
            return ALBUM_TEMPLATE;
        }
    }
}
