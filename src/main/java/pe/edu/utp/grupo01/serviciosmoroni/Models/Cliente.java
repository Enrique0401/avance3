package pe.edu.utp.grupo01.serviciosmoroni.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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

    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name = "ruc_cliente", nullable = false, unique = true, length = 11)
    private String rucCliente;

    @Column(name = "direccion_cliente", length = 150)
    private String direccionCliente;

    @Column(name = "telefono_cliente", unique = true, length = 9)
    private String telefonoCliente;

    @Column(name = "email_cliente", unique = true, length = 100)
    private String emailCliente;

    @Column(name = "contrasena_cliente", nullable = false, length = 255)
    private String contrasenaCliente;

    @Transient
    private String confirmPassword;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // Inicializamos la lista para evitar NullPointerException
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proyecto> proyectos = new ArrayList<>();
}
