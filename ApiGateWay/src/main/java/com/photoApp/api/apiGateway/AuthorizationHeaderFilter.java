package com.photoApp.api.apiGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	@Autowired
	private Environment env;

	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	public static class Config {

	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No Authorization", HttpStatus.UNAUTHORIZED);
			}
			String auth = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = auth.replace("Bearer", "");

			if (check(jwt) == false) {
				return onError(exchange, "Jwt is not valid", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
		};

	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus unauthorized) {

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(unauthorized);

		return response.setComplete();
	}

	private boolean check(String jwt) {
		boolean returnValue = true;
		String sub = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody()
				.getSubject();

		if (sub == null || sub.isEmpty()) {
			returnValue = false;
		}

		return returnValue;
	}


}
