package wepa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class CommentReport extends AbstractReport {
	
    @ManyToOne
    private Comment comment;
    
    public CommentReport() {
        super();
    }
    
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}
