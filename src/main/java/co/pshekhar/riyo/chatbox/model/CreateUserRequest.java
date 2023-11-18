package co.pshekhar.riyo.chatbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CreateUserRequest {
    private String username;
    private String passcode;
}
