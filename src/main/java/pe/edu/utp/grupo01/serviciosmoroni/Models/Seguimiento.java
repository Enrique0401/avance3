package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "seguimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguimiento")
    private Integer idSeguimiento;

    @Column(nullable = false, length = 300)
    private String descripcion;

    @Column(name = "fecha_avance")
    private LocalDate fechaAvance;

    @Column(name = "porcentaje_avance", nullable = false)
    private Integer porcentajeAvance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
}
