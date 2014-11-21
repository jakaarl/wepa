package wepa.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.AlbumReport;

public interface AlbumReportRepository extends JpaRepository<AlbumReport, Long> {
    
}
