package Vistas.sample;

import VideoWeb.VideoWeb;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import modelo.dominio.Video;

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
