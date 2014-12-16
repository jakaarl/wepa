package wepa.domain;

import java.util.Date;

import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
public abstract class AbstractReport extends AbstractPersistable<Long> {

	private String reason;
	protected Date created;
	@ManyToOne
	private User sentBy;

	public AbstractReport() {
		this.created = new Date();
	}

	public String getReason() {
	    return reason;
	}

	public void setReason(String reason) {
	    this.reason = reason;
	}
	
	public Date getCreated() {
		return created;
	}

	public User getSentBy() {
	    return sentBy;
	}

	public void setSentBy(User sentBy) {
	    this.sentBy = sentBy;
	}

}