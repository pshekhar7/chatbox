package co.pshekhar.riyo.chatbox.domain;

import co.pshekhar.riyo.chatbox.util.PIIDataConverter;
import co.pshekhar.riyo.chatbox.util.Utilities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE session SET expired = 'true' WHERE id = ?")
@Where(clause = "expired <> 'true'")
public class Session {
    public Session() {
        expired = Boolean.FALSE;
    }

    @Id
    @GeneratedValue
    private String id;

    @Column(unique = true)
    @Convert(converter = PIIDataConverter.class)
    private String token;

    @ManyToOne
    private User user;

    private Boolean expired;

    @CreationTimestamp
    @Column(updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedOn;

    @JsonIgnore
    public boolean isExpired() {
        return Utilities.getCurrentTime().plusMinutes(15L).isBefore(createdOn);
    }
}
