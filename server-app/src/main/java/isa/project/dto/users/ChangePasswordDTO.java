package isa.project.dto.users;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class used as DTO when account password is being changed.
 *
 */
public class ChangePasswordDTO {
	@NotNull (message = "Please, enter your old password.")
	@NotBlank (message = "Please, enter your old password.")
	private String oldPassword;
	
	@NotNull (message = "Please, enter your new password.")
	@NotBlank (message = "Please, enter your new password.")
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
