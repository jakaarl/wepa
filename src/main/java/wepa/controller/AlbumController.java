package wepa.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.Album;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;


@Controller
@RequestMapping("/")
public class AlbumController {

    static final String INDEX_TEMPLATE = "index";
    static final String ALBUM_TEMPLATE = "album";
    static final String INDEX_REDIRECT = "redirect:/";

    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AnimalPictureService pictureService;

    @PostConstruct
    public void init(){
        albumService.save(new Album("Default album_007"));
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("albums", albumService.getAll());
        model.addAttribute("images", pictureService.getLatest(5));
        return INDEX_TEMPLATE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addNewALbum(@ModelAttribute Album album, Model model, 
                            RedirectAttributes redirectAttributes) throws Exception {
        try {
            albumService.save(album);
            redirectAttributes.addFlashAttribute("message", "New album has been created!");
            return INDEX_REDIRECT; 
        } catch (IllegalArgumentException e){
            model.addAttribute("albums", albumService.getAll());
            model.addAttribute("images", pictureService.getLatest(5));
            model.addAttribute("errorMessage", e.getMessage());
            return INDEX_TEMPLATE;
        }
    }
    

    @RequestMapping(value = "/albums/{id}", method = RequestMethod.GET)
    public String getAlbum(@PathVariable Long id, Model model) {
        Album album= albumService.find(id);
        model.addAttribute("album", album);
        model.addAttribute("animalPictures", album.getAnimalPictures());
        return ALBUM_TEMPLATE;
    }


  
}
