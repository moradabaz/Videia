package modelo.dominio;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UsuarioTest {


    Usuario usuario;

    @Before
    public void setUp() throws Exception {
        this.usuario = new Usuario("moradisten", "wwww", "Morad", "Abbou", new Date(), "email");
        String nombre = usuario.getUsername();
        assertEquals("This", "moradisten", nombre);
    }

    @Test
    public void ajaosdf() throws Exception {

    }
}