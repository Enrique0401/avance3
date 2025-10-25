package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
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

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String empresa;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 9)
    private String telefono;

    @Column(nullable = false, length = 50)
    private String servicio;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
}
