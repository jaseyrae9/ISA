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

@Entity
@Table(name="users")
@Inheritance(strategy=SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=STRING)
public abstract class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(unique = true, nullable = false, columnDefinition="VARCHAR(64)")
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
		
	@Column(unique = true, nullable = false,  columnDefinition="VARCHAR(64)")
	private String phoneNumber;
	
	@Column
	private String address;
	
	@Column(nullable = false)
	private Boolean confirmedMail;
	
	@Column(nullable = false)
	private Boolean needsPasswordChange;
	
	@Column
    private Timestamp lastPasswordResetDate;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    protected Set<Authority> authorities;
	
	public User() {
		super();
	}
	
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
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		Timestamp now = new Timestamp(DateTime.now().getMillis());
        this.setLastPasswordResetDate( now );
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}	
		
	public Boolean getConfirmedMail() {
		return confirmedMail;
	}

	public void setConfirmedMail(Boolean confirmedMail) {
		this.confirmedMail = confirmedMail;
	}

	public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
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
	
	public Boolean getNeedsPasswordChange() {
		return needsPasswordChange;
	}

	public void setNeedsPasswordChange(Boolean needsPasswordChange) {
		this.needsPasswordChange = needsPasswordChange;
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
