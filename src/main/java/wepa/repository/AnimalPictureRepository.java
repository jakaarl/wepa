package wepa.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.AnimalPicture;
import wepa.domain.User;

public interface AnimalPictureRepository extends JpaRepository<AnimalPicture, Long> {
    public List<AnimalPicture> findAllByAuthor(User author);
    public Page<AnimalPicture> findByAuthor(User author, Pageable pageable);
}
