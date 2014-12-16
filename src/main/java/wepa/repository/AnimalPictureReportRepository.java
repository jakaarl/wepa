package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.AnimalPictureReport;

public interface AnimalPictureReportRepository extends JpaRepository<AnimalPictureReport, Long> {
    
}
