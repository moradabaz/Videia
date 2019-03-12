package vistas;

import VideoWeb.VideoWeb;
import controlador.Controlador;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import modelo.Etiqueta;
import modelo.Video;
import persistencia.PoolEtiqueta;

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
    public HBox hboxTitulo;
    private UserWindowController userWindowController;
    private Controlador controlador;
    private Video video;
    private static long tiempoEspera = 50000;

    public void inicializar() {
        this.userWindowController = UserWindowController.getInstancia();
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        videoWeb = VideoWeb.getUnicaInstancia();
        videoWeb.setPanel(visorBox);
        controlador = controlador.getInstanciaUnica();
        hboxTitulo.getChildren().clear();

       // playVideo(this.video);
    }

    public void playVideo(Video video) {
        videoWeb.playVideo(video.getRutaFichero());
        controlador.play(video);
        numReproduccionesLabel.setText(String.valueOf(video.getNumReproducciones()));

    }


    public void startVideo(Video video) {
        hboxTitulo.getChildren().clear();
        Text textoTitulo = new Text(video.getTitulo());
        hboxTitulo.getChildren().add(textoTitulo);
        this.video = video;
        ImageView imgView = videoWeb.getImage(video.getRutaFichero());
        visorBox.getChildren().add(imgView);
        imgView.setStyle("-fx-cursor: hand");
        imgView.setOnMouseClicked(MouseEvent -> {
            visorBox.getChildren().remove(imgView);
            playVideo(video);
        });
    }

    public void playVideo(LinkedList<Video> lista) {
        Timeline fiveSecondsWonder = null;

        botonSiguiente.setVisible(true);
        Iterator<Video> it = lista.iterator();
        if (it.hasNext()) {
            Video video1 = it.next();
            playVideo(video1);
            fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(userWindowController.getIntervalo()), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    videoWeb.cancel();
                    if (it.hasNext())
                        playVideo(it.next());
                }
            }));
            fiveSecondsWonder.setCycleCount(lista.size());
            fiveSecondsWonder.play();
        }
        botonSiguiente.setVisible(false);
    }

    private void gotoInicio() {
        videoWeb.cancel();
        controlador.setPlaying(false);
        this.userWindowController.restoreImages();
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
