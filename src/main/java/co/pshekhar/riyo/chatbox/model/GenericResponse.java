package co.pshekhar.riyo.chatbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenericResponse {
    private String status;
    private String message;
}
