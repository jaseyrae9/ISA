package isa.project.dto.users;

import isa.project.model.users.Customer;
import isa.project.model.users.friendship.Friendship;
import isa.project.model.users.friendship.FriendshipKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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
}
