package co.pshekhar.riyo.chatbox.model.request;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ChatHistoryRequest {
    @NotBlank(message = "text must not be blank")
    private String friend;
    @NotBlank(message = "text must not be blank")
    private String user;

    //derived
    private User forUser;

    @AssertTrue(message = "friend and user must be different")
    boolean isValid() {
        if (StringUtils.isBlank(friend) || StringUtils.isBlank(user)) return true;
        return !friend.equals(user);
    }
}
