package Vistas.sample;

import VideoWeb.VideoWeb;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class ThumbGridController {

    public static final int MAX_COLUMN = 4;
    public VideoWeb videoWeb;
    public ColumnConstraints col1;
    public ColumnConstraints col2;
    public ColumnConstraints col3;
    public ColumnConstraints col4;
    public RowConstraints row1;
    public RowConstraints row2;
    public RowConstraints row3;
    public RowConstraints row4;
    public GridPane gridPane;
    public BorderPane borderPane;

    public void inicializar(BorderPane borderPane) {
        this.borderPane = borderPane;
        videoWeb = VideoWeb.getUnicaInstancia();
        int n = gridPane.getColumnConstraints().size();
        System.out.println(n);
    }
    
    public void anadirMiniatura(ImageView imageView) {

    }


    public void insertImages(LinkedList<String> listaUrls) {
        int tamLista = listaUrls.size();
        System.out.println(tamLista);
        int contadorImagenes = 0;
        int filas = 0;

        while (contadorImagenes < tamLista) {

            int posColumna = contadorImagenes % MAX_COLUMN;

            System.out.println("Fila: " + filas + " - Columna: " + posColumna);

            VBox box = new VBox();
            String url = listaUrls.getFirst();
            //System.out.println(url);
            ImageView img = videoWeb.getThumb(url);
            if (img != null) {
                box.getChildren().add(img);
                gridPane.add(box, posColumna, filas);
                img.setStyle("-fx-cursor: hand");
                img.setOnMouseClicked(MouseEvent -> {
                    try {
                        visualizar(url);
                    } catch (IOException e) {

                    }
                });

                listaUrls.removeFirst();
            }

            contadorImagenes++;
            if (contadorImagenes % 4 == 0) {
                filas++;
            }

        }
    }

    private void visualizar(String url) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VisorWindow.fxml"));
        VBox visor = loader.load();
        VisorController visorController = loader.getController();
        borderPane.setCenter(visor);
        visorController.inicializar(url);

    }

}
