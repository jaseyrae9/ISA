package isa.project.security.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class used to receive user login in request.
 *
 */
public class JwtAuthenticationRequest {
	@NotNull (message = "Please, enter your email.")
	@NotBlank (message = "Please, enter your email.")
    private String email;
	
	@NotNull (message = "Please, enter your password")
	@NotBlank (message = "Please, enter your password.")
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
