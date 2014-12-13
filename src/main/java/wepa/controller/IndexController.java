/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;

@Controller
@RequestMapping("/")
public class IndexController {
    // How many items per load
    private final int albumsPerPage = 2;
    private final int animalPicturesPerPage = 6;
    
    @Autowired
    AlbumService albumService;
    
    @Autowired
    AnimalPictureService animalPictureService;
    
    @RequestMapping
    public String index(Model model){
        List<AnimalPicture> animalPictures = animalPictureService.getLatest(animalPicturesPerPage);
        if(animalPictures.size() > animalPicturesPerPage / 2){
            List<AnimalPicture> firstPictures = animalPictures.subList(0, animalPicturesPerPage/2);
            List<AnimalPicture> secondPictures = animalPictures.subList(animalPicturesPerPage/2, animalPictures.size());
            model.addAttribute("firstPictures",firstPictures);
            model.addAttribute("secondPictures",secondPictures);
        } else {
            model.addAttribute("firstPictures",animalPictures);
        }
        
        List<Album> albums = albumService.getLatest(albumsPerPage);
        if(albums.size() > albumsPerPage / 2){
            List<Album> firstAlbums = albums.subList(0, albumsPerPage/2);
            List<Album> secondAlbums = albums.subList(albumsPerPage/2, albums.size());
            model.addAttribute("firstAlbums", firstAlbums);
            model.addAttribute("secondAlbums", secondAlbums);
        } else {
            model.addAttribute("firstAlbums", albums);
        }
        
        if(albumService.countAlbums() > albumsPerPage || animalPictureService.countAnimalPictures() > animalPicturesPerPage){
            model.addAttribute("nextPage", 2);
        }
        return Routes.INDEX_TEMPLATE;
    }
    
    @RequestMapping("{page}")
    public String loadMore(@PathVariable int page, Model model){
        List<AnimalPicture> animalPictures = animalPictureService.getLatest(page*animalPicturesPerPage, page*(animalPicturesPerPage*2));
        if(animalPictures.size() > animalPicturesPerPage / 2){
            List<AnimalPicture> firstPictures = animalPictures.subList(0, animalPicturesPerPage/2);
            List<AnimalPicture> secondPictures = animalPictures.subList(animalPicturesPerPage/2, animalPictures.size());
            model.addAttribute("firstPictures",firstPictures);
            model.addAttribute("secondPictures",secondPictures);
        } else {
            model.addAttribute("firstPictures",animalPictures);
        }
        
        List<Album> albums = albumService.getLatest(page*albumsPerPage, page*(albumsPerPage*2));
        if(albums.size() > albumsPerPage / 2){
            List<Album> firstAlbums = albums.subList(0, albumsPerPage/2);
            List<Album> secondAlbums = albums.subList(albumsPerPage/2, albums.size());
            model.addAttribute("firstAlbums", firstAlbums);
            model.addAttribute("secondAlbums", secondAlbums);
        } else {
            model.addAttribute("firstAlbums", albums);
        }
        
        if(albumService.countAlbums() > page*(albumsPerPage*2) || animalPictureService.countAnimalPictures() > page*(animalPicturesPerPage*2)){
            model.addAttribute("nextPage", page+1);
        }
        return Routes.INDEX_TEMPLATE;
    }
}
