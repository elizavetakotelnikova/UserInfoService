package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfigurer {
    private final UsersDao usersDao;
    private final RolesDao rolesDao;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("seriousPassword"))
                .authorities("ROLE_ADMIN");
        auth.userDetailsService(new CustomDatabaseUserDetailsService(usersDao));
    }
    @EventListener(ContextRefreshedEvent.class)
    public void configureCustomRoles() {
        if (rolesDao.findRoleByAuthority("ROLE_USER") == null) rolesDao.save(new Role("ROLE_USER"));
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(customizer ->
                        customizer.requestMatchers(HttpMethod.POST,"/user").permitAll()
                                .requestMatchers("/user/login").permitAll()
                        .anyRequest().authenticated())
                //owner - permitAll()?
                .httpBasic(httpSecurityConfigurer-> httpSecurityConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(401)))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                /*.formLogin(form -> form
                .loginPage("/login")
                .permitAll())*/
        .build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomDatabaseUserDetailsService(usersDao);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
