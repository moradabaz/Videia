package Vistas.sample;

import VideoWeb.VideoWeb;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import controlador.Controlador;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import modelo.dominio.Video;


import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class ThumbGridController {

    public static final int MAX_COLUMN = 3;
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
    public Controlador controlador;

    public void inicializar(BorderPane borderPane) {
        this.controlador = Controlador.getInstanciaUnica();
        this.borderPane = borderPane;
        videoWeb = VideoWeb.getUnicaInstancia();
        int n = gridPane.getColumnConstraints().size();
        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setStyle("-fx-background-color: white");
        System.out.println(n);
    }
    
    public void anadirMiniatura(ImageView imageView) {

    }


    public void insertImages(LinkedList<String> listaUrls) {
        LinkedList<Video> listaVideos = new LinkedList<>(controlador.getVideoes());
        int tamLista = listaVideos.size();
        System.out.println(tamLista);
        int contadorImagenes = 0;
        int filas = 0;

        while (contadorImagenes < tamLista) {

            int posColumna = contadorImagenes % MAX_COLUMN;
            VBox box = new VBox();

            Video video = listaVideos.getFirst();
            String url = video.getRutaFichero();
            ImageView img = null;

            try {
                img = controlador.getImageFromUrl(url);
            } catch (NullPointerException ne) {
                img = videoWeb.getThumb(url);
                controlador.addImageAndUrl(url, img);
            }


            if (img != null) {
                box.setAlignment(Pos.CENTER);
                box.getChildren().add(img);
                Text tituloText = new Text(video.getTitulo());
                tituloText.setStyle("-fx-font-size: 11px");
                tituloText.setTextAlignment(TextAlignment.CENTER);
                box.getChildren().add(tituloText);
                System.out.println(url);
                gridPane.add(box, posColumna, filas);

                box.setStyle("-fx-cursor: hand");
                box.setOnMouseClicked(MouseEvent -> {
                    try {
                        visualizar(url);
                    } catch (IOException e) {

                    }
                });

                listaVideos.removeFirst();
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
