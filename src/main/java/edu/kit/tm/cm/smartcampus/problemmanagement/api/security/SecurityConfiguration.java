package edu.kit.tm.cm.smartcampus.problemmanagement.api.security;

import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;

@Configuration
@EnableGlobalMethodSecurity(proxyTargetClass = true, securedEnabled = true)
public class SecurityConfiguration {

  @Bean
  public GrpcAuthenticationReader authenticationReader() {
    return new BearerAuthenticationReader(BearerTokenAuthenticationToken::new);
  }

  /*
  @Bean
  AuthenticationManager authenticationManager() {
    final List<AuthenticationProvider> providers = new ArrayList<>();
    providers.add(new JwtAuthenticationProvider(new JwtDecoder() {
      @Override
      public Jwt decode(String token) throws JwtException {
        return new Jwt();
      }
    }); // Possibly JwtAuthenticationProvider
    return new ProviderManager(providers);
  }
   */
}
