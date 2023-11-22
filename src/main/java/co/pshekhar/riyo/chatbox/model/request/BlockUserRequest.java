package co.pshekhar.riyo.chatbox.model.request;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class BlockUserRequest {
    @NotBlank(message = "username must not be empty")
    private String username;
    @NotBlank(message = "userToBlock must not be empty")
    private String userToBlock;

    // derived
    private User user;
}
