package fr.bgsoft.incredy.config;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Value("${jwt.auth.converter.resource-id}")
	private String resourceId;

	@Value("${jwt.auth.converter.principal-attribute}")
	private String principalAttribute;

	@Override
	public AbstractAuthenticationToken convert(final Jwt jwt) {
		final Collection<GrantedAuthority> authorities = Stream
				.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
				.collect(Collectors.toSet());
		final String claimName = principalAttribute == null ? JwtClaimNames.SUB : principalAttribute;
		return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
	}

	private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {
		final Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
		Map<String, Object> resource;
		Collection<String> resourceRoles;
		if ((resourceAccess == null)
				|| ((resource = (Map<String, Object>) resourceAccess.get(resourceId)) == null)
				|| ((resourceRoles = (Collection<String>) resource.get("roles")) == null)) {
			return Set.of();
		}
		return resourceRoles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toSet());
	}
}