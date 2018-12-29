package isa.project.model.users.friendship;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.model.users.Customer;

@Embeddable
public class FriendshipKey implements Serializable {
	private static final long serialVersionUID = -4887821352931854328L;

	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(referencedColumnName = "id")
	private Customer from;

	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(referencedColumnName = "id")
	private Customer to;

	public FriendshipKey() {

	}

	public FriendshipKey(Customer from, Customer to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Customer getFrom() {
		return from;
	}

	public void setFrom(Customer from) {
		this.from = from;
	}

	public Customer getTo() {
		return to;
	}

	public void setTo(Customer to) {
		this.to = to;
	}

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
