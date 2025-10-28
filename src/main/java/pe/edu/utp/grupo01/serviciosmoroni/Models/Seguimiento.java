package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no debe superar los 300 caracteres")
    @Column(nullable = false, length = 300)
    private String descripcion;

    @NotNull(message = "La fecha de avance es obligatoria")
    @Column(name = "fecha_avance", nullable = false)
    private LocalDate fechaAvance;

    @NotNull(message = "El porcentaje de avance es obligatorio")
    @Column(name = "porcentaje_avance", nullable = false)
    private Integer porcentajeAvance;

    @NotNull(message = "Debe asociar un proyecto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
}
