package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import isa.project.model.users.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	public Optional<Customer> findByEmail(String email);

	/**
	 * Pronalazi kupce čije ime i prezime uključuju termin pretrage. Isklučuje kupca
	 * sa prosleđenim id-om.
	 * 
	 * @param searchTerm - termin koji ime i prezime treba da sadrže
	 * @param id - id koji treba ne uključiti u pretragu
	 * @param Page
	 * @return
	 */
	@Query("SELECT c FROM Customer c WHERE lower(CONCAT(c.firstName, ' ', c.lastName)) LIKE :searchTerm AND c.id<>:id")
	public Page<Customer> searchByName(String searchTerm, Integer id, Pageable page);
}
