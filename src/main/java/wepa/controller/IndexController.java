package wepa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;

@Controller
@RequestMapping("/")
public class IndexController {
    // How many items per load
    private static final int ALBUMS_PER_PAGE = 2;
    private static final int ANIMAL_PICTURES_PER_PAGE = 6;
    
    @Autowired
    AlbumService albumService;
    
    @Autowired
    AnimalPictureService animalPictureService;
    
    @RequestMapping
    public String index(Model model){
        List<AnimalPicture> animalPictures = animalPictureService.getLatest(ANIMAL_PICTURES_PER_PAGE);
        if(animalPictures.size() > ANIMAL_PICTURES_PER_PAGE / 2){
            List<AnimalPicture> firstPictures = animalPictures.subList(0, ANIMAL_PICTURES_PER_PAGE/2);
            List<AnimalPicture> secondPictures = animalPictures.subList(ANIMAL_PICTURES_PER_PAGE/2, animalPictures.size());
            model.addAttribute("firstPictures",firstPictures);
            model.addAttribute("secondPictures",secondPictures);
        } else {
            model.addAttribute("firstPictures",animalPictures);
        }
        
        List<Album> albums = albumService.getLatest(ALBUMS_PER_PAGE);
        if(albums.size() > ALBUMS_PER_PAGE / 2){
            List<Album> firstAlbums = albums.subList(0, ALBUMS_PER_PAGE/2);
            List<Album> secondAlbums = albums.subList(ALBUMS_PER_PAGE/2, albums.size());
            model.addAttribute("firstAlbums", firstAlbums);
            model.addAttribute("secondAlbums", secondAlbums);
        } else {
            model.addAttribute("firstAlbums", albums);
        }
        
        if(albumService.countAlbums() > ALBUMS_PER_PAGE || animalPictureService.countAnimalPictures() > ANIMAL_PICTURES_PER_PAGE){
            model.addAttribute("nextPage", 2);
        }
        return Routes.INDEX_TEMPLATE;
    }
    
    @RequestMapping("{page}")
    public String loadMore(@PathVariable int page, Model model){
        List<AnimalPicture> animalPictures = animalPictureService.getLatest(page*ANIMAL_PICTURES_PER_PAGE, page*(ANIMAL_PICTURES_PER_PAGE*2));
        if(animalPictures.size() > ANIMAL_PICTURES_PER_PAGE / 2){
            List<AnimalPicture> firstPictures = animalPictures.subList(0, ANIMAL_PICTURES_PER_PAGE/2);
            List<AnimalPicture> secondPictures = animalPictures.subList(ANIMAL_PICTURES_PER_PAGE/2, animalPictures.size());
            model.addAttribute("firstPictures",firstPictures);
            model.addAttribute("secondPictures",secondPictures);
        } else {
            model.addAttribute("firstPictures",animalPictures);
        }
        
        List<Album> albums = albumService.getLatest(page*ALBUMS_PER_PAGE, page*(ALBUMS_PER_PAGE*2));
        if(albums.size() > ALBUMS_PER_PAGE / 2){
            List<Album> firstAlbums = albums.subList(0, ALBUMS_PER_PAGE/2);
            List<Album> secondAlbums = albums.subList(ALBUMS_PER_PAGE/2, albums.size());
            model.addAttribute("firstAlbums", firstAlbums);
            model.addAttribute("secondAlbums", secondAlbums);
        } else {
            model.addAttribute("firstAlbums", albums);
        }
        
        if(albumService.countAlbums() > page*(ALBUMS_PER_PAGE*2) || animalPictureService.countAnimalPictures() > page*(ANIMAL_PICTURES_PER_PAGE*2)){
            model.addAttribute("nextPage", page+1);
        }
        return Routes.INDEX_TEMPLATE;
    }
}
