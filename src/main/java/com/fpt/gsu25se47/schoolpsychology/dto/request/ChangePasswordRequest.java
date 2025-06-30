package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Current password must not be blank")
    private String currentPassword;

    @NotBlank(message = "New password must not be blank")
    @Size(min = 8, max = 50, message = "New password must be between 8 and 50 characters")
    private String newPassword;

    @NotBlank(message = "Confirm new password must not be blank")
    private String confirmNewPassword;
}
