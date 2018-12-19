package isa.project.model.users;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
@Inheritance(strategy=SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=STRING)
public abstract class User implements UserDetails {
	private static final long serialVersionUID = 6586010945645182249L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="username", unique = true, nullable = false, columnDefinition="VARCHAR(64)")
	private String username;
	
	@Column(name="password", unique = false, nullable = false)
	private String password;
	
	@Column(name="firstName", unique = false, nullable = false)
	private String firstName;
	
	@Column(name="lastName", unique = false, nullable = false)
	private String lastName;
	
	@Column(name="email", unique = true, nullable = false, columnDefinition="VARCHAR(64)")
	private String email;
	
	@Column(name="phoneNumber", unique = true, nullable = true,  columnDefinition="VARCHAR(64)")
	private String phoneNumber;
	
	@Column(name="address", unique = false, nullable = false)
	private String address;
	
	@Column(name="confirmedMail", nullable = false)
	private Boolean confirmedMail;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;
	
	public User() {
		super();
	}
	
	public User(String username, String password, String firstName, String lastName, String email, String phoneNumber,
			String address) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.confirmedMail = false; //when user is created, email is not confirmed
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", phoneNumber=" + phoneNumber + ", address="
				+ address + ", confirmedMail=" + confirmedMail + "]";
	}

    @JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

    @JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

    @JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

    @JsonIgnore
	@Override
	public boolean isEnabled() {
		return confirmedMail;
	}
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
