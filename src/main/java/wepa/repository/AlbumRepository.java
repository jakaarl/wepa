
package wepa.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.Album;
import wepa.domain.User;


public interface AlbumRepository extends JpaRepository<Album, Long> {
    public List<Album> findAllByAuthor(User author);
    public Page<Album> findByAuthor(User author, Pageable pageable);
}
