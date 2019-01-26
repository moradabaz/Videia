package Vistas.sample;

import VideoWeb.VideoWeb;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;


public class VisorController {

    public VideoWeb videoWeb;
    public HBox hBoxButtons;
    public VBox visorBox;
    public VBox mainBox;
    private UserWindowController userWindowController;

    public void inicializar(UserWindowController userWindowController, String url) {
        this.userWindowController = userWindowController;
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        anadirBotones();
        videoWeb = VideoWeb.getUnicaInstancia();
        videoWeb.setPanel(visorBox);
        videoWeb.playVideo(url);


    }

    private void anadirBotones() {
        Button botonVolver = new Button("volver");

        botonVolver.setOnMouseClicked(MouseEvent -> {
            gotoInicio();
        });
        hBoxButtons.getChildren().add(botonVolver);
    }

    private void gotoInicio() {
        videoWeb.cancel();
        this.userWindowController.setGridPaneToCenter();
        //userWindowController.setCenter();
        //      borderPane.setCenter();

    }


}
