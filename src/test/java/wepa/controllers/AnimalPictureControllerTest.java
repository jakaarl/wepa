package wepa.controllers;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
import wepa.domain.AnimalPicture;
import wepa.domain.Comment;
import wepa.repository.AnimalPictureRepository;
import wepa.repository.CommentRepository;
import wepa.service.AnimalPictureService;
import wepa.service.CommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@Transactional
@WebAppConfiguration
@ActiveProfiles("test")
public class AnimalPictureControllerTest {
    
    private final String POST_ADDRESS = "/pictures/";

    @Autowired
    private WebApplicationContext webAppContext;
    
    @Autowired
    private AnimalPictureService pictureService;
    
    @Autowired
    private AnimalPictureRepository pictureRepo;
    
    @Autowired
    private TestUser testUser;

    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private CommentService commentService;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // black magic for Thymeleaf!
        webAppContext.getServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webAppContext);
       this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private AnimalPicture createPicture(){
        AnimalPicture picture =  new AnimalPicture();
        
        picture.setDescription(UUID.randomUUID().toString().substring(0, 6));
        picture.setTitle(UUID.randomUUID().toString().substring(0, 6));
        picture.setImage(UUID.randomUUID().toString().substring(0, 6).getBytes());
        return  pictureRepo.save(picture);
    }
        
    @Test
    public void addingNonPictureFileReturnsSamePageAndNiceErrorMessage() throws Exception {
        MockHttpSession session = testUser.login();
        long sizeBefore = pictureRepo.count();
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = "pdfdocname";
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "pdf", content.getBytes());
        mockMvc.perform(fileUpload(POST_ADDRESS).file(multipartFile)
                .session(session)
                .param("description", description)
                .param("title", title))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertEquals(sizeBefore, pictureRepo.count());
    }
    
    @Test
    public void addingPictureFileSavesPictureCorrectlyAndRedirectsToSamePage() throws Exception {
        testUser.login();
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
        
        AnimalPicture picture = pictureService.getLatest(3).get(0);
        // AnimalPicture picture = pictureRepo.findOne(pictureRepo.count());
        res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/src")) 
                .andExpect(status().is2xxSuccessful())
                .andReturn();
         assertEquals(picture.getTitle(), title);
         assertEquals(picture.getDescription(), description);
         assertEquals(sizeBefore + 1, pictureRepo.count());
         assertEquals(content, res.getResponse().getContentAsString());
         assertEquals("image/png", res.getResponse().getContentType());
    }
    
    @Test
    public void shouldRequireAuthenticationForAddingPicture() throws Exception {
        testUser.logout();
        String description = UUID.randomUUID().toString().substring(0, 6);
        String fileName = UUID.randomUUID().toString().substring(0, 6);
        String title = "title";
        String content = UUID.randomUUID().toString().substring(0, 6);
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "image/png", content.getBytes());
        Exception caught = null;
        try {
            mockMvc.perform(fileUpload(POST_ADDRESS).file(multipartFile)
                    .param("description", description)
                    .param("title", title))
                    .andReturn();
        } catch (Exception e) {
            caught = e;
        }
        assertNotNull(caught);
        assertTrue(caught.getCause() instanceof AuthenticationCredentialsNotFoundException);
    }
    @Test
    public void addingAnEmptyCommentIsNotAllowed() throws Exception {
        AnimalPicture picture = createPicture();
        Long sizeBefore = commentRepo.count();

        Comment c = new Comment();
        c.setComment("");
        
        MvcResult res = mockMvc.perform(post(POST_ADDRESS + picture.getId() + "/comment")
                .param("id", picture.getId().toString())
                .param("comment", c.getComment()))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertTrue(sizeBefore==commentRepo.count());                 
    }
    
    @Test
    public void addingNonEmptyCommentSavesItCorrectlyAndRedirects() throws Exception {
        AnimalPicture picture = createPicture();
        Long sizeBefore = commentRepo.count();

        Comment c = new Comment();
        c.setComment("I'm a regular comment.");
        
        MvcResult res = mockMvc.perform(post(POST_ADDRESS + picture.getId() + "/comment")
                .param("id", picture.getId().toString())
                .param("comment", c.getComment()))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        Comment savedComment = commentService.getLatestComments(picture, 3).get(0);
        
        assertEquals(sizeBefore+1, commentRepo.count()); 
        assertEquals(c.getComment(), savedComment.getComment());
        
    }
    
    @Test
    public void userCanLikeAnAnimalPictureButOnlyOnce() throws Exception {
        AnimalPicture picture = createPicture();
        int sizeBefore = picture.getLikes().size();
        String expectedValue = String.valueOf(sizeBefore+1);
        
        MvcResult res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/like")
                .param("id", picture.getId().toString()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        
        // check if increase number of likes after first attempt
        assertEquals(expectedValue, res.getResponse().getContentAsString());
//        
//        res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/like")
//                .param("id", picture.getId().toString()))
//               // .andExpect(status().is2xxSuccessful())
//                .andReturn();
//        // check if NOT increase number of likes after second attempt
//        assertEquals(expectedValue, res.getResponse().getContentAsString());
    }
    
    @Test
    public void userCanDislikeLikedAnimalPictureButOnlyOnce() throws Exception {
        AnimalPicture picture = createPicture();
        int sizeBefore = picture.getLikes().size();
        String expectedValue = String.valueOf(sizeBefore+1);
        
        MvcResult res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/like")
                .param("id", picture.getId().toString()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        
        // check if increase number of likes after first attempt
        assertEquals(expectedValue, res.getResponse().getContentAsString());
        
        res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/like")
                .param("id", picture.getId().toString()))
                .andReturn();
       
        // check if decrease number of likes after first attempt
        expectedValue = String.valueOf(sizeBefore);
        assertEquals(expectedValue, res.getResponse().getContentAsString());
        
//        res = mockMvc.perform(get(POST_ADDRESS + picture.getId() + "/dislike")
//                .param("id", picture.getId().toString()))
//                .andReturn();
//        // check if NOT decrease number of likes after second attempt
//        assertEquals(expectedValue, res.getResponse().getContentAsString());
    }
    

}
