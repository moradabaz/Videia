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
    public BorderPane borderPane;

    public void inicializar(String url) {

        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        anadirBotones();
        borderPane = (BorderPane) mainBox.getParent();
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
        FlowPane flowPane = new FlowPane();
        borderPane.setCenter(flowPane);
    }


}
