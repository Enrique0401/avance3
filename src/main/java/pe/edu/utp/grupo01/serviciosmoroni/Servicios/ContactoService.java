package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ContactoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service // Indica que esta clase es un servicio de la capa lógica de la aplicación
public class ContactoService {

    @Autowired // Inyección automática del repositorio para trabajar con la BD
    private ContactoRepository contactoRepository;

    /**
     * Guarda un mensaje de contacto enviado desde el formulario.
     * Antes de almacenar, se asigna la fecha actual.
     *
     * @param contacto objeto Contacto enviado desde el formulario
     * @return contacto guardado en la BD
     */
    public Contacto guardar(Contacto contacto) {
        contacto.setFechaEnvio(LocalDateTime.now()); // Guarda la fecha del momento en que se envía
        return contactoRepository.save(contacto); // Inserta el registro en la BD
    }

    /**
     * Retorna la lista completa de todos los contactos almacenados.
     *
     * @return lista de contactos
     */
    public List<Contacto> listarTodos() {
        return contactoRepository.findAll(); // Recupera todos los registros de la tabla
    }
}
