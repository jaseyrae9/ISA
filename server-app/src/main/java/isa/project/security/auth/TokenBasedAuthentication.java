package isa.project.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import isa.project.security.TokenUtils;

public class TokenBasedAuthentication extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private TokenUtils tokenUtils = new TokenUtils();
	private String token;
	private final UserDetails principle;

	public TokenBasedAuthentication(UserDetails principle) {
		super(principle.getAuthorities());
		this.principle = principle;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public boolean isAuthenticated() {
		return tokenUtils.validateToken(token, principle);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public UserDetails getPrincipal() {
		return principle;
	}

}
