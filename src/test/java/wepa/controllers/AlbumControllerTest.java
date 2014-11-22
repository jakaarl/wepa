package wepa.controllers;

import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import wepa.Application;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.repository.AlbumRepository;
import wepa.repository.AnimalPictureRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AlbumControllerTest {
    private final String POST_ADDRESS = "/albums/";

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private AlbumRepository albumRepo;
    
    @Autowired
    private AnimalPictureRepository pictureRepo;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void creatingAlbumWithoutNameReturnsErrorMessage() throws Exception{
        Long sizeBefore = albumRepo.count();
        String messageExpected = "Album name must not be empty!";
        MvcResult res = mockMvc.perform(post(POST_ADDRESS)
                .param("albumName", "")
                .param("albumDescription",""))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        

        assertTrue(sizeBefore==albumRepo.count());
       // assertEquals(messageExpected, messageParam);
    }
    
    
    @Test
    public void addingNamedAlbumRedirectsToSamePageAndSavesAlbumCorrectly() throws Exception{
        String albumName = UUID.randomUUID().toString().substring(0, 6);
        String albumDescription = UUID.randomUUID().toString().substring(0, 6);

        Long sizeBefore = albumRepo.count();
        String messageExpected = "Album name must not be empty!";
        MvcResult res = mockMvc.perform(post(POST_ADDRESS)
                .param("albumName", albumName)
                .param("albumDescription",albumDescription))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        Album album = albumRepo.findOne(albumRepo.count());
        assertEquals(sizeBefore+1, albumRepo.count());
        assertEquals(albumName, album.getName());
        assertEquals(albumDescription, album.getDescription());
    }
    
    @Test
    public void addingPictureFileSavesItCorrectly() throws Exception {
        Album album = albumRepo.save(new Album("as"));
        Long sizeBefore = pictureRepo.count();
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = UUID.randomUUID().toString().substring(0, 6);
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/png", content.getBytes());

        MvcResult res = mockMvc.perform(fileUpload(POST_ADDRESS + album.getId()).file(multipartFile)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        AnimalPicture picture = pictureRepo.findOne(pictureRepo.count());
        assertEquals(picture.getTitle(), title);
        assertEquals(picture.getDescription(), description);
        res = mockMvc.perform(get("/" + picture.getId()))          
                 .andExpect(status().is2xxSuccessful())            
                 .andReturn();
        
         assertEquals(content, res.getResponse().getContentAsString());
         assertEquals( "image/png", res.getResponse().getContentType());
         assertEquals(sizeBefore + 1, pictureRepo.count());
        
    }
    
    @Test
    public void addingNonPictureFileReturnsSamePageAndNiceErrorMessage() throws Exception {
        Album album = albumRepo.save(new Album("as"));
        Long sizeBefore = pictureRepo.count();
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = "pdfdocname";
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        String messageExpected = "Only image files allowed";
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "pdf", content.getBytes());

        MvcResult res = mockMvc.perform(fileUpload(POST_ADDRESS + album.getId()).file(multipartFile)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

       String messageParam = (String) res.getModelAndView().getModel().get("errorMessage");
        
        assertTrue(sizeBefore==pictureRepo.count());
        assertEquals(messageExpected, messageParam);
        
    }

}
