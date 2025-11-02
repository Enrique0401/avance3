package pe.edu.utp.grupo01.serviciosmoroni.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ClienteDetallesServicio;

@Configuration
public class SecurityConfig {

        private final ClienteDetallesServicio clienteDetallesServicio;
        private final CustomLoginSuccessHandler loginSuccessHandler;

        public SecurityConfig(ClienteDetallesServicio clienteDetallesServicio,
                        CustomLoginSuccessHandler loginSuccessHandler) {
                this.clienteDetallesServicio = clienteDetallesServicio;
                this.loginSuccessHandler = loginSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/inicio", "/galeria", "/empresa", "/servicios",
                                                                "/contacto", "/contacto/enviar", "/contactoCliente",
                                                                "/contactoCliente/enviar",
                                                                "/login", "/clientes/register", "/clientes/register/**",
                                                                "/css/**", "/js/**", "/img/**", "/proyectos/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/clientes/**").hasAnyRole("USER", "ADMIN")
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
                                .csrf(csrf -> csrf.ignoringRequestMatchers(
                                                "/contacto/enviar",
                                                "/contactoCliente/enviar",
                                                "/clientes/register"));

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
