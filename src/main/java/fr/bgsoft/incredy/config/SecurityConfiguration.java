package fr.bgsoft.incredy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private final JwtAuthConverter jwtAuthConverter;

	public SecurityConfiguration(final JwtAuthConverter jwtAuthConverter) {
		this.jwtAuthConverter = jwtAuthConverter;
	}

	@Bean
	public SecurityFilterChain resourceServerFilterChain(final HttpSecurity http) throws Exception {
		http
		//.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
		.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/api/v1/test/**"))
				.permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/v1/**", HttpMethod.GET.name())).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/actuator")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/swagger-ui/*")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/v3/api-docs")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/unauthenticated")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
				.anyRequest().authenticated());
		http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
		.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
