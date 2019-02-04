package isa.project.dto.users;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse implements Serializable {
	private static final long serialVersionUID = -6624726180748515507L;
	private String token;
	private Boolean needsPasswordChange;
	
	public AuthenticationResponse(String token, Boolean needsPasswordChange) {
		this.setToken(token);
		this.needsPasswordChange = needsPasswordChange;
	}
}
