package co.pshekhar.riyo.chatbox.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
public class AppConfig {

    @Id
    String name;

    @Column(length = 512)
    String value;

    public Map<String, String> getJson() {
        Map<String, String> json = new HashMap<>();
        json.put("name", name);
        json.put("value", value);
        return json;
    }
}
