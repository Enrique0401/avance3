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

        // Servicio que carga los datos del cliente (usuario)
        private final ClienteDetallesServicio clienteDetallesServicio;

        // Manejador para cuando el login es exitoso
        private final CustomLoginSuccessHandler loginSuccessHandler;

        public SecurityConfig(ClienteDetallesServicio clienteDetallesServicio,
                        CustomLoginSuccessHandler loginSuccessHandler) {
                this.clienteDetallesServicio = clienteDetallesServicio;
                this.loginSuccessHandler = loginSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // Configura qué rutas son públicas y cuáles necesitan login
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/inicio", "/galeria", "/empresa", "/servicios",
                                                                "/contacto", "/contacto/enviar", "/contactoCliente",
                                                                "/contactoCliente/enviar", "/login",
                                                                "/clientes/register",
                                                                "/clientes/register/**", "/css/**", "/js/**", "/img/**",
                                                                "/proyectos/**")
                                                .permitAll() // Estas rutas son libres
                                                .requestMatchers("/admin/**").hasRole("ADMIN") // Solo admin
                                                .requestMatchers("/supervisor/**").hasRole("VISOR") // Solo el
                                                                                                    // supervisor
                                                .requestMatchers("/clientes/**").hasAnyRole("USER", "ADMIN", "VISOR")
                                                // Usuarios ,admin o supervisor
                                                .anyRequest().authenticated() // Todo lo demás necesita login
                                )
                                // Configura el formulario de login
                                .formLogin(form -> form
                                                .loginPage("/login") // Página personalizada de login
                                                .usernameParameter("emailCliente") // Campo de usuario
                                                .passwordParameter("contrasenaCliente") // Campo de contraseña
                                                .successHandler(loginSuccessHandler) // Qué pasa si el login es correcto
                                                .permitAll())
                                // Configura el logout (cerrar sesión)
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/inicio") // A dónde va después de salir
                                                .permitAll())
                                // Desactiva CSRF solo en rutas específicas (para evitar errores en formularios)
                                .csrf(csrf -> csrf.ignoringRequestMatchers(
                                                "/contacto/enviar",
                                                "/contactoCliente/enviar",
                                                "/clientes/register"));

                return http.build();
        }

        // Bean para manejar la autenticación
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        // Bean para encriptar contraseñas
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
