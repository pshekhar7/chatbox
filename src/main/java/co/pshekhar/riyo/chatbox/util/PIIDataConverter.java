package co.pshekhar.riyo.chatbox.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.context.annotation.Lazy;

@Converter
public class PIIDataConverter implements AttributeConverter<String, String> {
    private final GenericCipherUtil cipherUtil;

    public PIIDataConverter(@Lazy GenericCipherUtil cipherUtil) {
        this.cipherUtil = cipherUtil;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return cipherUtil.encryptToHex(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return cipherUtil.decryptFromHex(dbData);
    }
}
