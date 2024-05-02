package dz.lab22.lab22;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MaConfiguration {
    @Bean
    public BCryptPasswordEncoder pwdEncoder() { return new BCryptPasswordEncoder(); }
    @Bean public UserDetailsService userDetailsService(BCryptPasswordEncoder bcrypt) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(bcrypt.encode("123")).roles("USER").build());
        manager.createUser(User.withUsername("admin")
                .password(bcrypt.encode("987")).roles("USER", "ADMIN").build());
        return manager;
    }
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.authorizeRequests().anyRequest().authenticated().and().httpBasic();

        http.authorizeHttpRequests(request -> request
                .requestMatchers("/index").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/portail/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().authenticated()
        );
        http.exceptionHandling(exception -> exception.accessDeniedPage("/403"));
        return http.build();
    }

    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(request -> request
                .requestMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
        );
        http.formLogin(formLogin -> formLogin.loginPage("/login").permitAll());
        http.logout(LogoutConfigurer::permitAll);
        return http.build();
    }*/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**");
    }

}

