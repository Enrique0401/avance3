package pe.edu.utp.grupo01.serviciosmoroni.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    // 🔹 Tipo de documento (DNI o RUC )
    @NotBlank(message = "Debe seleccionar un tipo de documento")
    @Pattern(regexp = "DNI|RUC", message = "Tipo de documento inválido")
    @Column(name = "tipo_documento", nullable = false, length = 10)
    private String tipoDocumento;

    // 🔹 Documento: puede ser DNI (8) o RUC (11)
    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "\\d{8}|\\d{11}", message = "El documento debe tener 8 dígitos (DNI) o 11 dígitos (RUC)")
    @Column(name = "num_documento", nullable = false, unique = true, length = 11)
    private String numDocumento;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150, message = "La dirección no debe superar los 150 caracteres")
    @Column(name = "direccion_cliente", nullable = false, length = 150)
    private String direccionCliente;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    @Column(name = "telefono_cliente", nullable = false, unique = true, length = 9)
    private String telefonoCliente;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo electrónico válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Column(name = "email_cliente", nullable = false, unique = true, length = 100)
    private String emailCliente;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(name = "contrasena_cliente", nullable = false, length = 255)
    private String contrasenaCliente;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol = "ROLE_USER";

    @Transient
    private String confirmPassword;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proyecto> proyectos = new ArrayList<>();
}
