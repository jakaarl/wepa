package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    
}
