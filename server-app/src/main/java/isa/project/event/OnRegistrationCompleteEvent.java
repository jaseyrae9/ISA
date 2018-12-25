package isa.project.event;

import org.springframework.context.ApplicationEvent;

import isa.project.model.users.Customer;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6882673804978702721L;
	private Customer customer;
	private String url;

	public OnRegistrationCompleteEvent(Customer customer, String url) {
		super(customer);
		this.customer = customer;
		this.url = url;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
