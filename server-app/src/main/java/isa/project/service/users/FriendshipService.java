package isa.project.service.users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	 * Kreira novo prijateljstvo. Kreirano prijateljstvo nije potvrđeno.
	 * 
	 * @param from - ko šalje zahtev
	 * @param to   - kome se zahtev šalje
	 * @throws ResourceNotFoundException - ukoliko nije pronađen neki od korisnika
	 */
	public Friendship sendFriendRequest(String email, Integer to) throws ResourceNotFoundException {
		Optional<Customer> from_cust = customerRepository.findByEmail(email);
		if (!from_cust.isPresent()) {
			throw new ResourceNotFoundException(email, "User not found");
		}
		Optional<Customer> to_cust = customerRepository.findById(to);
		if (!to_cust.isPresent()) {
			throw new ResourceNotFoundException(to.toString(), "User not found");
		}
		Friendship friendship = new Friendship(new FriendshipKey(from_cust.get(), to_cust.get()), false);
		return friendshipRepository.save(friendship);
	}

	/**
	 * Prihvata zahtev za prijateljstvo.
	 * 
	 * @param from - čiji zahtev se prihvata
	 * @param email - korisnika koji prihvata zahtev
	 * @return - prihvaćeno prijateljstvo
	 * @throws ResourceNotFoundException - ako nije pronađen korisnik ili zahtev
	 */
	public Friendship acceptRequest(Integer from, String email) throws ResourceNotFoundException {
		Optional<Customer> to_cust = customerRepository.findByEmail(email);
		if (!to_cust.isPresent()) {
			throw new ResourceNotFoundException(email, "User not found");
		}
		Optional<Customer> from_cust = customerRepository.findById(from);
		if (!from_cust.isPresent()) {
			throw new ResourceNotFoundException(from.toString(), "User not found");
		}
		Optional<Friendship> friendship = friendshipRepository.findRequest(from_cust.get().getId(),
				to_cust.get().getId());
		if (!friendship.isPresent()) {
			throw new ResourceNotFoundException(from.toString(), "You don't have friend request from this user.");
		}
		if (friendship.get().isActive()) {
			throw new ResourceNotFoundException(from.toString(), "You are already friends with this user.");
		}
		friendship.get().setActive(true);
		return friendshipRepository.save(friendship.get());
	}
	
	/**
	 * Briše postojeći zahtev za prijateljstvo.
	 * 
	 * @param otherPerson - čiji zahtev se briše
	 * @param email - korisnika koji briše zahtev
	 * @throws ResourceNotFoundException - ako nije pronađen korisnik ili zahtev
	 */
	public void deleteRequest(Integer otherPerson, String email) throws ResourceNotFoundException {
		Optional<Customer> to_cust = customerRepository.findByEmail(email);
		if (!to_cust.isPresent()) {
			throw new ResourceNotFoundException(email, "User not found");
		}
		Optional<Customer> from_cust = customerRepository.findById(otherPerson);
		if (!from_cust.isPresent()) {
			throw new ResourceNotFoundException(otherPerson.toString(), "User not found");
		}
		Optional<Friendship> friendship = friendshipRepository.findFriendship(from_cust.get().getId(),
				to_cust.get().getId());
		if (!friendship.isPresent()) {
			throw new ResourceNotFoundException(otherPerson.toString(), "Friendship or friend request don't exist.");
		}
				
		friendshipRepository.delete(friendship.get());
	}

	/**
	 * Vraća listu zahteva za prijateljstvo za korisnika sa prosleđenim mejlom.
	 * 
	 * @param email - mejl korisnika čiji zahtevi se traže
	 * @return - lista nepotvrđenih zahteva
	 * @throws ResourceNotFoundException
	 */
	public List<Friendship> getFriendshipRequests(String email) throws ResourceNotFoundException {
		Optional<Customer> to = customerRepository.findByEmail(email);
		if (!to.isPresent()) {
			throw new ResourceNotFoundException(email, "User not found");
		}
		return friendshipRepository.findFriendshipRequests(to.get().getId());
	}
	
	/**
	 * Vraća listu prijatelja korisnika sa prosleđenim mejlom.
	 * 
	 * @param email - mejl korisnika čiji zahtevi se traže
	 * @return - lista potvrđenih zahteva
	 * @throws ResourceNotFoundException
	 */
	public List<Friendship> getFriendships(String email) throws ResourceNotFoundException {
		Optional<Customer> person = customerRepository.findByEmail(email);
		if (!person.isPresent()) {
			throw new ResourceNotFoundException(email, "User not found");
		}
		return friendshipRepository.findActiveFriendships(person.get().getId());
	}
	

}
