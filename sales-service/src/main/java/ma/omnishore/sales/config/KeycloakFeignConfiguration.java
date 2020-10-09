package ma.omnishore.sales.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
@ConditionalOnProperty(prefix = "keycloak", name = "auth-server-url", matchIfMissing = false)
public class KeycloakFeignConfiguration {

    @Bean
    public RequestInterceptor getFeignClientInterceptor() {
        return new FeignClientInterceptor();
    }
}
