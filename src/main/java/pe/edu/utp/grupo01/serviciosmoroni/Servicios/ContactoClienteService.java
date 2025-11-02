package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ContactoClienteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactoClienteService {

    @Autowired
    private ContactoClienteRepository contactoClienteRepository;

    /**
     * ✅ Guarda un contacto en la base de datos con la fecha actual.
     */
    @Transactional
    public Contacto guardar(Contacto contactoCliente) {
        contactoCliente.setFechaEnvio(LocalDateTime.now());
        return contactoClienteRepository.save(contactoCliente);
    }

    /**
     * ✅ Lista todos los contactos registrados.
     */
    @Transactional(readOnly = true)
    public List<Contacto> listarTodos() {
        return contactoClienteRepository.findAll();
    }
}
