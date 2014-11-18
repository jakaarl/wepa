package wepa.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wepa.domain.AnimalPicture;
import wepa.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {   
    public Page<Comment> findByPicture(AnimalPicture picture, Pageable pageable);    
}
