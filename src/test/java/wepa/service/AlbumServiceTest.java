package wepa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import wepa.Application;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.domain.User;
import wepa.repository.AlbumRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class AlbumServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private AlbumService albumService;
    
    private User testUser;
    
    private Album createAlbum() {
        return albumRepository.save(new Album("new Album"));
    }
    
    @Before
    public void onSetUp() {
       testUser = userService.getTestUser();
       Authentication auth = new UsernamePasswordAuthenticationToken(testUser, null);
       SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowNonNamedAlbums() throws IOException {
        albumService.save(new Album());
    }

    @Test
    public void shouldAddPictureAlbum() throws IOException {
        Album album = createAlbum();
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "image/jpeg", new byte [10]);
        AnimalPicture picture = albumService.addPictureToAlbum(pictureFile, "image", testUser, "image", album);
        assertNotNull(picture.getId());
        album = albumRepository.findOne(album.getId());
        assertNotNull(album);
        assertEquals(1, album.getAnimalPictures().size());
    }
    
}
