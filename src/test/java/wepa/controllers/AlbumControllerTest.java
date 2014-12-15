package wepa.controllers;

import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import wepa.TestConfiguration;
import wepa.auth.TestUser;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.repository.AlbumRepository;
import wepa.repository.AnimalPictureRepository;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;
import wepa.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AlbumControllerTest {
    private final String POST_ADDRESS = "/albums/";

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private AlbumRepository albumRepo;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private UserService userService;
        
    @Autowired
    private AnimalPictureRepository pictureRepo;

    @Autowired
    private AnimalPictureService pictureService;
    
    @Autowired
    private TestUser testUser;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // black magic for Thymeleaf!
        webAppContext.getServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webAppContext);
       this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void creatingAlbumWithoutNameReturnsErrorMessage() throws Exception{
        MockHttpSession session = testUser.login();
        long sizeBefore = albumRepo.count();
        mockMvc.perform(post(POST_ADDRESS)
                .session(session)
                .param("name", "")
                .param("description",""))
                .andExpect(status().is3xxRedirection())
                .andReturn();
  
        assertEquals(sizeBefore, albumRepo.count());
    }
    

    
    @Test
    public void addingNamedAlbumRedirectsToSamePageAndSavesAlbumCorrectly() throws Exception{
        MockHttpSession session = testUser.login();
        String albumName = UUID.randomUUID().toString().substring(0, 6);
        String albumDescription = UUID.randomUUID().toString().substring(0, 6);
                
        mockMvc.perform(post(POST_ADDRESS)
                .session(session)
                .param("name", albumName)
                .param("description",albumDescription))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        Album album = albumService.getLatest(1).get(0);
        assertNotNull(album);
        assertEquals(1, albumRepo.count());
        assertEquals(albumName, album.getName());
        assertEquals(albumDescription, album.getDescription());
    }
    
    @Test
    public void addingPictureFileToAlbumSavesItCorrectly() throws Exception {
        MockHttpSession session = testUser.login();
        Album album = albumRepo.save(new Album("as"));
        assertNotNull(album);
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = UUID.randomUUID().toString().substring(0, 6);
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/png", content.getBytes());

        MvcResult res = mockMvc.perform(fileUpload(POST_ADDRESS + album.getId()).file(multipartFile)
                .session(session)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        album = albumRepo.findOne(album.getId());
        assertNotNull(album);
        assertEquals(1, album.getAnimalPictures().size());
        AnimalPicture picture = album.getAnimalPictures().get(0);
        assertEquals(picture.getTitle(), title);
        res = mockMvc.perform(get("/pictures/" + picture.getId() + "/src"))          
                 .andExpect(status().is2xxSuccessful())            
                 .andReturn();
        
         assertEquals(content, res.getResponse().getContentAsString());
         assertEquals( "image/png", res.getResponse().getContentType());
    }
    
    @Test
    public void addingNonPictureFileToAlbumReturnsSamePageAndNiceErrorMessage() throws Exception {
        MockHttpSession session = testUser.login();
        Album album = albumRepo.save(new Album("as"));
        assertNotNull(album);
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = "pdfdocname";
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "pdf", content.getBytes());

        mockMvc.perform(fileUpload(POST_ADDRESS + album.getId()).file(multipartFile)
                .session(session)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        album = albumRepo.findOne(album.getId());
        assertNotNull(album);
        assertEquals(0, album.getAnimalPictures().size());
        
    }

}
