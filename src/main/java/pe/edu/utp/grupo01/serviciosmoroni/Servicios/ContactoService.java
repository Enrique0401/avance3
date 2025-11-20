package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ContactoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public Contacto guardar(Contacto contacto) {
        contacto.setFechaEnvio(LocalDateTime.now());
        return contactoRepository.save(contacto);
    }

    public List<Contacto> listarTodos() {
        return contactoRepository.findAll();
    }
}
