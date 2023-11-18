package co.pshekhar.riyo.chatbox.model;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ChatHistoryRequest {
    private String friend;
    private String user;

    //derived
    private User forUser;
}
