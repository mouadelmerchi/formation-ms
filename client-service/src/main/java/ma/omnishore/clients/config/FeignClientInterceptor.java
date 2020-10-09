package ma.omnishore.clients.config;

import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignClientInterceptor.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {

        if(template.headers().containsKey(AUTHORIZATION_HEADER)) {
            log.debug("'{}' header already exists", AUTHORIZATION_HEADER);
            return;
        }

        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext == null) {
            log.debug("There is not SecurityContext in the request");
            return;
        }

        final Authentication authentication = securityContext.getAuthentication();
        if(authentication == null) {
            log.debug("Cannot retrieve Token because there is no Authentication Details in the request");
            return;
        }

        if(!(authentication.getPrincipal() instanceof KeycloakPrincipal)) {
            log.debug("Cannot retrieve Token because Authentication Principal is not of type KeycloakPrincipal");
            return;
        }

        final KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) authentication.getPrincipal();
        if(keycloakPrincipal.getKeycloakSecurityContext() == null) {
            log.debug("Cannot retrieve Token because there is not KeycloakSecurityContext in KeycloakPrincipal");
            return;
        }

        template.header(AUTHORIZATION_HEADER,
                String.format("%s %s", BEARER_TOKEN_TYPE, keycloakPrincipal.getKeycloakSecurityContext().getTokenString()));
    }
}
