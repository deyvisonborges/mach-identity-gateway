package com.machcommerce.identitygateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.interfaces.Claim;
import com.machcommerce.identitygateway.RouteValidator;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtFilter implements GatewayFilter {
    private final JwtService jwtService;
    private final RouteValidator routeValidator;

    public JwtFilter(JwtService jwtService, RouteValidator routeValidator) {
        this.jwtService = jwtService;
        this.routeValidator = routeValidator;
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        if (routeValidator.isSecured(request)) {
            String token = extractToken(request);

            if (token == null) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                String message = "Voce deseja solicitar uma rota protegida, mas não forneceu um token.";
                return response.writeAndFlushWith(Mono.just(
                        Mono.just(response.bufferFactory().wrap(message.getBytes()))));
            }

            Map<String, Claim> claims = jwtService.getClaimsFromToken(token);
            List<String> roles = claims.get("roles").asList(String.class);

            exchange.getRequest().mutate().header("roles", String.valueOf(roles)).build();
        }
        return chain.filter(exchange);
    }

    private String extractToken(ServerHttpRequest request) throws RuntimeException {
        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (headers != null && !headers.isEmpty()) {
            String header = headers.get(0);
            if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
                return header.substring(7);
            }
        }
        return null;
    }
}