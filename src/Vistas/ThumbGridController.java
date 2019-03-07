package Vistas;

import VideoWeb.VideoWeb;
import controlador.Controlador;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import modelo.Video;
import modelo.VideoList;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ThumbGridController {

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
    private HashSet<String> listaUrl;
    private HashMap<String, ImageView> mapaImagenes;
    private HashMap<String, String> rutas;
    private static final String subruta = "thumbs/";

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
        listaUrl = new HashSet<>();
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
        System.out.println(tamLista);
        Iterator<Video> it = listaVideos.iterator();
        while (contadorImagenes < tamLista) {
            posColumna = contadorImagenes % MAX_COLUMN;

            VBox box = new VBox();
            Video video = null;
            if (it.hasNext()) video = it.next();
            String url = video.getRutaFichero();

            ImageView img = mapaImagenes.get(url);
            if (img == null) {
                img = videoWeb.getThumb(url);
                mapaImagenes.put(url, img);
                almacenarMiniatura(url);
            }

            box.setAlignment(Pos.CENTER);
            box.getChildren().add(img);

            Text tituloText = acortarTitulo(video.getTitulo());

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
                    }
                }
            });
            listaUrl.add(url);
            contadorImagenes++;
            // }
            if (contadorImagenes % 4 == 0) {
                filas++;
            }
        }
        borderPane.setCenter(gridPane);
        gridPane.requestLayout();
        borderPane.requestLayout();
    }


    public void displayImages(LinkedList<Video> lista) {
        userWindowController.setGridPaneToCenter();
        int tamLista = lista.size();
        Iterator<Video> it = lista.iterator();
        int posColumna = 0;
        int filas = 0;
        int contadorImagenes = 0;
        gridPane.getChildren().clear();
        while (contadorImagenes < tamLista) {
            posColumna = contadorImagenes % MAX_COLUMN;
            VBox box = new VBox();
            Video video = null;
            if (it.hasNext()) video = it.next();
            assert video != null;
            String url = video.getRutaFichero();
            ImageView img = mapaImagenes.get(url);
            if (img == null) {
                img = videoWeb.getThumb(url);
                mapaImagenes.put(url, img);
            }
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
            //listaUrl.addLast(url);
            contadorImagenes++;
            if (contadorImagenes % 4 == 0) {
                filas++;
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

                if (!listaUrl.contains(url)) {
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
                    gridPane.add(box, posColumna, filas);
                    listaUrl.add(url);
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
        String rutaStore = "";
        if (!rutas.isEmpty()) {
                rutaStore = subruta + route + ".jpg";
                rutas.put(ruta, rutaStore);

        } else {
            rutaStore = subruta + route + ".jpg";
            rutas.put(ruta, rutaStore);
        }
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
        String rutaImagenFile = subruta + videoWeb.getUrlID(url) + ".jpg";
        if (!rutas.isEmpty())
            if (rutas.get(url) == null) {
                almacenarMiniatura(url);
                Path path = Paths.get(rutaImagenFile);
                Image img = new Image("file:" + path.toAbsolutePath().toString());
                ImageView imgView = new ImageView(img);
                return imgView;
            } else {
                if (mapaImagenes.containsKey(url)) {
                    return mapaImagenes.get(url);
                } else {
                    Path path = Paths.get(rutas.get(url));
                    Image img = new Image("file:" + path.toAbsolutePath().toString());
                    ImageView imgView = new ImageView(img);
                    return imgView;
                }
            }
        else {
            almacenarMiniatura(url);
            Path path = Paths.get(rutaImagenFile);
            Image img = new Image("file:" + path.toAbsolutePath().toString());
            ImageView imgView = new ImageView(img);
            return imgView;
        }
    }

}



