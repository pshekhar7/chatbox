package co.pshekhar.riyo.chatbox.util;

import co.pshekhar.riyo.chatbox.exception.AppConfigException;
import co.pshekhar.riyo.chatbox.service.AppConfigService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

@Component
class GenericCipherUtil {

    Logger log = LoggerFactory.getLogger(GenericCipherUtil.class);

    private final String CHARACTER_ENCODING = "UTF-8";
    private final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private final String ALGORITHM = "AES";
    private final SecretKeySpec secretKey;
    private final IvParameterSpec ivParameterSpec;
    static final String ENCRYPTION_DATA_ERROR_MESSAGE = "Error occurred while encrypting data";
    static final String DECRYPTION_DATA_ERROR_MESSAGE = "Error occurred while decrypting data";


    GenericCipherUtil(AppConfigService appConfigService) throws UnsupportedEncodingException, AppConfigException {
        // init encryption and decryption ciphers
        Pair<String, String> credential = appConfigService.getPiiDataEncryptionCredential();
        final byte[] passwordBytes = credential.getLeft().getBytes(CHARACTER_ENCODING);
        final byte[] ivBytes = credential.getRight().getBytes(CHARACTER_ENCODING);
        secretKey = new SecretKeySpec(passwordBytes, ALGORITHM);
        ivParameterSpec = new IvParameterSpec(ivBytes);
    }

    public String encryptToHex(String plainText) {
        if (StringUtils.isBlank(plainText)) return null;
        byte[] encrypted = encrypt(plainText);
        return Hex.encodeHexString(encrypted);
    }

    public String decryptFromHex(String cipherText) {
        if (StringUtils.isBlank(cipherText)) return null;
        try {
            return decrypt(Hex.decodeHex(cipherText));
        } catch (Exception ex) {
            log.error("Exception occurred while decrypting PII Data. Error - ${ex.getMessage()}");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, DECRYPTION_DATA_ERROR_MESSAGE);
        }
    }

    private byte[] encrypt(String plainText) {
        if (StringUtils.isBlank(plainText)) return null;
        try {
            final Cipher encryptionCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            encryptionCipher.init(1, secretKey, ivParameterSpec);
            return encryptionCipher.doFinal(plainText.getBytes(CHARACTER_ENCODING));
        } catch (Exception ex) {
            log.error("Exception occurred while encrypting PII Data. Error - ${ex.getMessage()}");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ENCRYPTION_DATA_ERROR_MESSAGE);
        }
    }

    private String decrypt(byte[] cipherTextByte) {
        if (null == cipherTextByte) return null;
        try {
            final Cipher decryptionCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            decryptionCipher.init(2, secretKey, ivParameterSpec);
            return new String(decryptionCipher.doFinal(cipherTextByte), CHARACTER_ENCODING);
        } catch (Exception ex) {
            log.error("Exception occurred while decrypting PII Data. Error - ${ex.getMessage()}");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, DECRYPTION_DATA_ERROR_MESSAGE);
        }
    }
}
