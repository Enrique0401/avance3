package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    private Integer idContacto;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 100, message = "El nombre de la empresa no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String empresa;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo electrónico válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    @Column(nullable = false, length = 9)
    private String telefono;

    @NotBlank(message = "Debe seleccionar un servicio")
    @Size(max = 50, message = "El nombre del servicio no debe superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String servicio;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no debe superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDateTime.now();
    }
}
