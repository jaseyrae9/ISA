package isa.project.dto.reservations;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketReservationRequestDTO {
	private String firstName;
	private String lastName;
	private String passport;
	@NotNull (message = "Ticket can not be blank.")
	private Long ticketId;
	@Min(value = 0, message = "Status can be 0 - for customer, 1 - for unregistred user, 2 - friend")
	@Max(value = 2, message = "Status can be 0 - for customer, 1 - for unregistred user, 2 - friend")
	@NotNull (message = "Status can not be blank.")
	private Integer status;
	private Integer friendId;
	
	public boolean checkIfBasicDataIsValid() {
		if(status == 0) {
			//ako je za korisnika ne treba nista dodatno
			return true;
		}
		else if(status == 1) {
			if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || passport.trim().isEmpty()) {
				return false;
			}
			return true;
		}
		return true;
	}
}
