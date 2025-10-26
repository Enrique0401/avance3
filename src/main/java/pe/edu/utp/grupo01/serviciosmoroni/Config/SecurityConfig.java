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
                                // 🔓 Configuración de acceso por URL
                                .authorizeHttpRequests(auth -> auth
                                                // Rutas públicas accesibles para todos
                                                .requestMatchers(
                                                                "/", "/inicio", "/galeria", "/empresa", "/servicios",
                                                                "/contacto", "/contacto/enviar", "/login",
                                                                "/clientes/register",
                                                                "/css/**", "/js/**", "/img/**", "/proyectos/**")
                                                .permitAll()

                                                // 🔒 Rutas exclusivas para ADMIN
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                // 🔐 Rutas exclusivas para CLIENTE (USER) y ADMIN
                                                .requestMatchers("/clientes/**").hasAnyRole("USER", "ADMIN")

                                                // ⚙️ Cualquier otra ruta requiere autenticación
                                                .anyRequest().authenticated())

                                // 🔑 Configuración del formulario de login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("emailCliente") // campo del email en tu formulario
                                                .passwordParameter("contrasenaCliente") // campo de contraseña
                                                .successHandler(loginSuccessHandler) // redirige según rol
                                                .permitAll())

                                // 🚪 Configuración del logout
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/inicio")
                                                .permitAll())

                                // 👤 Servicio de detalles de usuario personalizado
                                .userDetailsService(clienteDetallesServicio)

                                // 🧾 Deshabilita CSRF solo para la ruta de envío de contacto
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/contacto/enviar"));

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
