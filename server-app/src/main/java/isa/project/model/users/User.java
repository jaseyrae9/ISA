package isa.project.model.users;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.joda.time.DateTime;

import isa.project.model.users.security.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name="users")
@Inheritance(strategy=SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=STRING)
public abstract class User {

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Getter
	@Setter
	@Column(unique = true, nullable = false, columnDefinition="VARCHAR(64)")
	private String email;
	
	@Getter
	@Column(nullable = false)
	private String password;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private String firstName;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private String lastName;
		
	@Getter
	@Setter
	@Column(unique = true, nullable = false,  columnDefinition="VARCHAR(64)")
	private String phoneNumber;
	
	@Getter
	@Setter
	@Column
	private String address;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Boolean confirmedMail;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Boolean needsPasswordChange;
	
	@Getter
	@Column
    private Timestamp lastPasswordResetDate;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    protected Set<Authority> authorities;
		
	// Copy constructor
	public User(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.confirmedMail = user.getConfirmedMail(); 
		this.authorities = user.getUserAuthorities();
	}	
	
	public User(String username, String password, String firstName, String lastName, String phoneNumber,
			String address, Boolean needsPasswordChange) {
		super();
		this.email = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.confirmedMail = false; //when user is created, email is not confirmed
		this.authorities = new HashSet<>();
		this.needsPasswordChange = needsPasswordChange;
	}
	
	public void setPassword(String password) {
		Timestamp now = new Timestamp(DateTime.now().getMillis());
        this.lastPasswordResetDate = now;
		this.password = password;
	}
    
    public void addAuthority(Authority authority) {
    	authorities.add(authority);
    }

	public Set<Authority> getUserAuthorities() {
		return authorities;
	}

	public void setUserAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", address="
				+ address + ", confirmedMail=" + confirmedMail + "]";
	}

}
