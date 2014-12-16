package wepa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class AlbumReport extends AbstractReport {
	
    @ManyToOne
    private Album album;
    
    public AlbumReport() {
        super();
    }
    
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    
}
