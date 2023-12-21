package de.mscho.toftws.mvc;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@UserDtoValidator.Constraint
public class UserDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String token;
}