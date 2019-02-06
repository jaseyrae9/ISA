package isa.project.dto.reservations;

import isa.project.model.users.FriendInvite;
import isa.project.model.users.FriendInvite.FriendInviteStatus;
import isa.project.model.users.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FriendInviteDTO {
	private Integer id;
	private ReservationDTO reservationDTO;
	private FriendInviteStatus status;
	private String invitedBy;
	
	public FriendInviteDTO(FriendInvite invite) {
		this.id = invite.getId();
		this.status = invite.getStatus();
		Reservation res = invite.getTicketReservation().getFlightReservation().getReservation();
		this.invitedBy = res.getCustomer().getFirstName() + " " + res.getCustomer().getLastName();
		this.reservationDTO = new ReservationDTO(res);
	}
}
