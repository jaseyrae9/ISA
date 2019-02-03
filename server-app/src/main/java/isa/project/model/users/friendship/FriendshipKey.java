package isa.project.model.users.friendship;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.model.users.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class FriendshipKey implements Serializable {
	private static final long serialVersionUID = -4887821352931854328L;

	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(referencedColumnName = "id")
	private Customer from;

	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(referencedColumnName = "id")
	private Customer to;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FriendshipKey))
			return false;
		FriendshipKey that = (FriendshipKey) o;
		if (!from.equals(that.getFrom()))
			return false;
		return to.equals(that.getTo());
	}

	@Override
	public int hashCode() {
		int result = from.hashCode();
		result = 31 * result + to.hashCode();
		return result;
	}

}
