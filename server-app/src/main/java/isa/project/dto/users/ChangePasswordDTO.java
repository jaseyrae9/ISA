package isa.project.dto.users;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordDTO {
	@NotBlank(message = "Please, enter your old password.")
	private String oldPassword;
	@NotBlank(message = "Please, enter your new password.")
	private String newPassword;
}
