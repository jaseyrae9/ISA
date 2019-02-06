package isa.project.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import isa.project.model.users.FriendInvite;

public interface FriendInvitesRepository extends JpaRepository<FriendInvite, Integer> {
	
	@Query("SELECT f FROM FriendInvite f WHERE f.friend.email = :email ORDER BY f.sent DESC")
	List<FriendInvite> getFriendInvites(String email);
}
