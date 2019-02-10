package pruebas;

import modelo.Filtro;
import modelo.Usuario;
import modelo.Video;
import modelo.VideoList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsuarioTest {


    // int codigo, String username, String password, String nombre, String apellidos, String fechaNac, String email

    Usuario usuario;
    Video video1, video2;
    VideoList videoList;
    String ruta1 = "https://www.youtube.com/watch?v=gd1c885zgO4";
    String ruta2 = "https://www.youtube.com/watch?v=WM28K5do8_k";


    @Before
    public void initialize() {
        usuario = new Usuario(1,"moradisten", "wwww", "Morad", "Abbou", "1996-04-04", "moradgta@gmail.com");
        videoList = new VideoList("Mi lista");
        usuario.addVideoList(videoList);
        video1 = new Video("Video 1", ruta1);
        video2 = new Video("Video 2", ruta2);

    }

    @Test
    public void testAddCancionAListaInexistente()  {
        VideoList vdList = null;
        usuario.anadirVideoALista(video1, vdList);
        assertNull(vdList);
    }


    @Test
    public void testAnadirVideoALista() {
        usuario.anadirVideoALista(video1, videoList);
        assertEquals(1, videoList.getVideos().size());
        Video vd = videoList.getVideos().get(0);
        assertEquals(video1, vd);
    }

    @Test
    public void testAnadirVideoAReciente() {
        usuario.anadirVideoReciente(video1);
        Video vd = usuario.getVideosRecientes().get(0);
        assertEquals(video1, vd);
        assertEquals(1, usuario.getVideosRecientes().size());
    }

    @Test
    public void testContieneLista() {
        assertEquals(true, usuario.contieneVideoList("Mi lista"));
    }

    @Test
    public void tertNoContieneLista() {
        assertNotEquals(true, usuario.contieneVideoList("lista"));
    }

    @Test
    public void testTiene23anyos() {
        int edad = usuario.getEdad();
        assertEquals(23, edad);
    }

    @Test
    public void testEsMayorEdad() {
        boolean mayorEdad = usuario.getEdad() >= 18;
        assertTrue("Es mayor de edad", mayorEdad);
    }

    @Test
    public void testFiltro() {
        Filtro filtro = usuario.getFiltro();
        assertEquals("No Filtro", filtro.getNombre());
    }

    @Test
    public void cambiarFiltroImpopularesSinPremium() {
        usuario.setFiltro("Impopulares");
        assertEquals("No Filtro", usuario.getFiltroNombre());
    }

    @Test
    public void cambiarFiltroConPremium() {
        usuario.setPremium(true);
        usuario.setFiltro("Impopulares");
        assertEquals("Impopulares", usuario.getFiltroNombre());
    }

}