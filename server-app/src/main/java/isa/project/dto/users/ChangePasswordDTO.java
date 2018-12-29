package isa.project.dto.users;

import javax.validation.constraints.NotBlank;

/**
 * Class used as DTO when account password is being changed.
 *
 */
public class ChangePasswordDTO {
	@NotBlank(message = "Please, enter your old password.")
	private String oldPassword;
	@NotBlank(message = "Please, enter your new password.")
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
