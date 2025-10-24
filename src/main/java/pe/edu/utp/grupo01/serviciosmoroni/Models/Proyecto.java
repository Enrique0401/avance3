package pe.edu.utp.grupo01.serviciosmoroni.Models;

import jakarta.persistence.*;
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

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String estado = "Pendiente"; // Valor por defecto

    @Column(nullable = false)
    @Builder.Default
    private Integer progreso = 0; // Valor por defecto

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    // Relación con cliente (Lazy evita sobrecarga al listar proyectos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Campo temporal (no se guarda en BD)
    @Transient
    private MultipartFile[] archivos;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seguimiento> seguimientos = new ArrayList<>();
}
