package wepa.controllers;

import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
import wepa.repository.AlbumRepository;

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
    
//    @Autowired
//    private AnimalPictureRepository pictureRepo;

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
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        
        String messageParam = (String) res.getModelAndView().getModel().get("errorMessage");
        
        assertTrue(sizeBefore==albumRepo.count());
        assertEquals(messageExpected, messageParam);
    }
    
    
    @Test
    public void addingNamedAlbumRedirectsToSamePageandSavesAlbumCorrectly() throws Exception{
        String albumName = UUID.randomUUID().toString().substring(0, 6);
        String albumDescription = UUID.randomUUID().toString().substring(0, 6);

        Long sizeBefore = albumRepo.count();
        String messageExpected = "Album name must ndot be empty!";
        MvcResult res = mockMvc.perform(post(POST_ADDRESS)
                .param("albumName", albumName)
                .param("albumDescription",albumDescription))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        Album album = albumRepo.findOne(albumRepo.count());
        assertEquals(sizeBefore+1, albumRepo.count());
        assertEquals(albumName, album.getAlbumName());
        assertEquals(albumDescription, album.getAlbumDescription());
    }
    
    
  

}
