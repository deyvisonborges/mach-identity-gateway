// package com.machcommerce.identitygateway.config;

// import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import io.netty.handler.codec.http.HttpMethod;

// @Configuration
// public class RoutingConfig {
//   // private static final String API_IDENTITY_URI = "http://localhost:4000/api";
//   private static final String API_IDENTITY_URI = "lb://mach-identity-service/api";

//   @Bean
//   private RouteLocator routeLocator(RouteLocatorBuilder builder) {
//     return builder.routes()
//         .route("test",
//             r -> r.me(HttpMethod.GET).and().path("/api/test").filters(f -> f.filter(jwtAuthenticationFilter))
//                 .uri(API_IDENTITY_URI))
//         .build();
//   }
// }
