package isa.project.model.users.friendship;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "friendships")
public class Friendship{
	
	@EmbeddedId
	private FriendshipKey key;
	
	@Column(name="active", nullable = false)
	private boolean active;			
}
