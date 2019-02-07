package isa.project.service.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.reservations.FriendInviteDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.Ticket;
import isa.project.model.aircompany.Ticket.TicketStatus;
import isa.project.model.users.Customer;
import isa.project.model.users.FriendInvite;
import isa.project.model.users.FriendInvite.FriendInviteStatus;
import isa.project.repository.aircompany.TicketRespository;
import isa.project.repository.users.FriendInvitesRepository;

@Service
public class FriendInvitesService {
	@Autowired
	private FriendInvitesRepository friendInvitesRepository;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TicketRespository ticketRespository;
	
	public List<FriendInvite> getAllPendingInvites(){
		return friendInvitesRepository.getAllPendingInvites();
	}
	
	
	/**
	 * Pronalazi sve pozivnice za putovanja korisnika sa prosleđenim mejlom.
	 * @param email
	 * @return
	 */
	public List<FriendInviteDTO> getAllInvites(String email){
		List<FriendInvite> invites = friendInvitesRepository.getFriendInvites(email);
		List<FriendInviteDTO> ret = new ArrayList<>();
		for(FriendInvite invite:invites) {
			ret.add(new FriendInviteDTO(invite));
		}
		return ret;
	}
	
	/**
	 * Prihvata poziv na putovanaje.
	 * @param inviteId - oznaka poziva.
	 * @param email - email korisnika koji je poslao zahtev za prihvatanje poziva.
	 * @return - prihvaceni poziv
	 * @throws ResourceNotFoundException - nije pronadjen poziv
	 * @throws RequestDataException - poziv nije namenjen osobi koja pokusava da ga prihvati, karta nije pending
	 */
	public FriendInvite acceptInvite(Integer inviteId, String email) throws ResourceNotFoundException, RequestDataException {
		FriendInvite invite = findFriendInvite(inviteId);
		if(!invite.getFriend().getEmail().equals(email)) {
			throw new RequestDataException("Stop trying to mess with other people's invites.");
		}
		if(!invite.getStatus().equals(FriendInviteStatus.PENDING)) {
			throw new RequestDataException("Ticket is not pending. You can not accept it.");
		}
				
		invite.setStatus(FriendInviteStatus.ACCEPTED);
		
		//dodaj dodatne poene prijatelju koji je prihvatio zahtev
		Customer friend = invite.getFriend();
		Flight flight = invite.getTicketReservation().getFlightReservation().getFlight();
		friend.setLengthTravelled(friend.getLengthTravelled() + flight.getLength());
		customerService.saveCustomer(friend);		
		
		return friendInvitesRepository.save(invite);
	}
	
	/**
	 * Odbija pozivnicu za putovanje i oslobadja mesto.
	 * @param inviteId - oznaka pozivnice.
	 * @param email - email korisnika koji odbija pozivnicu
	 * @return - odbijena pozivnica
	 * @throws ResourceNotFoundException - nije pronadjena pozivnica
	 * @throws RequestDataException - pozivnica nije namenjena za datog korisnika, karta nije pending
	 */
	public FriendInvite refuseInviteWithId(Integer inviteId, String email) throws ResourceNotFoundException, RequestDataException {
		FriendInvite invite = findFriendInvite(inviteId);
		if(!invite.getFriend().getEmail().equals(email)) {
			throw new RequestDataException("Stop trying to mess with other people's invites.");
		}

		if(!invite.getStatus().equals(FriendInviteStatus.PENDING)) {
			throw new RequestDataException("Ticket is not pending. You can not accept it.");
		}
		return refuseInvite(invite);
	}
	
	/**
	 * Oznacava kartu kao odbijenu.
	 * @param invite
	 * @return
	 * @throws RequestDataException
	 */
	public FriendInvite refuseInvite(FriendInvite invite){		
		//oslobodi mesto u avionu
		Ticket ticket = invite.getTicketReservation().getTicket();
		ticket.setStatus(TicketStatus.AVAILABLE);
		ticket.setActiveReservation(null);
		ticketRespository.save(ticket);		
		
		invite.setStatus(FriendInviteStatus.REFUSED);
		return friendInvitesRepository.save(invite);
	}
	
	private FriendInvite findFriendInvite(Integer id) throws ResourceNotFoundException {
		Optional<FriendInvite> inviteOpt = friendInvitesRepository.findById(id);
		if(!inviteOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Trip invite not found.");
		}
		return inviteOpt.get();
	}
	
}
