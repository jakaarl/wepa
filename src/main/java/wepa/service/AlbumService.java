/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wepa.service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.domain.User;
import wepa.repository.AlbumRepository;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private AnimalPictureService pictureService;
    
    @Autowired
    private UserService userService;

    public List<Album> getAll() {
        return albumRepository.findAll();
    }
    
    public List<Album> getLatestAlbums(User user, int maxCount) {
        Pageable limit = new PageRequest(0, maxCount, Sort.Direction.DESC, "created");
        return albumRepository.findByAuthor(user, limit).getContent();
    }
    
    public List<Album> getLatest(int maxCount) {
        Pageable limit = new PageRequest(0, maxCount, Sort.Direction.DESC, "created");
        return albumRepository.findAll(limit).getContent();
    }
    
    public List<Album> getAllAlbumsByUser(User user) {
        return albumRepository.findAllByAuthor(user);
    }

    public Album save(Album album) {
        if (album.getName()==null || album.getName().isEmpty()){
            throw new IllegalArgumentException("Album name must not be empty!");
        }
        
        User user = userService.getAuthenticatedPerson();
        if(user == null){
            throw new IllegalArgumentException("First you must login!");
        }
        
        album.setAuthor(user);
        
        return albumRepository.save(album);
    }
    
    public Album find(Long id) {
        return albumRepository.findOne(id);
    }

    public AnimalPicture addPictureToAlbum(MultipartFile file, String title, User user, String description,
            Long albumId) throws IllegalArgumentException, IOException {
        Album album = albumRepository.findOne(albumId);
        AnimalPicture picture = pictureService.add(file, title, user, description, album);
        picture.setAlbum(album);
        album.getAnimalPictures().add(picture);
        albumRepository.save(album);
        return picture;
    }
    
    
}
