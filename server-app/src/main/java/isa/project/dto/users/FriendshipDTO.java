package isa.project.dto.users;

import isa.project.model.users.friendship.Friendship;

/**
 * Klasa za slanje informacija o prijateljstvu.
 *
 */
public class FriendshipDTO {
	private String from_firstname;
	private String from_lastname;
	private String to_firstname;
	private String to_lastname;
	private Boolean active;

	public FriendshipDTO(Friendship friendship) {
		from_firstname = friendship.getKey().getFrom().getFirstName();
		from_lastname = friendship.getKey().getFrom().getLastName();
		to_firstname = friendship.getKey().getTo().getFirstName();
		to_lastname = friendship.getKey().getTo().getLastName();
		active = friendship.isActive();
	}

	public String getFrom_firstname() {
		return from_firstname;
	}

	public void setFrom_firstname(String from_firstname) {
		this.from_firstname = from_firstname;
	}

	public String getFrom_lastname() {
		return from_lastname;
	}

	public void setFrom_lastname(String from_lastname) {
		this.from_lastname = from_lastname;
	}

	public String getTo_firstname() {
		return to_firstname;
	}

	public void setTo_firstname(String to_firstname) {
		this.to_firstname = to_firstname;
	}

	public String getTo_lastname() {
		return to_lastname;
	}

	public void setTo_lastname(String to_lastname) {
		this.to_lastname = to_lastname;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
