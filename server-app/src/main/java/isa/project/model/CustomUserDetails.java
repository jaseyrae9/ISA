package isa.project.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import isa.project.model.users.Authority;
import isa.project.model.users.User;

public class CustomUserDetails extends User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8362933053338873751L;
	
	public CustomUserDetails(final User user)
	{
		super(user);
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		// Ispis ispis
		for(Authority a : super.getUserAuthorities())
		{
			System.err.println("Ima auth: " + a.getAuthority());
		}
		
		return super.getUserAuthorities()
				.stream()
				.map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getAuthority()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
