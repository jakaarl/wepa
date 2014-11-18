/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wepa.service;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.repository.AlbumRepository;
import wepa.repository.AnimalPictureRepository;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private AnimalPictureService pictureService;

    public List<Album> getAll() {
        return albumRepository.findAll();
    }
    
    // TODO: Get all albums for user
    
    // TODO: Get latest albums for user

    public Album save(Album album) {
        if (album.getAlbumName()==null || album.getAlbumName().isEmpty()){
            throw new IllegalArgumentException("Album name must not be empty!");
        }
        return albumRepository.save(album);
    }
    
    public Album find(Long id) {
        return albumRepository.findOne(id);
    }

    public AnimalPicture addPictureToAlbum(MultipartFile file, String title, String description, Long albumId) throws IllegalArgumentException, IOException {
        Album album = albumRepository.findOne(albumId);    
        AnimalPicture picture = pictureService.add(file, title, description, album);
        picture.setAlbum(album);
        album.getAnimalPictures().add(picture);
        albumRepository.save(album);
        return picture;
    }
    
    
}
