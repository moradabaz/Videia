package modelo.dominio.persistencia;

import modelo.dominio.Usuario;
import org.junit.*;

import java.util.Date;

import static org.junit.Assert.*;

public class AdaptadorUsuarioDAOTest {

    int codigo = 0;
    AdaptadorUsuarioDAO adaptadorUsuarioDAO;

    @Before
    public void setUp() throws Exception {
        adaptadorUsuarioDAO = AdaptadorUsuarioDAO.getUnicaInstancia();
    }

    @Test
    public void anadirUsuario() throws Exception {
        Usuario usuario = new Usuario("moradisten", "wwww", "Morad", "Abbou", new Date(), "email");
        adaptadorUsuarioDAO.registrarUsuario(usuario);
        codigo = usuario.getCodigo();
    }

    @Test
    public void recuperarUsuario() throws Exception {
        Usuario usuario = adaptadorUsuarioDAO.recuperarUsuario(codigo);
        if (usuario != null) {
            System.out.println(usuario.getUsername());
        }
        assertEquals("this", "moradisten", usuario.getUsername());
    }
}