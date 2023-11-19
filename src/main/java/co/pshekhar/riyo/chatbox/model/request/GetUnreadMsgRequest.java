package co.pshekhar.riyo.chatbox.model.request;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GetUnreadMsgRequest {
    @NotBlank(message = "username must not be blank")
    private String username;

    // derived
    private User user;
}
