package Vistas.sample;

import VideoWeb.VideoWeb;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import controlador.Controlador;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import modelo.dominio.Video;
import modelo.dominio.VideoList;


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
    public Controlador controlador;
    LinkedList<Video> listaVideos;
    private int posColumna;
    private int filas;
    private int contadorImagenes;
    private UserWindowController userWindowController;
    LinkedList<String> listaUrl;


    public void inicializar(UserWindowController userWindowController) {
        this.controlador = Controlador.getInstanciaUnica();
        this.posColumna = 0;
        this.filas = 0;
        this.contadorImagenes = 0;
        this.userWindowController = userWindowController;
        this.borderPane = userWindowController.getMainBorderPane();
        videoWeb = VideoWeb.getUnicaInstancia();
        int n = gridPane.getColumnConstraints().size();
        this.listaVideos = controlador.getVideoes();
        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setStyle("-fx-background-color: white");
        System.out.println(n);
        listaUrl = new LinkedList<>();
    }
    
    public void anadirMiniatura(ImageView imageView) {

    }


    public void insertImages() {
        //gridPane.getChildren().clear();

        listaVideos = controlador.getVideoes();
        int tamLista = listaVideos.size();
        System.out.println(tamLista);
        Iterator<Video> it = listaVideos.iterator();

        while (contadorImagenes < tamLista) {
            posColumna = contadorImagenes % MAX_COLUMN;

            VBox box = new VBox();
            Video video = null;
            if (it.hasNext()) video = it.next();
            String url = video.getRutaFichero();
            ImageView img = videoWeb.getThumb(url);
            if (img != null) {
                box.setAlignment(Pos.CENTER);
                box.getChildren().add(img);
                String titulo = video.getTitulo();
                Text tituloText;
                if (titulo.length() > 20)
                     tituloText = new Text(video.getTitulo().substring(0, 20) + "...");
                else
                    tituloText = new Text(video.getTitulo());

                tituloText.setStyle("-fx-font-size: 11px");
                tituloText.setTextAlignment(TextAlignment.CENTER);
                box.getChildren().add(tituloText);
                System.out.println(url);
                if (!listaUrl.contains(url)) {
                    gridPane.add(box, posColumna, filas);
                    box.setStyle("-fx-cursor: hand");
                    ContextMenu contextMenu = createContextMenu(video);
                    box.setOnMouseClicked(MouseEvent -> {
                        if (MouseEvent.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(box, MouseEvent.getScreenX(), MouseEvent.getScreenY());
                        } else {
                            try {
                                visualizar(url);
                            } catch (IOException e) {

                            }
                        }
                    });
                    listaUrl.addLast(url);
                    contadorImagenes++;
                }
            }

            if (contadorImagenes % 4 == 0) {
                filas++;
            }

            gridPane.requestLayout();
            borderPane.requestLayout();
        }
    }

    private void visualizar(String url) throws IOException {
        userWindowController.visualizar(url);
    }

    private ContextMenu createContextMenu(  Video video) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item2 = new MenuItem("Anadir a lista");
        MenuItem item3 = new MenuItem("Detalles");

        item2.setOnAction(event -> {
            ContextMenu listmenu = new ContextMenu();
               //for (VideoList vdlist : controlador.videli)
            if (controlador.getUserVideoLists().isEmpty()) {
                MenuItem item = new MenuItem("No List");
                listmenu.getItems().add(item);
            } else {
                for (VideoList vdlist : controlador.getUserVideoLists()) {
                    MenuItem menuItem = new MenuItem(vdlist.getNombre());
                    menuItem.setOnAction(event1 -> {
                        // TODO: Añadir Video a la lista :P
                        controlador.addVideoToVideoList(video, vdlist);
                        if (vdlist.contains(video))
                            System.out.println("Video anadido con existo");
                    });
                    listmenu.getItems().add(menuItem);
                }
            }
            listmenu.show(gridPane, contextMenu.getX(), contextMenu.getY());
        });

        contextMenu.getItems().addAll(item2, item3);
        return contextMenu;
    }

    public void refreshThumbGrid() {
        this.controlador = Controlador.getInstanciaUnica();
        insertImages();

        gridPane.requestLayout();
        borderPane.requestLayout();
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}

