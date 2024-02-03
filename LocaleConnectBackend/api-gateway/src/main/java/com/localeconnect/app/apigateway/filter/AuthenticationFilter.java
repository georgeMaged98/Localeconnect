package com.localeconnect.app.apigateway.filter;

import com.localeconnect.app.apigateway.dto.AuthenticationRequestDTO;
import com.localeconnect.app.apigateway.dto.AuthenticationResponseDTO;
import lombok.AllArgsConstructor;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.RouteMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private RouteValidator routeValidator;
    private WebClient webClient;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // if request not in the ignored endpoints then must authenticate
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                //read the body
                //ToDo add logic here [WIP]
            }
            return chain.filter(exchange);
        });
    }
    private Mono<AuthenticationResponseDTO> authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        return webClient.post()
                .uri("http://user-service/api/auth/authenticate")
                .bodyValue(authenticationRequestDTO)
                .retrieve()
                .bodyToMono(AuthenticationResponseDTO.class);
    }
    public static class Config{

    }
}
