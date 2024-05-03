package dz.lab22.lab22;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MaConfiguration {
    @Bean
    public BCryptPasswordEncoder pwdEncoder() { return new BCryptPasswordEncoder(); }
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bcrypt) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(bcrypt.encode("123")).roles("USER").build());
        // manager.createUser(User.withUsername("admin")
        //        .password(bcrypt.encode("987")).roles("USER", "ADMIN").build());
        return manager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.authorizeRequests().anyRequest().authenticated().and().httpBasic();

        http.authorizeHttpRequests(request -> request
                .requestMatchers("/login").permitAll()
                .requestMatchers("/").hasRole("USER")
                // .requestMatchers("/admin/**").hasRole("ADMIN")
                // .requestMatchers("/portail/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().authenticated()
        );
        http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
        // http.logout(LogoutConfigurer::permitAll);
        http.exceptionHandling(exception -> exception.accessDeniedPage("/403"));
        return http.build();
    }
}

