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
import modelo.dominio.persistencia.PoolEtiqueta;


public class VisorController {

    public VideoWeb videoWeb;
    public HBox hBoxButtons;
    public VBox visorBox;
    public VBox mainBox;
    public Label numReproduccionesLabel;
    public Button botonEtiqueta;
    private UserWindowController userWindowController;
    private Controlador controlador;
    private Video video;

    public void inicializar(UserWindowController userWindowController, Video video) {
        this.video = video;
        this.userWindowController = userWindowController;
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        videoWeb = VideoWeb.getUnicaInstancia();
        videoWeb.setPanel(visorBox);
        videoWeb.playVideo(video.getRutaFichero());
        controlador = controlador.getInstanciaUnica();
        controlador.play(video);
        numReproduccionesLabel.setText(String.valueOf(video.getNumReproducciones()));
    }

    private void gotoInicio() {
        videoWeb.cancel();
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
}
