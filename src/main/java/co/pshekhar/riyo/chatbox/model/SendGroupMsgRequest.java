package co.pshekhar.riyo.chatbox.model;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SendGroupMsgRequest {
    private String from;
    private List<String> toList;
    private String text;

    //derived
    private User fromUser;
}
