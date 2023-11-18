package co.pshekhar.riyo.chatbox.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class Utilities {
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setTimeZone(TimeZone.getTimeZone(Constants.IND_ZONE_ID));

    public static ObjectMapper objectMapper() {
        return mapper;
    }

    public static ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now(ZoneId.of(Constants.IND_ZONE_ID));
    }

}
