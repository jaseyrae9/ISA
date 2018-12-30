package isa.project.repository.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.users.friendship.Friendship;
import isa.project.model.users.friendship.FriendshipKey;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipKey> {
	/**
	 * Pronalazi neprihvaćene zahteve za prijateljstvo korisnika sa identifikacijom
	 * to.
	 * 
	 * @param to - čije zahteve treba pronaći
	 * @return - list pronađenih zahteva
	 */
	@Query("SELECT f FROM Friendship f WHERE f.key.to.id = :to AND f.active = 0")
	List<Friendship> findFriendshipRequests(@Param("to") Integer to);

	/**
	 * Pronalazi sve prijatelje osobe sa identifikacijom user.
	 * 
	 * @param user - čije prijatelje treba pronaći
	 * @return - listu prijateljstava
	 */
	@Query("SELECT f FROM Friendship f WHERE (f.key.to.id = :user OR f.key.from.id = :user) AND f.active = 1")
	List<Friendship> findActiveFriendships(@Param("user") Integer user);

	/**
	 * Pronalazi zahtev upućen od strane osobe from osobi to, bez obzira da li je
	 * zahtev prihvaćen ili ne.
	 * 
	 * @param from - pošiljalac zahteva
	 * @param to   - primalac zahteva
	 * @return
	 */
	@Query("SELECT f FROM Friendship f WHERE f.key.to.id = :from AND f.key.from.id = :to")
	Optional<Friendship> findRequest(@Param("to") Integer from, @Param("from") Integer to);

	/**
	 * Pronalazi prijateljstvo između osoba user1 i user2, ignorišući ko je poslao
	 * zahtev, bez obzira da li je zahtev prihvaćen ili ne.
	 * 
	 * @param user1 - osoba 1
	 * @param user2 - osoba 2
	 * @return
	 */
	@Query("SELECT f FROM Friendship f WHERE (f.key.to.id = :user1 AND f.key.from.id = :user2) OR (f.key.to.id = :user2 AND f.key.from.id = :user1)")
	Optional<Friendship> findFriendship(@Param("user1") Integer user1, @Param("user2") Integer user2);
}
