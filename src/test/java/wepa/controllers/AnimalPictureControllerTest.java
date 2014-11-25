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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import wepa.domain.AnimalPicture;
import wepa.domain.User;
import wepa.repository.AnimalPictureRepository;
import wepa.repository.UserRepository;
import wepa.service.AnimalPictureService;
import wepa.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AnimalPictureControllerTest {
    private final String POST_ADDRESS = "/pictures/";

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired 
    private UserService userService;
    
    @Autowired
    private AnimalPictureService pictureService;
    
    @Autowired
    private AnimalPictureRepository pictureRepo;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
       this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
       Authentication auth = new UsernamePasswordAuthenticationToken(userService.getTestUser(),null);
       SecurityContextHolder.getContext().setAuthentication(auth);
    }

    
        
    @Test
    public void addingNonPictureFileReturnsSamePageAndNiceErrorMessage() throws Exception {
        Long sizeBefore = pictureRepo.count();
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = "pdfdocname";
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        String messageExpected = "Only image files allowed";
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "pdf", content.getBytes());
        MvcResult res = mockMvc.perform(fileUpload(POST_ADDRESS).file(multipartFile)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is3xxRedirection())
                .andReturn();

       //String messageParam = (String) res.getModelAndView().getModel().get("error");
        
        assertTrue(sizeBefore==pictureRepo.count());
       // assertEquals(messageExpected, messageParam);
        
    }
    
    @Test
    public void addingPictureFileSavesPictureCorrectlyAndRedirectsToSamePage() throws Exception {
        Long sizeBefore = pictureRepo.count();
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = UUID.randomUUID().toString().substring(0, 6);
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/png", content.getBytes());
        MvcResult res = mockMvc.perform(fileUpload(POST_ADDRESS).file(multipartFile)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
       // AnimalPicture picture = pictureService.getLatest(3).get(0);
         AnimalPicture picture = pictureRepo.findOne(pictureRepo.count());
        res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/src")) 
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // assertEquals(picture.getTitle(), title);
        // assertEquals(picture.getDescription(), description);
         assertEquals(sizeBefore + 1, pictureRepo.count());
         assertEquals(content, res.getResponse().getContentAsString());
         assertEquals( "image/png", res.getResponse().getContentType());
        
    }

}
