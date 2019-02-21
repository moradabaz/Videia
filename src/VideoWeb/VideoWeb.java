package VideoWeb;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class VideoWeb {


    private static final String cabeceraURLYoutube = "https://www.youtube.com/watch?v=";
    private static VideoWeb unicaInstancia;
    private String reproducir;
    private Pane panel;
    private static boolean activo;
    private static WebView webView;
    private boolean iniciadoVideo;

    public static VideoWeb getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new VideoWeb();
        }
        return unicaInstancia;
    }

    private VideoWeb() {
        this.iniciadoVideo = false;
        this.panel = new VBox();
        this.activo = false;
    }

    public void setPanel(Pane panel) {
        webView = new WebView();
        panel.getChildren().add(webView);
    }


    public void playVideo(String url) {

        if (!url.startsWith(cabeceraURLYoutube)) {
            System.out.println("Error de url de youtube: debe empezar por < https://www.youtube.com/watch?v= >");
            System.exit(1);
        }
        if (url.length() != 43) {
            System.out.println("Error: La URL debe ser de 43 caracteres");
            System.exit(1);
        }

        if (!activo) {

            String lineAReemplazar = "watch?v=";
            String lineaParaReemplazar = "embed/";
            String urlEmbeded = url.replace(lineAReemplazar, lineaParaReemplazar);

            this.reproducir = "<iframe" +
                    " frameborder=\"0\"" +
                    " scrolling=\"no\" marginheight=\"0\"" +
                    " marginwidth=\"0\"" +
                    " width=\"456\"" +
                    " height=\"300\"" +
                    " type=\"text/html\"" +
                    " src=\"" + urlEmbeded + "?autoplay=1&amp;fs=0&amp;iv_load_policy=3&amp;showinfo=0&amp;rel=0&amp;cc_load_policy=0&amp;start=0&amp;end=0&amp;origin=undefined\">" +
                    "</iframe>";

            webView.getEngine().loadContent(reproducir);
            activo = true;
        }
    }


    public void cancel() {
        final String negro = "<html><body bgcolor=\"white\"></body></html>";
        this.activo = false;
        webView.getEngine().load(null);
    }

    public String getVersion()  {
        return "Version 1.0 Gamma - Author: Morad Abbou Azaz - (c) 2018 TDS";
    }

    public ImageView getThumb(String urlYoutube) {
        URL urlThumb = null;
        Image imgThumb = null;
        ImageView imgView = new ImageView();
        if (urlYoutube.startsWith(cabeceraURLYoutube) && urlYoutube.length() == 43) {
            String idVideo = urlYoutube.substring(urlYoutube.indexOf(61)+ 1);
            try {
                String url = "http://img.youtube.com/vi/" + idVideo + "/1.jpg";
                imgThumb = new Image(url);
            }catch (Exception e) {
                ;
            }
        }
        imgView.setImage(imgThumb);
        return imgView;
    }

    public ImageView getImage(String urlYoutube) {
        Image imgThumb = null;
        ImageView imgView = new ImageView();
        if (urlYoutube.startsWith(cabeceraURLYoutube) && urlYoutube.length() == 43) {
            String idVideo = urlYoutube.substring(urlYoutube.indexOf(61)+ 1);
            try {
                String url = "http://img.youtube.com/vi/" + idVideo + "/hqdefault.jpg";
                imgThumb = new Image(url);
            }catch (Exception e) {
                ;
            }
        }
        imgView.setImage(imgThumb);
        return imgView;
    }


    public List<ImageView> getUrlImages(List<String> listaUrls) {
        List<ImageView> lista = new LinkedList<ImageView>();
        for (String url : listaUrls) {
            ImageView img = getThumb(url);
            img.setStyle("-fx-cursor: hand");
            lista.add(img);
        }
        return lista;
    }




}
