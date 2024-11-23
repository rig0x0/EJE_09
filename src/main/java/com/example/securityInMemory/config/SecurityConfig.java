package com.example.securityInMemory.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    // Security filter chain
    // Singleton - tener solo una instancia
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        // Configurar los filtros personalizados
        return httpSecurity.httpBasic(Customizer.withDefaults()).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(http->{
            //http.requestMatchers(HttpMethod.GET, "/api/v2/listar").permitAll();
            http.requestMatchers(HttpMethod.GET,"/api/v2/listar").hasAuthority("READ");
            http.requestMatchers(HttpMethod.GET,"/api/v2/actualizar").hasAuthority("UPDATE");
            http.requestMatchers(HttpMethod.GET,"/api/v2/eliminar").hasAuthority("DELETE");
            http.requestMatchers(HttpMethod.GET,"/api/v2/crear").hasAuthority("CREATE");
            http.anyRequest().denyAll();
        }).build();
    }

    //authentication manager - Lo obtenemos de una instancia que ya existe
    public AuthenticationManager authenticationManager() throws Exception{
        return  authenticationConfiguration.getAuthenticationManager();
    }

    // authentication provider - DAO - Va a proporcionar la autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    // Password encoder
    public PasswordEncoder passwordEncoder(){
        //return  new BCryptPasswordEncoder();
        return  NoOpPasswordEncoder.getInstance();
    }

    // UserDetailsService - base de datos o usuarios en memoria
    @Bean
    public UserDetailsService userDetailsService(){
        // Definir usuario en memoria por el momento
        UserDetails usuarioMiguel = User.withUsername("miguel").password("miguel1234").roles("ADMIN").authorities("READ", "CREATE", "UPDATE", "DELETE").build();
        UserDetails usuarioRodrigo = User.withUsername("rodrigo").password("rodrigo1234").roles("USER").authorities("READ", "UPDATE").build();
        UserDetails usuarioInvitado = User.withUsername("guest").password("guest1234").roles("GUEST").authorities("READ").build();

        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(usuarioMiguel);
        userDetailsList.add(usuarioRodrigo);
        userDetailsList.add(usuarioInvitado);
        return new InMemoryUserDetailsManager(userDetailsList);
    }
}
