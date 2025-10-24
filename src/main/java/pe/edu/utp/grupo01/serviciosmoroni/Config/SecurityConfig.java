package pe.edu.utp.grupo01.serviciosmoroni.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ClienteDetallesServicio;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

        @Autowired
        private ClienteDetallesServicio clienteDetallesServicio;

        @Autowired
        private CustomLoginSuccessHandler loginSuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/", "/inicio", "/galeria", "/empresa", "/servicios",
                                                                "/contacto",
                                                                "/login", "/clientes/register", "/css/**", "/js/**",
                                                                "/img/**",
                                                                "/proyectos/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("emailCliente")
                                                .passwordParameter("contrasenaCliente")
                                                .successHandler(loginSuccessHandler)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/inicio")
                                                .permitAll())
                                .userDetailsService(clienteDetallesServicio);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
