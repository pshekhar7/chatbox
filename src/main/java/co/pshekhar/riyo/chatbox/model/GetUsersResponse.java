package co.pshekhar.riyo.chatbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetUsersResponse {
    private String status;
    private List<String> data;
}
