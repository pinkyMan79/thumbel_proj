package one.terenin.gatewayservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    private static final String BASE_URL_AUTH_SERVICE = "https://localhost:8083/api/v1/auth/";
    private static final String BASE_URL_USER_SERVICE = "https://localhost:8082/api/v1/user/";
    private static final String BASE_URL_MEDIA_SERVICE = "https://localhost:8081/api/v1/file/";
    private static final String BASE_URL_PHOTO_SERVICE = "https://localhost:8079/api/v1/photo/";
    public static final int TIMEOUT = 1000;

    @Bean
    public WebClient authServiceWebClient() {
        return getWebClient(BASE_URL_AUTH_SERVICE);
    }
    @Bean
    public WebClient userServiceWebClient() {
        return getWebClient(BASE_URL_USER_SERVICE);
    }
    @Bean
    public WebClient mediaServiceWebClient() {
        return getWebClient(BASE_URL_MEDIA_SERVICE);
    }
    @Bean
    public WebClient photoServiceWebClient() {
        return getWebClient(BASE_URL_PHOTO_SERVICE);
    }

    private WebClient getWebClient(String url) {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

}
