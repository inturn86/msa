package com.sdc.gatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.HttpHeaders;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	Environment env;

	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}


	public static class Config {

	}

	//login 0> token -> users(with token) -> header(include token)
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			//Header에 Authorization이 있는지 확인
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.replace("Bearer", "");

			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}

			return chain.filter(exchange);
		});
	}

	private boolean isJwtValid(String jwt) {

		boolean returnValue = true;

		String subject = null;


		try {
			subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
					.parseClaimsJws(jwt).getBody()
					.getSubject();
		} catch (Exception e) {
			returnValue = false;
		}

		if (subject == null || subject.isEmpty()) {
			returnValue = false;
		}
		System.out.println();
		return returnValue;
	}

	//Mono, Flux -> Sping WebFlux client 요청이 들어왔을 경우 반환
	//Spring mvc 를 사용하지 않고 spring webflux를 사용(비동기 처리 방식을 사용)
	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);

		log.error(err);
		return response.setComplete();
	}

}
