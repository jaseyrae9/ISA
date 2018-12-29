package isa.project.model.users.friendship;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "friendships")
public class Friendship{
	
	@EmbeddedId
	private FriendshipKey key;
	
	@Column(name="active", nullable = false)
	private boolean active;
	
	public Friendship() {
		
	}	

	public Friendship(FriendshipKey key, boolean active) {
		super();
		this.key = key;
		this.active = active;
	}

	public FriendshipKey getKey() {
		return key;
	}

	public void setKey(FriendshipKey key) {
		this.key = key;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}	
		
}
