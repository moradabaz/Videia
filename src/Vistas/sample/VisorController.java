package Vistas.sample;

import VideoWeb.VideoWeb;
import controlador.Controlador;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import modelo.dominio.Video;


public class VisorController {

    public VideoWeb videoWeb;
    public HBox hBoxButtons;
    public VBox visorBox;
    public VBox mainBox;
    public Label numReproduccionesLabel;
    private UserWindowController userWindowController;
    private Controlador controlador;
    public void inicializar(UserWindowController userWindowController, Video video) {
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
}
