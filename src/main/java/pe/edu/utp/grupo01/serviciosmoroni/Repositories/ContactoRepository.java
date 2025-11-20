package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
}
