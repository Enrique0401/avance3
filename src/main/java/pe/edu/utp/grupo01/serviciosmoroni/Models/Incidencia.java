package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "incidencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIncidencia;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no debe superar los 300 caracteres")
    @Column(nullable = false, length = 300)
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no debe superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String estado;

    @NotNull(message = "Debe asociar un proyecto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
}
