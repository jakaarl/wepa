package wepa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.AnimalPicture;
import wepa.domain.Comment;
import wepa.domain.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {   
    
    public Page<Comment> findByPicture(AnimalPicture picture, Pageable pageable);
    
    public List<Comment> findAllByAuthor(User author);
    
    public Page<Comment> findByAuthor(User author, Pageable pageable);
    
}
