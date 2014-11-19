
package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wepa.domain.AnimalPicture;
import wepa.domain.Comment;
import wepa.domain.User;
import wepa.repository.AnimalPictureRepository;
import wepa.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired 
    private CommentRepository commentRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AnimalPictureRepository animalPictureRepository;
        
    
    public List<Comment> getLatestComments(AnimalPicture picture, int maxCount) {
        Pageable limit = new PageRequest(0, maxCount, Sort.Direction.DESC, "created");
        return commentRepository.findByPicture(picture, limit).getContent();
    }
    
    public List<Comment> getLatestComments(User user, int maxCount) {
        Pageable limit = new PageRequest(0, maxCount, Sort.Direction.DESC, "created");
        return commentRepository.findByAuthor(user, limit).getContent();
    }
    
    public List<Comment> getAllCommentsByUser(User user) {
        return commentRepository.findAllByAuthor(user);
    }
    
    public Comment addComment(Long pictureId, String comment){
        if (comment.isEmpty()){
            throw new IllegalArgumentException("Comment can not be empty!");   
        }
        
        AnimalPicture animalPicture = animalPictureRepository.findOne(pictureId);
        if(animalPicture == null){
            throw new IllegalArgumentException("Animal picture was not found!");
        }
        
        User user = userService.getAuthenticatedPerson();
        if(user == null){
            throw new IllegalArgumentException("First you must login!");
        }
        Comment c = new Comment();
        c.setComment(comment);
        c.setPicture(animalPicture);
        c.setAuthor(user);
        c = commentRepository.save(c);
        animalPicture.getComments().add(c);
        animalPictureRepository.save(animalPicture);
        user.getComments().add(c);
        userService.save(user);
        return c;
    }
}
