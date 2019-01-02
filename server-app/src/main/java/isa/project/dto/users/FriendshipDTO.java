package isa.project.dto.users;

import isa.project.model.users.friendship.Friendship;
import isa.project.model.users.friendship.FriendshipKey;

/**
 * DTO containing friendship information.
 *
 */
public class FriendshipDTO {
	private Integer fromId;
	private Integer toId;
	private String fromFirstname;
	private String fromLastname;
	private String toFirstname;
	private String toLastname;
	private Boolean active;

	public FriendshipDTO(Friendship friendship) {
		FriendshipKey key = friendship.getKey();
		fromId = key.getFrom().getId();
		toId = key.getTo().getId();
		fromFirstname = key.getFrom().getFirstName();
		fromLastname = key.getFrom().getLastName();
		toFirstname = key.getTo().getFirstName();
		toLastname = key.getTo().getLastName();
		active = friendship.isActive();
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public String getFromFirstname() {
		return fromFirstname;
	}

	public void setFromFirstname(String fromFirstname) {
		this.fromFirstname = fromFirstname;
	}

	public String getFromLastname() {
		return fromLastname;
	}

	public void setFromLastname(String fromLastname) {
		this.fromLastname = fromLastname;
	}

	public String getToFirstname() {
		return toFirstname;
	}

	public void setToFirstname(String toFirstname) {
		this.toFirstname = toFirstname;
	}

	public String getToLastname() {
		return toLastname;
	}

	public void setToLastname(String toLastname) {
		this.toLastname = toLastname;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
