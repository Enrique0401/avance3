package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity // Indica que esta clase representa una tabla en la base de datos
@Table(name = "contacto") // Nombre de la tabla
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    @Column(name = "id_contacto")
    private Integer idContacto;

    // Nombre de la persona que envía el formulario
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    // Empresa o institución del remitente
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Pattern(regexp = "^(?!\\s*$).+", message = "La empresa o institución no puede estar vacía o contener solo espacios")
    @Size(max = 100, message = "El nombre de la empresa no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String empresa;

    // Correo electrónico del remitente
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo electrónico válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String email;

    // Teléfono del remitente (validación: debe iniciar con 9 y tener 9 dígitos)
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^9\\d{8}$", message = "El teléfono debe iniciar con 9 y tener 9 dígitos numéricos")
    @Column(nullable = false, length = 9)
    private String telefono;

    // Servicio elegido en el formulario de contacto
    @NotBlank(message = "Debe seleccionar un servicio")
    @Size(max = 50, message = "El nombre del servicio no debe superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String servicio;

    // Mensaje que envía el usuario
    @NotBlank(message = "El mensaje es obligatorio")
    @Pattern(regexp = "^(?!\\s*$).+", message = "El mensaje no puede estar vacío o contener solo espacios")
    @Size(max = 500, message = "El mensaje no debe superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String mensaje;

    // Fecha en que se envía el formulario
    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    // Antes de guardar el registro en la BD, asignar fecha automática
    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDateTime.now();
    }
}
