package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.users.Customer;
import isa.project.model.users.friendship.Friendship;
import isa.project.model.users.friendship.FriendshipKey;
import isa.project.repository.users.CustomerRepository;
import isa.project.repository.users.FriendshipRepository;

@Service
public class FriendshipService {
	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Creates new friendship request.
	 * 
	 * @param from - email of person sending request
	 * @param to   - id of user receiving the request
	 * @throws ResourceNotFoundException - if user receiving request is not found
	 * @throws RequestDataException      - if user tries to send friend request to
	 *                                   self, or to someone he has friend request
	 *                                   from, or to a friend
	 */
	public Friendship sendFriendRequest(String email, Integer to)
			throws ResourceNotFoundException, RequestDataException {
		// Pronadji oba korisnika
		Optional<Customer> fromCustOpt = customerRepository.findByEmail(email);
		Optional<Customer> toCustOpt = customerRepository.findById(to);
		if (!toCustOpt.isPresent()) {
			throw new ResourceNotFoundException(to.toString(), "User not found");
		}
		Customer fromCust = fromCustOpt.get();
		Customer toCust = toCustOpt.get();

		// Proveri da li korisnik zahtev salje samom sebi
		if (fromCust.getId().equals(toCust.getId())) {
			throw new RequestDataException("You are trying to send friendrequest to yourself.");
		}

		// proveri da li vec postoji zahtev u suprotnom smeru
		if (friendshipRepository.findFriendship(fromCust.getId(), toCust.getId()).isPresent()) {
			throw new RequestDataException("There's already existing friendship or friend request.");
		}

		Friendship friendship = new Friendship(new FriendshipKey(fromCust, toCust), false);
		return friendshipRepository.save(friendship);
	}

	/**
	 * Accepts friend request.
	 * 
	 * @param from  - id of user whose friend request is being accepted
	 * @param email - email of user accepting friend request
	 * @return - accepted friendship
	 * @throws ResourceNotFoundException - if user whose request is being accepted
	 *                                   is not found, or friend request is not
	 *                                   found
	 */
	public Friendship acceptRequest(Integer from, String email) throws ResourceNotFoundException {
		// pronadji oba kupca
		Optional<Customer> toCustOpt = customerRepository.findByEmail(email);
		Optional<Customer> fromCustOpt = customerRepository.findById(from);
		if (!fromCustOpt.isPresent()) {
			throw new ResourceNotFoundException(from.toString(), "User not found");
		}
		Customer fromCust = fromCustOpt.get();
		Customer toCust = toCustOpt.get();

		// pronadji zahtev
		Optional<Friendship> friendship = friendshipRepository.findRequest(fromCust.getId(), toCust.getId());
		if (!friendship.isPresent()) {
			throw new ResourceNotFoundException(from.toString(), "You don't have friend request from this user.");
		}
		if (friendship.get().isActive()) {
			throw new ResourceNotFoundException(from.toString(), "You are already friends with this user.");
		}

		// prihvati zahtev
		friendship.get().setActive(true);
		return friendshipRepository.save(friendship.get());
	}

	/**
	 * Delete friend or friendship request
	 * 
	 * @param otherPerson - id of user whose friend request/friendship is being
	 *                    delete
	 * @param from        - email of user deleting friend request
	 * @throws ResourceNotFoundException - if other user of friendship is not found
	 */
	public void deleteRequest(Integer otherPerson, String from) throws ResourceNotFoundException {
		// pronadji oba kupca
		Optional<Customer> toCustOpt = customerRepository.findByEmail(from);
		Optional<Customer> fromCustOpt = customerRepository.findById(otherPerson);
		if (!fromCustOpt.isPresent()) {
			throw new ResourceNotFoundException(from.toString(), "User not found");
		}
		Customer fromCust = fromCustOpt.get();
		Customer toCust = toCustOpt.get();

		// pronadji zahtev
		Optional<Friendship> friendship = friendshipRepository.findFriendship(fromCust.getId(), toCust.getId());
		if (!friendship.isPresent()) {
			throw new ResourceNotFoundException(from.toString(), "You don't have friend request from this user.");
		}
		
		//izbrisi prijateljstvo
		friendshipRepository.delete(friendship.get());
	}

	/**
	 * Gets friend requests sent to users.
	 * 
	 * @param email - email of user asking to see friend requests
	 * @return - list of friend requests
	 */
	public Page<Friendship> getFriendshipRequests(String email, Pageable page){
		Optional<Customer> to = customerRepository.findByEmail(email);
		return friendshipRepository.findFriendshipRequests(to.get().getId(), page);
	}

	/**
	 * Gets list of friendship that user has
	 * 
	 * @param email - email of user asking to see friendships
	 * @return - friendships
	 */
	public Page<Friendship> getFriendships(String email, Pageable page){
		Optional<Customer> person = customerRepository.findByEmail(email);
		return friendshipRepository.findActiveFriendships(person.get().getId(), page);
	}

}
