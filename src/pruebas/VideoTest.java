package pruebas;

import static org.junit.Assert.*;
import VideoWeb.VideoWeb;
import modelo.Etiqueta;
import modelo.Video;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;


public class VideoTest {

    Video video1;
    Video video2;
    Video video3;
    Video video4;
    Video video5;
    Video video6;
    Video video7;

    String ruta1 = "https://www.youtube.com/watch?v=gd1c885zgO4";
    String ruta2 = "https://www.youtube.com/watch?v=WM28K5do8_k";
    String ruta3 = "https://www.youtube.com/watch?v=iY4TPWa2vEc";
    String ruta4 = "https://www.youtube.com/watch?v=n18g4bRJDCY";
    String ruta5 = "https://www.youtube.com/watch?v=SBKMVUnEYVc";
    String ruta6 = "https://www.youtube.com/watch?v=i3mlkSl3fM4";
    String ruta7 = "https://www.youtube.com/watch?v=QCyIY10KBnk";

    VideoWeb videoWeb = VideoWeb.getUnicaInstancia();

    @Before
    public void initialize() {
        video1 = new Video("Video 1", ruta1);
        video2 = new Video("Video 2", ruta2);
        video3 = new Video("Video 3", ruta3);
        video4 = new Video("Video 4", ruta4);
        video5 = new Video("Video 5", ruta5);
        video6 = new Video("Video 6", ruta6);
        video7 = new Video("Video 7", ruta7);
    }


    @Test
    public void testTitulos() {
        assertEquals("Video 2", video2.getTitulo());
        assertEquals("Video 3", video3.getTitulo());
        assertEquals("Video 4", video4.getTitulo());
    }

    @Test
    public void testGetRuta() {
        assertEquals("Prueba de ruta de fichero - video1 ", ruta1, video1.getRutaFichero());
        assertEquals("Prueba de ruta de fichero - video5 ", ruta5, video5.getRutaFichero());
        assertEquals("Prueba de ruta de fichero - video7 ", ruta7, video7.getRutaFichero());
    }

    @Test
    public void testGetEtiquetas() {
        Etiqueta etiqueta1 = new Etiqueta("Musica");
        video1.addEtiqueta(etiqueta1);
        LinkedList<Etiqueta> lista = new LinkedList<>(video1.getEtiquetas());
        String etiqueta = lista.get(0).getEtiqueta();
        assertEquals(etiqueta, etiqueta1.getEtiqueta());
    }

    @Test
    public void testReproducciones() {
        video1.setNumReproducciones(4);
        video7.incrementarReproducciones();
        assertEquals(4, video1.getReproducciones());
        assertEquals(1, video7.getReproducciones());
    }

}