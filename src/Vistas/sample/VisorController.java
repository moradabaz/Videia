package Vistas.sample;

import VideoWeb.VideoWeb;
import controlador.Controlador;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import modelo.dominio.Etiqueta;
import modelo.dominio.Video;
import modelo.dominio.VideoList;
import modelo.dominio.persistencia.PoolEtiqueta;

import java.util.Iterator;
import java.util.LinkedList;


public class VisorController {

    public VideoWeb videoWeb;
    public HBox hBoxButtons;
    public VBox visorBox;
    public VBox mainBox;
    public Label numReproduccionesLabel;
    public Button botonEtiqueta;
    public Button botonSiguiente;
    private UserWindowController userWindowController;
    private Controlador controlador;
    private Video video;
    private static long tiempoEspera = 50000;

    public void inicializar(UserWindowController userWindowController) {
       // this.video = video;
        this.userWindowController = userWindowController;
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        videoWeb = VideoWeb.getUnicaInstancia();
        videoWeb.setPanel(visorBox);
        controlador = controlador.getInstanciaUnica();
       // playVideo(this.video);
    }

    public void playVideo(Video video) {
        this.video = video;
        videoWeb.playVideo(video.getRutaFichero());
        controlador.play(video);
        numReproduccionesLabel.setText(String.valueOf(video.getNumReproducciones()));
    }

    public void playVideoList(Video video) {
        this.video = video;
        videoWeb.playVideoOnBackGround(video.getRutaFichero());
        controlador.play(video);
        numReproduccionesLabel.setText(String.valueOf(video.getNumReproducciones()));
    }

    public void playVideo(LinkedList<Video> lista) {
        botonSiguiente.setVisible(true);
        Iterator<Video> it = lista.iterator();
     /*   botonSiguiente.setOnMouseClicked(event -> {
            Video video2 = null;
            while (it.hasNext()) {
                video2 = it.next();
                videoWeb.cancel();
                long ahora = System.currentTimeMillis();
                playVideo(video2);
                while ((System.currentTimeMillis() - ahora) < tiempoEspera) {}
                videoWeb.cancel();
            }
        });*/
        while (it.hasNext()) {
            Video video1 = it.next();
            long ahora = System.currentTimeMillis();
            playVideoList(video1);
          //  while ((System.currentTimeMillis() - ahora) < tiempoEspera) {}
            videoWeb.cancel();
        }
        botonSiguiente.setVisible(false);
    }

    private void gotoInicio() {
        videoWeb.cancel();
        controlador.setPlaying(false);
        this.userWindowController.setGridPaneToCenter();
    }

    public void volverInicio(MouseEvent event) {
        gotoInicio();
    }

    public void anadirEtiqueta(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        for (Etiqueta label : PoolEtiqueta.getUnicaInstancia().getEtiquetas()) {

            MenuItem item = new MenuItem(label.getEtiqueta());
            item.setOnAction(event2 -> {
                video.addEtiqueta(label);
                controlador.addEtiqueta(label);
                controlador.actualizarVideo(video);
            });
            contextMenu.getItems().add(item);
        }
        contextMenu.show(botonEtiqueta, event.getScreenX(), event.getScreenY());
    }

    public static void setTiempoEspera(long tiempo) {
        tiempoEspera = tiempo;
    }

    public void playNextVideo(MouseEvent mouseEvent) {

    }
}
