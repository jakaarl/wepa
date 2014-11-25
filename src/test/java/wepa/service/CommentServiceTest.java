package wepa.service;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wepa.Application;
import wepa.domain.AnimalPicture;
import wepa.domain.Comment;
import wepa.domain.User;
import wepa.repository.AnimalPictureRepository;
import wepa.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CommentServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AnimalPictureRepository pictureRepo;
    
    @Autowired
    private CommentService commentService;
    
    
    private AnimalPicture createPicture() {
        return pictureRepo.save(new AnimalPicture());
    }
    
    @Before
    public void onSetUp() {
       Authentication auth = new UsernamePasswordAuthenticationToken(userService.getTestUser(),null);
       SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowNonNamedAlbums() throws IOException {
        commentService.addComment(createPicture().getId(), "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addShouldDisallowSavigCommentForNonExistingPicture() throws IOException {
        commentService.addComment(new Long(Long.MAX_VALUE), "some regular comment");
    }
    

    @Test
    public void addShouldAllowValidComment() throws IOException {
        Long pictureId = createPicture().getId();
        String commentTitle = "some comment";
//        Comment c = commentService.addComment(pictureId, commentTitle);
//        assertNotNull(c);
//        assertEquals(c.getPicture().getId(), pictureId);
//        assertEquals(c.getComment(), commentTitle);
    }
    
}
