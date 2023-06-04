package one.terenin.security;

import lombok.Getter;
import lombok.Setter;
import one.terenin.security.propertysource.JWTPropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
@Getter
@Setter
public class MyPasswordEncoder implements PasswordEncoder {

    private final String INSTANCE = "PBKDF2WithHmacSHA512";

    private JWTPropertySource propertySource;

    public MyPasswordEncoder(JWTPropertySource propertySource) {
        this.propertySource = propertySource;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] encoded = SecretKeyFactory.getInstance(INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            propertySource.getPasswordEncoderSecret().getBytes(),
                            propertySource.getPasswordEncoderIteration(),
                            propertySource.getPasswordEncoderKeyLength())).getEncoded();
            return Base64.getEncoder().encodeToString(encoded);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException invalidKeyException) {
            throw new RuntimeException(invalidKeyException);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(encodedPassword).equals(rawPassword);
    }
}
