package isa.project.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.users.friendship.Friendship;
import isa.project.model.users.friendship.FriendshipKey;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipKey> {
	@Query("SELECT f FROM Friendship f WHERE f.key.to.id = :to AND f.active = 0")
	List<Friendship> findFriendshipRequests(@Param("to")Integer to);
	
	@Query("SELECT f FROM Friendship f WHERE (f.key.to.id = :user OR f.key.from.id = :user) AND f.active = 1")
	List<Friendship> findActiveFriendships(@Param("user")Integer user);
	
	@Query("SELECT f FROM Friendship f WHERE (f.key.to.id = :user1 AND f.key.from.id = :user2) OR (f.key.to.id = :user2 AND f.key.from.id = :user1)")
	Friendship findFriendship(@Param("user1")Integer user1, @Param("user2")Integer user2);
}
