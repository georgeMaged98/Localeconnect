package com.localeconnect.app.apigateway.config;

//import com.localeconnect.app.apigateway.filter.AuthenticationFilter;
import com.localeconnect.app.apigateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
   private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
//                .route("chat-service", r -> r.path("/api/chat/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://CHAT"))
//                .route("meetup-service", r -> r.path("/api/meetup/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://MEETUP"))
                .route("user-service", r -> r.path("/api/user/**")
                        .filters(f -> f.filter(filter))
                      .uri("http://user-service:8084"))
//                .route("itinerary-service", r -> r.path("/api/itinerary/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://ITINERARY"))
//                .route("feed-service", r -> r.path("/api/feed/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://FEED"))
//                .route("trip-service", r -> r.path("/api/trip/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://TRIP"))
//                .route("notification-service", r -> r.path("/api/notification/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://NOTIFICATION"))
                .route("authentication-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://authentication-service:8500"))
                .build();
    }
}

