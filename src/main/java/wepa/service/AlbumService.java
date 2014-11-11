/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wepa.domain.Album;
import wepa.repository.AlbumRepository;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getAll() {
        return albumRepository.findAll();
    }

    public Album save(Album album) {
        if (album.getAlbumName()==null || album.getAlbumName().isEmpty()){
            throw new IllegalArgumentException("Album name must not be empty!");
        }
        return albumRepository.save(album);
    }

    public Album find(Long id) {
        return albumRepository.findOne(id);
    }
    
    
}
