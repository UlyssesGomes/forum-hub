package br.com.forum_hub.infra.scurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers("/login/**").permitAll();
                            req.requestMatchers(HttpMethod.GET, "/courses").permitAll();

                            req.requestMatchers(HttpMethod.DELETE, "/topics/{id}").hasAnyRole("PARTICIPANT", "MODERATOR");
                            req.requestMatchers(HttpMethod.GET, "/topics/**").permitAll();

                            req.requestMatchers(HttpMethod.PATCH, "/users/deactivate-account/{id}").hasAnyRole("PARTICIPANT", "MODERATOR", "ADMIN");
                            req.requestMatchers("/users/**").hasRole("ADMIN");

                            req.requestMatchers(HttpMethod.PATCH, "topics/{idTopic}/responses/{id}").hasAnyRole("PARTICIPANT", "MODERATOR");
                            req.requestMatchers(HttpMethod.DELETE, "topics/{idTopic}/responses/{id}").hasAnyRole("PARTICIPANT", "MODERATOR");
                            req.requestMatchers(HttpMethod.PUT, "topics/{idTopic}/responses").hasRole("MODERATOR");

                            req.requestMatchers("/h2-console/**").permitAll(); // Allow h2-console routes
                            req.anyRequest().authenticated();
                        }
                )
                .headers(headers -> headers              //
                        .frameOptions(frame -> frame.sameOrigin())  // Allow h2-console iframe
                )                                                                     //
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEnconder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy hierarquiaPerfis(){
        String hierarchy = "ROLE_ADMIN > ROLE_MODERATOR\n"+
                "ROLE_MODERATOR > ROLE_PARTICIPANT";
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }
}
