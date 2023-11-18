package co.pshekhar.riyo.chatbox.model.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UnreadMsg {
    private String username;
    private List<String> texts;
}
