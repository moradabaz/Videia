package pruebas;

import VideoWeb.VideoWeb;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.*;

public class TestImagen {


    public static final int ANCHURA = 120;
    public static final int ALTURA = 90;

    private static final String subruta = "thumbs/";

    private HashMap<String, String> mapaImagenes;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;


    VideoWeb videoWeb = VideoWeb.getUnicaInstancia();

    String ruta1 = videoWeb.getImageUrl("https://www.youtube.com/watch?v=4uiaZhgren4");
    String ruta2 = videoWeb.getImageUrl("https://www.youtube.com/watch?v=qu577tNp1hA");
    String ruta3 = videoWeb.getImageUrl("https://www.youtube.com/watch?v=R5NyB_bAZI8");
    String ruta4 = videoWeb.getImageUrl("https://www.youtube.com/watch?v=qTktSFh1UJA");
    String ruta5 = videoWeb.getImageUrl("https://www.youtube.com/watch?v=G1IbRujko-A");

    Path path;

    @Before
    public void initialize() {
        img1 = videoWeb.getThumb(ruta1);
        img2 = videoWeb.getThumb(ruta2);
        img3 = videoWeb.getThumb(ruta3);
        img4 = videoWeb.getThumb(ruta4);
        img5 = videoWeb.getThumb(ruta5);
        path = Paths.get("../thumbs/");
        mapaImagenes = new HashMap<>();

        String rutaImg1 = subruta + videoWeb.getUrlID("https://www.youtube.com/watch?v=4uiaZhgren4") + ".jpg";
        String rutaImg2 = subruta + videoWeb.getUrlID("https://www.youtube.com/watch?v=qu577tNp1hA") + ".jpg";
        String rutaImg3 = subruta + videoWeb.getUrlID("https://www.youtube.com/watch?v=R5NyB_bAZI8") + ".jpg";
        String rutaImg4 = subruta + videoWeb.getUrlID("https://www.youtube.com/watch?v=qTktSFh1UJA") + ".jpg";
        System.out.println(rutaImg1);
        mapaImagenes.put(ruta1, rutaImg1);
        mapaImagenes.put(ruta2, rutaImg2);
        mapaImagenes.put(ruta3, rutaImg3);
        mapaImagenes.put(ruta4, rutaImg4);

    }


    @Test
    public void almacenarImagen2() {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(ruta2).openStream());
             FileOutputStream fileOS = new FileOutputStream("/thumbs/imagen2.jpg")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }

    @Test
    public  void almacenarImagen3() {
        URL url = null;
        try {
            url = new URL(ruta2);
            InputStream in = new BufferedInputStream(url.openStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream("thumbs/imagen3.jpg"));

            for ( int i; (i = in.read()) != -1; ) {
                out.write(i);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Fail");
        }

    }

    @Test
    public void createImage() {
        JFXPanel jfxPanel = new JFXPanel();
        Path pathQuit = Paths.get("thumbs/imagen3.jpg");
        Image image = new Image("file:" + pathQuit.toAbsolutePath().toString());
        assertNotNull(image);
    }

    @Test
    public  void createImages() {
        for (String str : mapaImagenes.keySet()) {
            String rutaImg = mapaImagenes.get(str);
            URL url = null;
            try {
                url = new URL(str);
                InputStream in = new BufferedInputStream(url.openStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream(rutaImg));

                for ( int i; (i = in.read()) != -1; ) {
                    out.write(i);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                System.err.println("Fail");
            }
        }
    }

    @Test
    public void uploadImgs() {
        JFXPanel jfxPanel = new JFXPanel();
        for (String str : mapaImagenes.keySet()) {
            String rutaImg = mapaImagenes.get(str);
            Path pathQuit = Paths.get(rutaImg);
            Image image = new Image("file:" + pathQuit.toAbsolutePath().toString());
            assertNotNull(image);
            System.out.println(image.toString());
        }
    }

}
