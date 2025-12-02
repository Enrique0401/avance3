package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Entity // Marca la clase como entidad JPA (tabla en la BD)
@Table(name = "seguimiento") // Nombre de la tabla
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguimiento {

    @Id // Llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    @Column(name = "id_seguimiento")
    private Integer idSeguimiento;

    // Descripción del avance registrado
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no debe superar los 300 caracteres")
    @Column(nullable = false, length = 300)
    private String descripcion;

    // Fecha en la que se realizó el avance del proyecto
    @NotNull(message = "La fecha de avance es obligatoria")
    @Column(name = "fecha_avance", nullable = false)
    private LocalDate fechaAvance;

    // Porcentaje del progreso (ej. 30, 50, 100)
    @NotNull(message = "El porcentaje de avance es obligatorio")
    @Column(name = "porcentaje_avance", nullable = false)
    private Integer porcentajeAvance;

    // Relación con el proyecto del cual pertenece este seguimiento
    @NotNull(message = "Debe asociar un proyecto")
    @ManyToOne(fetch = FetchType.LAZY) // Muchos seguimientos pertenecen a un proyecto
    @JoinColumn(name = "proyecto_id", nullable = false) // Llave foránea
    private Proyecto proyecto;
}
