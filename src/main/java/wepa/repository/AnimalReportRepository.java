package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.AnimalReport;

public interface AnimalReportRepository extends JpaRepository<AnimalReport, Long> {
    
}
