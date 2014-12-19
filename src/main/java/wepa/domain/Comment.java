package wepa.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
public class Comment extends AbstractPersistable<Long> {
    
    @NotBlank
    private String comment;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;
    @ManyToOne
    private User author;
    @ManyToOne
    private AnimalPicture picture;
    
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<CommentReport> commentReports = new ArrayList<>();
    
    public Comment(){
        created = new Date();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public AnimalPicture getPicture() {
        return picture;
    }

    public void setPicture(AnimalPicture picture) {
        this.picture = picture;
    }

    public List<CommentReport> getCommentReports() {
        return commentReports;
    }

    public void setCommentReports(List<CommentReport> commentReports) {
        this.commentReports = commentReports;
    }
    
}
