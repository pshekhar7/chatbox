package co.pshekhar.riyo.chatbox.model;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SendMsgRequest {
    private String from;
    private String to;
    private String text;

    //derived
    private User fromUser;
}
