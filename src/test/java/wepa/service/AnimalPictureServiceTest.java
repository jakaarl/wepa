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
import wepa.domain.AnimalPicture;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class AnimalPictureServiceTest {
    
    @Autowired
    private AnimalPictureService animalPictureService;
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowNonImageTypes() throws IOException {
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "not/image", new byte [10]);
        animalPictureService.add(pictureFile, "not an image");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowEmptyImages() throws IOException {
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "image/jpeg", new byte [0]);
        animalPictureService.add(pictureFile, "empty image");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowBigImages() throws IOException {
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "image/jpeg", new byte [6*1024*1024]);
        animalPictureService.add(pictureFile, "big image");
    }
    
    @Test
    public void addShouldAllowValidImage() throws IOException {
        MultipartFile pictureFile = new MockMultipartFile("foo", "bar", "image/jpeg", new byte [10]);
        AnimalPicture picture = animalPictureService.add(pictureFile, "valid image");
        assertNotNull(picture);
    }
}
