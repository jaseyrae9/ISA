package isa.project.dto.users;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
	private static final long serialVersionUID = -6624726180748515507L;
	private String token;
	private Boolean needsPasswordChange;

	public AuthenticationResponse() {
		super();
	}

	public AuthenticationResponse(String token, Boolean needsPasswordChange) {
		this.setToken(token);
		this.needsPasswordChange = needsPasswordChange;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getNeedsPasswordChange() {
		return needsPasswordChange;
	}

	public void setNeedsPasswordChange(Boolean needsPasswordChange) {
		this.needsPasswordChange = needsPasswordChange;
	}

}
