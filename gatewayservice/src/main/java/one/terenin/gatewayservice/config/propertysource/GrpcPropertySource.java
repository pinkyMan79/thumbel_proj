package one.terenin.gatewayservice.config.propertysource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Data
@PropertySource("classpath:/application.yaml")
public class GrpcPropertySource {

    String serviceHost;
    Integer serviceGrpcPort;

    public GrpcPropertySource(@Value("grpc.servicePort") Integer serviceGrpcPort) {
        this.serviceHost = "localhost";
        this.serviceGrpcPort = serviceGrpcPort;
    }
}
