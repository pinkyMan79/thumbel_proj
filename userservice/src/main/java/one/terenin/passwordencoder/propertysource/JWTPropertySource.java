package one.terenin.passwordencoder.propertysource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.yml")
public class JWTPropertySource {

    private String passwordEncoderSecret;
    private Integer passwordEncoderIteration;
    private Integer passwordEncoderKeyLength;
    private String jwtSecret;
    private Integer jwtExpiration;
    private String jwtIssuer;

    public JWTPropertySource(@Value("${jwt.password.encoder.secret}") String passwordEncoderSecret,
                             @Value("${jwt.password.encoder.iteration}") String passwordEncoderIteration,
                             @Value("${jwt.password.encoder.keylength}") String passwordEncoderKeyLength,
                             @Value("${jwt.secret}") String jwtSecret,
                             @Value("${jwt.expiration}") String jwtExpiration,
                             @Value("${jwt.issuer}") String jwtIssuer) {
        this.passwordEncoderSecret = passwordEncoderSecret;
        this.passwordEncoderIteration = Integer.parseInt(passwordEncoderIteration);
        this.passwordEncoderKeyLength = Integer.parseInt(passwordEncoderKeyLength);
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = Integer.parseInt(jwtExpiration);
        this.jwtIssuer = jwtIssuer;
    }
}
