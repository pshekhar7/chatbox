package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.AppConfig;
import co.pshekhar.riyo.chatbox.exception.AppConfigException;
import co.pshekhar.riyo.chatbox.repository.AppConfigRepository;
import co.pshekhar.riyo.chatbox.util.Constants;
import co.pshekhar.riyo.chatbox.util.Utilities;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AppConfigService {
    private static final Logger log = LoggerFactory.getLogger(AppConfigService.class);
    private final AppConfigRepository appConfigRepository;

    public AppConfigService(AppConfigRepository appConfigRepository) {
        this.appConfigRepository = appConfigRepository;
    }

    public Pair<String, String> getPiiDataEncryptionCredential() throws AppConfigException {
        AppConfig appConfig = appConfigRepository.findByName(Constants.PII_ENCRYPTION_CREDENTIALS);
        Map<String, String> value;
        try {
            value = Utilities.objectMapper().readValue(appConfig.getValue(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new AppConfigException("Application config parsing error for key: [" + Constants.PII_ENCRYPTION_CREDENTIALS + "]");
        }
        if (null == value || value.size() < 1) {
            throw new AppConfigException("ApplicationDefinitionId map is not configured for key: [" + Constants.PII_ENCRYPTION_CREDENTIALS + "]");
        }
        return Pair.of(value.get("password"), value.get("iv"));
    }
}
