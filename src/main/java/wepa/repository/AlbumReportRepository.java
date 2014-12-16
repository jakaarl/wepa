package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.AlbumReport;

public interface AlbumReportRepository extends JpaRepository<AlbumReport, Long> {
    
}
