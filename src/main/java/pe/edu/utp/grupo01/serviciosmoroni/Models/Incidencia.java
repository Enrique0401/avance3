package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity // Indica que esta clase es una tabla en la base de datos
@Table(name = "incidencia") // Nombre de la tabla
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incidencia {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Integer idIncidencia;

    // Descripción de la incidencia reportada
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no debe superar los 300 caracteres")
    @Column(nullable = false, length = 300)
    private String descripcion;

    // Fecha en la que se registró la incidencia
    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    // Estado de la incidencia (Ej: Pendiente, En proceso, Resuelto)
    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no debe superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String estado;

    // Relación con el proyecto al que pertenece la incidencia
    @NotNull(message = "Debe asociar un proyecto")
    @ManyToOne(fetch = FetchType.LAZY) // Muchas incidencias pueden pertenecer a un mismo proyecto
    @JoinColumn(name = "proyecto_id", nullable = false) // Nombre de la columna FK en la BD
    private Proyecto proyecto;
}
