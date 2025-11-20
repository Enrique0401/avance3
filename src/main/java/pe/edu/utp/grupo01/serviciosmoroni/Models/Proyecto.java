package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Entity
@Table(name = "proyecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(max = 150, message = "El nombre no debe superar los 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no debe superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 100, message = "La categoría no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String categoria;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no debe superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    @Builder.Default
    private String estado = "Pendiente";

    @NotNull(message = "El progreso es obligatorio")
    @Column(nullable = false)
    @Builder.Default
    private Integer progreso = 0;

    @NotNull(message = "La fecha de entrega es obligatoria")
    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @NotNull(message = "Debe asociar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Transient
    private MultipartFile[] archivos;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seguimiento> seguimientos = new ArrayList<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Incidencia> incidencias = new ArrayList<>();
}
