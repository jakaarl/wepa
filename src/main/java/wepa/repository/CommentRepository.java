package wepa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.AnimalPicture;
import wepa.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
   
    public List<Comment> findByPicture(AnimalPicture picture);    

}
