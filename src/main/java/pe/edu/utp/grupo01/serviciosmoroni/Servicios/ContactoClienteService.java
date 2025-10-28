package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ContactoClienteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactoClienteService {

    @Autowired
    private ContactoClienteRepository contactoClienteRepository;

    public Contacto guardar(Contacto contactoCliente) {
        contactoCliente.setFechaEnvio(LocalDateTime.now());
        return contactoClienteRepository.save(contactoCliente);
    }

    public List<Contacto> listarTodos() {
        return contactoClienteRepository.findAll();
    }
}
