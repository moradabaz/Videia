package vistas;

import VideoWeb.VideoWeb;
import controlador.Controlador;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import modelo.Video;
import modelo.VideoList;


import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ThumbGridController {

    private static final String SUB_RUTA = "thumbs/";
    public static final int MAX_COLUMN = 4;

    public static int contador = 0;
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
    private HashSet<String> conjuntoURLs;
    private HashMap<String, ImageView> mapaImagenes;
    private HashMap<String, String> rutas;

    /**
     *
     */
    public void inicializar() {
        this.controlador = Controlador.getInstanciaUnica();
        this.posColumna = 0;
        this.filas = 0;
        this.contadorImagenes = 0;
        this.userWindowController = UserWindowController.getInstancia();
        this.borderPane = userWindowController.getMainBorderPane();
        videoWeb = VideoWeb.getUnicaInstancia();
        int n = gridPane.getColumnConstraints().size();
        this.listaVideos = controlador.getVideoes();
        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setStyle("-fx-background-color: white");
        System.out.println(n);
        conjuntoURLs = new HashSet<>();
        mapaImagenes = new HashMap<>();
        rutas = new HashMap<>();
    }
    

    public void restoreImages() {
        userWindowController.setGridPaneToCenter();
        posColumna = 0;
        filas = 0;
        contadorImagenes = 0;
        listaVideos = controlador.getVideoes();
        int tamLista = listaVideos.size();
        mostrarMiniaturasDeVideos(listaVideos, tamLista);
        borderPane.setCenter(gridPane);
        gridPane.requestLayout();
        borderPane.requestLayout();
    }


    public void displayImages(LinkedList<Video> lista) {
        userWindowController.setGridPaneToCenter();
        int tamLista = lista.size();
        posColumna = 0;
        filas = 0;
        contadorImagenes = 0;
        gridPane.getChildren().clear();
        mostrarMiniaturasDeVideos(lista, tamLista);
    }


    private void mostrarMiniaturasDeVideos(List<Video> lista, int tamLista) {
        Iterator<Video> it = lista.iterator();
        while (contadorImagenes < tamLista) {
            posColumna = contadorImagenes % MAX_COLUMN;
            VBox box = new VBox();
            Video video = null;
            if (it.hasNext()) {
                video = it.next();
                if (video != null) {
                    String url = video.getRutaFichero();
                    ImageView img = mapaImagenes.get(url);

                    if (img == null) {
                        img = videoWeb.getThumb(url);
                        mapaImagenes.put(url, img);
                    }

                    box.setAlignment(Pos.CENTER);
                    anadirBackground(box);

                    box.getChildren().add(img);
                    Text tituloText = acortarTitulo(video.getTitulo());

                    tituloText.setStyle("-fx-font-size: 11px");
                    tituloText.setTextAlignment(TextAlignment.CENTER);

                    box.getChildren().add(tituloText);
                    gridPane.add(box, posColumna, filas);
                    box.setStyle("-fx-cursor: hand");
                    ContextMenu contextMenu = createContextMenu(video);
                    Video finalVideo = video;

                    box.setOnMouseClicked(MouseEvent -> {
                        if (MouseEvent.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(box, MouseEvent.getScreenX(), MouseEvent.getScreenY());
                        } else {
                            try {
                                visualizar(finalVideo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    conjuntoURLs.add(url);
                    contadorImagenes++;
                    if (contadorImagenes % 4 == 0) {
                        filas++;
                    }
                }
            }
        }
    }


    public void insertImages() {
        listaVideos = controlador.getVideoes();
        int tamLista = listaVideos.size();
        System.out.println(tamLista);
        Iterator<Video> it = listaVideos.iterator();
        while (contadorImagenes < tamLista) {

            posColumna = contadorImagenes % MAX_COLUMN;
            Video video = null;
            if (it.hasNext()) video = it.next();
            String url = video.getRutaFichero();
            ImageView img = getImagen(url);

            if (img != null) {
                System.out.println(url);
                if (!conjuntoURLs.contains(url)) {
                    VBox box = new VBox();
                    box.setAlignment(Pos.CENTER);
                    box.getChildren().add(img);
                    String titulo = video.getTitulo();
                    Text tituloText = acortarTitulo(titulo);
                    box.getChildren().add(tituloText);
                    box.setStyle("-fx-cursor: hand");
                    ContextMenu contextMenu = createContextMenu(video);
                    Video finalVideo = video;
                    box.setOnMouseClicked(MouseEvent -> {
                        if (MouseEvent.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(box, MouseEvent.getScreenX(), MouseEvent.getScreenY());
                        } else {
                            try {
                                visualizar(finalVideo);
                            } catch (IOException e) {

                            }
                        }
                    });

                    anadirBackground(box);

                   // box.setOnMouseEntered(event ->);

                    gridPane.add(box, posColumna, filas);
                    conjuntoURLs.add(url);
                    mapaImagenes.put(url, img);
                    contadorImagenes++;
                    if (contadorImagenes % 4 == 0) {
                        filas++;
                    }
                }
            }
            gridPane.requestLayout();
            borderPane.requestLayout();
        }
        System.out.println("Contador de Imagenes: " + contadorImagenes);
    }

    private void visualizar(Video video) throws IOException {
        controlador.anadirVideoAReciente(video);
        userWindowController.visualizar(video);
    }

    private ContextMenu createContextMenu(Video video) {
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
                    String nombreVideoList = vdlist.getNombre();
                    MenuItem menuItem = new MenuItem(vdlist.getNombre());
                    menuItem.setOnAction(event1 -> {
                        controlador.actualizarVideoesEnLista(video.getTitulo(), nombreVideoList);
                    });
                    listmenu.getItems().add(menuItem);
                }
            }
            listmenu.show(gridPane, contextMenu.getX(), contextMenu.getY());
        });

        item3.setOnAction(e -> {
            Notificacion.showVideoDetails(video);
        });

        contextMenu.getItems().addAll(item2, item3);
        return contextMenu;
    }

    public void refreshThumbGrid() {
        this.controlador = Controlador.getInstanciaUnica();
        insertImages();
    }

    public GridPane getGridPane() {
        return gridPane;
    }


    public void almacenarMiniatura(String ruta) {
        URL url = null;
        String route = videoWeb.getUrlID(ruta);
        String rutaImagenEnLocal = "";
        rutaImagenEnLocal = SUB_RUTA + route + ".jpg";
        rutas.put(ruta, rutaImagenEnLocal);

        try {
            String urlRouteImg = videoWeb.getImageUrl(ruta);
            url = new URL(urlRouteImg);                                     // Aqui OCURRE ALGO, SE PONE NULO
            InputStream in = new BufferedInputStream(url.openStream());
            String rutaFichero = rutas.get(ruta);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(rutaFichero));

            for ( int i; (i = in.read()) != -1; ) {
                out.write(i);
            }
            in.close();
            out.close();

        } catch (NullPointerException e) {
            System.err.println("Alguna ruta es nula");
        } catch (Exception d) {
            System.err.println("Failazo");
        }
    }

    private Text acortarTitulo(String titulo) {
        Text text;
        if (titulo.length() > 20)
            text = new Text(titulo.substring(0, 20) + "...");
        else
            text = new Text(titulo);

        text.setStyle("-fx-font-size: 11px");
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }


    private ImageView getImagen(String url) {
        String rutaImagenFile = SUB_RUTA + videoWeb.getUrlID(url) + ".jpg";
        if (!rutas.isEmpty())
            if (rutas.get(url) == null) {
                almacenarMiniatura(url);
                Image img = createImageFromRoute(rutaImagenFile);
                ImageView imgView = new ImageView(img);
                return imgView;
            } else {
                if (mapaImagenes.containsKey(url)) {
                    return mapaImagenes.get(url);
                } else {
                    Image img = createImageFromRoute(rutas.get(url));
                    ImageView imgView = new ImageView(img);
                    return imgView;
                }
            }
        else {
            almacenarMiniatura(url);
            Image img = createImageFromRoute(rutaImagenFile);
            return new ImageView(img);
        }
    }

    private Image createImageFromRoute(String ruta) {
        Path path = Paths.get(ruta);
        Image image = new Image("file:" + path.toAbsolutePath().toString());
        return image;
    }


    private void anadirBackground(VBox box) {
        box.setOnMouseEntered(event -> {
            box.setBackground(new Background(new BackgroundFill(Color.web("#42A5F5"), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        box.setOnMouseExited(event -> {
            box.setBackground(null);
        });
    }

}



