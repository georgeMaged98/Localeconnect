package com.localeconnect.app.apigateway.filter;

import com.localeconnect.app.apigateway.dto.AuthenticationRequestDTO;
import com.localeconnect.app.apigateway.dto.AuthenticationResponseDTO;
import lombok.AllArgsConstructor;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.RouteMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private RouteValidator routeValidator;
    private WebClient.Builder webClientBuilder;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // if request not in the ignored endpoints then must authenticate
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                return handleAuthentication(exchange, chain);
            }
            return chain.filter(exchange);
        };
    }

    // This method authenticates the request and adds the JWT to the headers if authentication is successful
    private Mono<Void> handleAuthentication(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // Extract credentials from the request
        AuthenticationRequestDTO credentials = extractCredentials(request);
        // Authenticate and obtain JWT
        return authenticate(credentials).flatMap(authenticationResponseDTO -> {
            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("Authorization", "Bearer " + authenticationResponseDTO.getToken())
                    .build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }).onErrorResume(e -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        });
    }

    // This method extracts user credentials from the request body
    private AuthenticationRequestDTO extractCredentials(ServerHttpRequest request) {
        //todo complete this
        return new AuthenticationRequestDTO("test", "pass");
    }

    private Mono<AuthenticationResponseDTO> authenticate(AuthenticationRequestDTO credentials) {
        return webClientBuilder.build().post()
                .uri("http://user-service/api/auth/login")
                .bodyValue(credentials)
                .retrieve()
                .bodyToMono(AuthenticationResponseDTO.class);
    }
    public static class Config{

    }
}
