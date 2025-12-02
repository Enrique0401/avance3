package pe.edu.utp.grupo01.serviciosmoroni.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity // Indica que esta clase es una entidad mapeada a una tabla en la BD
@Table(name = "cliente") // Nombre de la tabla en la BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremento
    @Column(name = "id_cliente")
    private Integer idCliente;

    @NotBlank(message = "El nombre es obligatorio") // Validación: no puede estar vacío
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    // Tipo de documento (solo DNI o RUC)
    @NotBlank(message = "Debe seleccionar un tipo de documento")
    @Pattern(regexp = "DNI|RUC", message = "Tipo de documento inválido")
    @Column(name = "tipo_documento", nullable = false, length = 10)
    private String tipoDocumento;

    // Documento del cliente: acepta DNI (8 dígitos) o RUC (11 dígitos)
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

    // Rol del usuario (usuario por defecto)
    @Column(name = "rol", nullable = false, length = 20)
    private String rol = "ROLE_USER";

    // Campo temporal para confirmar la contraseña, no se guarda en la BD
    @Transient
    private String confirmPassword;

    // Fecha en la que se registró el cliente
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // Se ejecuta antes de insertar un nuevo registro
    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now(); // Establece la fecha actual
    }

    // Relación uno a muchos: un cliente puede tener varios proyectos
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proyecto> proyectos = new ArrayList<>();
}
