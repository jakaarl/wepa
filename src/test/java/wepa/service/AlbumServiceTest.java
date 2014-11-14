package wepa.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import wepa.Application;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.repository.AlbumRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class AlbumServiceTest {
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private AlbumService albumService;
    
    
    private Album createAlbum() {
        return albumRepository.save(new Album("new Album"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowNonNamedAlbums() throws IOException {
        albumService.save(new Album());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowEmptyImages() throws IOException {
        Album a = createAlbum();
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "image/jpeg", new byte [0]);
        albumService.addPictureToAlbum(pictureFile, "empty image", "empty image", a.getId());
    }
    

    @Test
    public void addShouldAllowValidImage() throws IOException {
        Album a = createAlbum();
        Long albumId = a.getId();
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "image/jpeg", new byte [10]);
        AnimalPicture picture = albumService.addPictureToAlbum(pictureFile, "image", "image", albumId);

        assertNotNull(picture);
    }
}
