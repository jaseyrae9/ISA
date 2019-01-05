package isa.project.dto.users;

import isa.project.model.users.Customer;
import isa.project.model.users.friendship.Friendship;
import isa.project.model.users.friendship.FriendshipKey;

/**
 * DTO containing friendship information.
 *
 */
public class FriendshipDTO {
	private Integer user1Id;
	private Integer user2Id;
	private String user1Firstname;
	private String user1Lastname;
	private String user2Firstname;
	private String user2Lastname;
	//0 - prijatelji, 1 - primljen zahtev, 2 - poslan zahtev, 3 - nista
	private int status;
	
	public FriendshipDTO(Friendship friendship, boolean isSender) {
		FriendshipKey key = friendship.getKey();
		if(isSender) {
			user1Id = key.getFrom().getId();
			user1Firstname = key.getFrom().getFirstName();
			user1Lastname = key.getFrom().getLastName();
			user2Id = key.getTo().getId();
			user2Firstname = key.getTo().getFirstName();
			user2Lastname = key.getTo().getLastName();
		}
		else {
			user2Id = key.getFrom().getId();
			user2Firstname = key.getFrom().getFirstName();
			user2Lastname = key.getFrom().getLastName();
			user1Id = key.getTo().getId();
			user1Firstname = key.getTo().getFirstName();
			user1Lastname = key.getTo().getLastName();
		}
		
		if(friendship.isActive()) {
			status = 0;
		}
		else if(!isSender) {
			status = 1;
		}
		else {
			status = 2;
		}		
	}
	
	public FriendshipDTO(Customer customer) {
		user2Id = customer.getId();
		user2Firstname = customer.getFirstName();
		user2Lastname = customer.getLastName();
		status = 3;
	}

	public Integer getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(Integer user1Id) {
		this.user1Id = user1Id;
	}

	public Integer getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(Integer user2Id) {
		this.user2Id = user2Id;
	}

	public String getUser1Firstname() {
		return user1Firstname;
	}

	public void setUser1Firstname(String user1Firstname) {
		this.user1Firstname = user1Firstname;
	}

	public String getUser1Lastname() {
		return user1Lastname;
	}

	public void setUser1Lastname(String user1Lastname) {
		this.user1Lastname = user1Lastname;
	}

	public String getUser2Firstname() {
		return user2Firstname;
	}

	public void setUser2Firstname(String user2Firstname) {
		this.user2Firstname = user2Firstname;
	}

	public String getUser2Lastname() {
		return user2Lastname;
	}

	public void setUser2Lastname(String user2Lastname) {
		this.user2Lastname = user2Lastname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}	
}
