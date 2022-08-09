package edu.kit.tm.cm.smartcampus.problemmanagement.api.configuration;

import edu.kit.tm.cm.proto.ProblemManagementGrpc;
import edu.kit.tm.cm.smartcampus.problemmanagement.api.configuration.converter.KeyCloakGrantedAuthoritiesConverter;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.error.ClientExceptionInterceptor;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a {@link Configuration} for spring, it contains of important application
 * settings and beans used upon the application.
 *
 * @author Bastian Bacher, Dennis Fadeev
 */
@Configuration
public class ServerConfiguration {

  /**
   * The {@link RestTemplate} {@link Bean} used as tool for the connectors to call and obtain data
   * from connected domain microservices, it is capable of parsing api responses directly to model
   * objects of this microservice, it reduces a lot of own implementation and JSON parsing.
   *
   * @return the rest template used in this service
   */
  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new ClientExceptionInterceptor());
    return restTemplate;
  }

  /**
   * This class contains all necessary {@link Bean}s needed to accomplish server sided security, via
   * oauth2 and keycloak. "grpc-spring-boot-starter" has full "spring-security" support and just
   * needed a valid {@link GrpcAuthenticationReader}, as well as a {@link JwtAuthenticationProvider}
   * inside a {@link AuthenticationManager}.
   *
   * @author Bastian Bacher, Dennis Fadeev
   */
  @Configuration
  @EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
  public static class SecurityConfiguration {

    @Value("${spring.security.oauth2.client.provider.cm-pse-22.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${role.user}")
    private String user;

    @Value("${role.admin}")
    private String admin;

    /**
     * This method creates a {@link JwtAuthenticationConverter} to convert {@link Authentication} to
     * {@link JwtAuthenticationToken}. Therefor it uses the custom {@link
     * KeyCloakGrantedAuthoritiesConverter}.
     *
     * @return the jwt authentication converter bean
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
      final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
      converter.setJwtGrantedAuthoritiesConverter(keyCloakGrantedAuthoritiesConverter());
      return converter;
    }

    /**
     * This method creates a {@link KeyCloakGrantedAuthoritiesConverter} to convert {@link
     * GrantedAuthority}.
     *
     * @return the keycloak granted authorities converter bean
     */
    @Bean
    public KeyCloakGrantedAuthoritiesConverter keyCloakGrantedAuthoritiesConverter() {
      return new KeyCloakGrantedAuthoritiesConverter();
    }

    /**
     * This method provides a {@link JwtAuthenticationProvider} bean.
     *
     * @return the jwt authentication provider bean
     */
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
      final JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder());
      provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
      return provider;
    }

    /**
     * This method creates a {@link AuthenticationManager} bean to authenticate a provided
     * authentication token.
     *
     * @return the authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager() {
      final List<AuthenticationProvider> providers = new ArrayList<>();
      providers.add(jwtAuthenticationProvider());
      return new ProviderManager(providers);
    }

    /**
     * This method provides a {@link GrpcAuthenticationReader} bean, to read the authentication
     * token from grpc metadata headers.
     *
     * @return the grpc authentication reader bean
     */
    @Bean
    public GrpcAuthenticationReader authenticationReader() {
      return new BearerAuthenticationReader(BearerTokenAuthenticationToken::new);
    }

    /**
     * This method creates a {@link JwtDecoder} bean, to decode a jwt token, it uses an
     * implementation of {@link NimbusJwtDecoder} connected to jwkSetUri provided in application
     * properties.
     *
     * @return the jwt decoder bean
     */
    @Bean
    public JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * This bean represents some {@link GrpcSecurityMetadataSource}, set to provide access
     * information about various rpc methods.
     *
     * @return the grpc metadata source bean
     */
    @Bean
    GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
      return new ManualGrpcSecurityMetadataSource()
          .set(ProblemManagementGrpc.getChangeStateMethod(), AccessPredicate.hasRole(admin))
          .set(ProblemManagementGrpc.getRemoveProblemMethod(), AccessPredicate.hasRole(admin))
          .set(ProblemManagementGrpc.getUpdateProblemMethod(), AccessPredicate.hasRole(admin))
          .set(
              ProblemManagementGrpc.getCreateProblemMethod(),
              AccessPredicate.hasAnyRole(user, admin))
          .setDefault(AccessPredicate.permitAll());
    }

    /**
     * Create an access {@link AccessDecisionManager} bean, which decides if access is granted or
     * denied based on AccessPredicates.
     *
     * @return the access decision manager bean
     */
    @Bean
    AccessDecisionManager accessDecisionManager() {
      final List<AccessDecisionVoter<?>> voters = new ArrayList<>();
      voters.add(new AccessPredicateVoter());
      return new UnanimousBased(voters);
    }
  }
}
