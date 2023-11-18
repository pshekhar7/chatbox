package co.pshekhar.riyo.chatbox.domain;

import co.pshekhar.riyo.chatbox.util.PIIDataConverter;
import co.pshekhar.riyo.chatbox.util.UniqueIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GenericGenerator(name = "user_id", type = UniqueIdGenerator.class)
    @GeneratedValue(generator = "user_id")
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(unique = true)
    private String username;

    @Convert(converter = PIIDataConverter.class)
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedOn;

}
