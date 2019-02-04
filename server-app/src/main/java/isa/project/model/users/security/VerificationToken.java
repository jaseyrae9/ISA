package isa.project.model.users.security;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import isa.project.model.users.Customer;
import isa.project.model.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class VerificationToken {
	private static final int EXPIRATION = 60 * 24;

	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String token;

	@OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	private Date expiryDate;

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	public VerificationToken(String token, User user) {
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}	
}
