package co.pshekhar.riyo.chatbox.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserLoginRequest {
    @NotBlank(message = "username must not be blank")
    private String username;
    @NotBlank(message = "passcode must not be blank")
    private String passcode;
}
