
package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.Album;


public interface AlbumRepository extends JpaRepository<Album, Long> {

}
