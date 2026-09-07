package cl.duoc.pedidos360.productos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Este microservicio actua como Resource Server OAuth2: no genera tokens,
 * solo los valida.
 *
 * La validacion real (firma contra el JWKS de Azure AD, issuer y audience)
 * se configura declarativamente en application.yml, bajo:
 *   spring.security.oauth2.resourceserver.jwt.issuer-uri
 *
 * Spring Security descubre automaticamente el JWKS de Azure AD a partir
 * del issuer-uri (OpenID Connect Discovery) y valida cada request.
 *
 * NOTA IMPORTANTE (defensa en profundidad):
 * El AWS API Gateway ya valida el JWT en el borde (JWT Authorizer contra
 * el JWKS de Azure). Esta configuracion vuelve a validar el token aqui,
 * en el propio microservicio, para que este nunca confie ciegamente en
 * lo que le llegue, incluso si alguien accediera a la instancia EC2
 * sin pasar por el API Gateway.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                // Catalogo de lectura publica (ajustar segun negocio)
                .requestMatchers("GET", "/api/productos", "/api/productos/**").permitAll()
                // Escritura del catalogo requiere token JWT valido
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * Traduce los "scopes"/"roles" que vienen dentro del JWT de Azure AD
     * (claims como "scp" o "roles") en authorities de Spring Security,
     * para poder usar @PreAuthorize("hasAuthority('SCOPE_xxx')") en los
     * controladores si se requiere mas adelante.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("scp");
        authoritiesConverter.setAuthorityPrefix("SCOPE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    /**
     * CORS a nivel de microservicio (defensa adicional). El requisito del
     * proyecto pide configurar CORS principalmente en el AWS API Gateway,
     * pero se deja tambien aqui para pruebas locales del backend sin pasar
     * por el Gateway.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
