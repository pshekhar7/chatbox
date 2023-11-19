package co.pshekhar.riyo.chatbox.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserLoginResponse {
    private String status;
    private String sessionToken;
}
