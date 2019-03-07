package Vistas;


import com.jfoenix.controls.JFXButton;
import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import modelo.Etiqueta;
import modelo.Usuario;
import modelo.Video;
import modelo.VideoList;
import org.junit.Test;
import persistencia.PoolEtiqueta;
import umu.tds.videos.IBuscadorVideos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class UserWindowController implements IBuscadorVideos {

    private static final int EN_RECIENTES = 1;
    private static final int EN_MAS_VISTOS = 2;
    private static final int EN_GENERAL = 0;


    public static final String RUTA_IMG_BIRTHDAY = "iconos/birthday.png";

    public VBox listasBox;
    public VBox userListsVox;
    public BorderPane mainBorderPane;
    public Button comboBoxProfile;

    @FXML
    public MenuItem logoutItem;
    @FXML
    public MenuItem closeItem;
    @FXML
    public FlowPane flowPaneCenter;
    @FXML
    public HBox birthdayBox1;
    @FXML
    public HBox birthdayBox2;
    @FXML
    public HBox bottomBox;
    @FXML
    public Button botonCargarVideos;
    @FXML
    public VBox lowerBox;
    @FXML
    public VBox topBox;
    @FXML
    public VBox leftBox;
    @FXML
    public VBox rightBox;
    @FXML
    public VBox cajaEtiquetas;
    @FXML
    public VBox cajaEtiquetasBusqueda;
    @FXML
    public Button botonBuscar;
    @FXML
    public TextField tituloBusqueda;
    @FXML
    public Button botonRecientes;
    @FXML
    public VBox cajaFiltro;
    public ImageView imgListaRep;

    private Usuario usuarioActual;
    private Controlador controlador = Controlador.getInstanciaUnica();
    public Label userLabel;
    public MenuBar topMenuBar;
    public HBox topHBox;
    private HashMap<String, VideoList> listasUsuario;
    private HashMap<Boolean, VideoList> listasVisibles;
    private ThumbGridController thumbGridController;
    private VBox visorBox;
    private boolean editMode;
    private  URL location = UserWindowController.class.getResource("UserWindowController.java");
    private boolean isCreatingLabel;
    private int intervalo;
    private Button masVistos;
    private static UserWindowController unicaInstancia;
    private int tipoListaActual = EN_GENERAL;

    public void inicializar() {

        this.isCreatingLabel = false;
        this.editMode = false;
        this.controlador = Controlador.getInstanciaUnica();                     // TODO: Cambio efectuado :S
        usuarioActual = controlador.getUsuarioActual();
        listasUsuario = new HashMap<String, VideoList>();
        listasVisibles = new HashMap<Boolean, VideoList>();
        masVistos = new Button("Mas Vistos");
        inicializarMasVistos();
        this.intervalo = 5;

        boolean login = controlador.login(usuarioActual.getUsername(), usuarioActual.getPassword());

        if (login) {
            userLabel.setText(usuarioActual.getUsername());
        }

        logoutItem.setOnAction(event -> {
            logout();
            Window currentWindow =  mainBorderPane.getScene().getWindow();
            currentWindow.fireEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        closeItem.setOnAction(event -> {
            close();
        });

        if (usuarioActual.isPremium()) {
            userListsVox.getChildren().add(masVistos);

            if (usuarioActual.isBirthday()) {
                Path pathQuit = Paths.get(RUTA_IMG_BIRTHDAY);
                System.out.println(pathQuit.toAbsolutePath().toString());
                Image imgCumple = new Image("file:" + pathQuit.toAbsolutePath().toString());
                ImageView imgBirthday1 = new ImageView(imgCumple);
                ImageView imgBirthday2 = new ImageView(imgCumple);
                birthdayBox1.getChildren().add(imgBirthday1);
                birthdayBox2.getChildren().add(imgBirthday2);
                userLabel.setText("Feliz Cumpleaños " + usuarioActual.getNombre());
            }

            Text text = new Text("Filtro");
            Label label = new Label(controlador.getFiltroActual());
            cajaFiltro.getChildren().addAll(text, label);
        }

        System.out.println("Tamano de lista: " + usuarioActual.getMyVideoLists().size());

        if (!usuarioActual.getMyVideoLists().isEmpty()) {
            ListView<String> listView = new ListView<>();
            for (VideoList lista : usuarioActual.getMyVideoLists()) {
                listasUsuario.put(lista.getNombre(), lista);
                Button botonLista = crearBotonLista(lista);
                userListsVox.getChildren().add(botonLista);
            }
        }

        unicaInstancia = this;

        // INICIALIZAMOS LA PARTE CENTRAL XD

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ThumGridWindow.fxml"));
        GridPane gridPane = null;

        try {
            gridPane = loader.load();
        } catch (IOException e) {}

        thumbGridController = loader.getController();
        thumbGridController.inicializar();

        if (gridPane != null) {
            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setMaxWidth(600);
            scrollPane.setMinHeight(400);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            mainBorderPane.setCenter(scrollPane);
        }
        thumbGridController.insertImages();
        cargarEtiquetas();

    }

    private void inicializarMasVistos() {
        masVistos.setVisible(true);
        masVistos.setPrefWidth(115);
        masVistos.setStyle("-fx-cursor: hand");
        masVistos.setOnMouseClicked(ActionEvent -> {
            if (ActionEvent.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = opcionRepMultiple(controlador.getVideosMasVistos());
                contextMenu.show(masVistos, ActionEvent.getScreenX(), ActionEvent.getScreenY());
            } else {
                setTipoDeListaActual(EN_MAS_VISTOS);
                mostrarListasMasVistas();
            }
        });
    }

    public void refrescarFiltro(String filtro) {
        cajaFiltro.getChildren().clear();
        usuarioActual.setFiltro(filtro);
        controlador.setFiltroEnCatalogoVideos();
        Text text = new Text("Filtro");
        Label label = new Label(controlador.getFiltroActual());
        cajaFiltro.getChildren().addAll(text, label);
    }

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    private void mostrarLista(String videoListNombre) {
        if (!usuarioActual.contieneVideoList(videoListNombre)) {
            Notificacion.listNotFoundError(videoListNombre);
        } else {
            LinkedList<Video> lista = controlador.getVideosFromVideoList(videoListNombre);
            if (lista != null) {
                if (!lista.isEmpty())
                    thumbGridController.displayImages(lista);
            }
        }
    }

    public void anadirLista(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("nuevaLista.fxml"));
        FlowPane nuevaLista = loader.load();
        nuevaListaController nuevaListaController = loader.getController();
        nuevaListaController.inicializar();
        mainBorderPane.setCenter(nuevaLista);
        mainBorderPane.requestLayout();
    }

    public void actualizarVistaListas(String listaNombre) {
        VideoList lista = controlador.getVideoList(listaNombre);
        Button botonLista = crearBotonLista(lista);
        userListsVox.getChildren().add(botonLista);
        mainBorderPane.requestLayout();
    }

    public void gotoProfileWindow(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfileWindow.fxml"));
        VBox profilePane = loader.load();
        ProfileWindowController profileController = loader.getController();
        profileController.inicializar();
        mainBorderPane.setCenter(profilePane);
        leftBox.setVisible(false);
        lowerBox.setVisible(false);
        rightBox.setVisible(false);
        topBox.setVisible(false);
    }


    public void logout() {
        controlador.logout();
        try {
            volverALogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void volverALogin() throws IOException {
        BorderPane root = FXMLLoader.load(getClass().getResource("inicio.fxml"));
        FlowPane login =  FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        root.setCenter(login);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Registration Form FXML Application");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void close() {
        System.exit(0);
    }

    public void cargarVideos(MouseEvent mouseEvent) throws FileNotFoundException {

        Path pathQuit = Paths.get("xml");
        System.out.println(pathQuit.toAbsolutePath().toString());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(pathQuit.toAbsolutePath().toString()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        File fichero = fileChooser.showOpenDialog(mainBorderPane.getCenter().getScene().getWindow());
        if (fichero != null) {
            // BUSCAR VIDEOS CON LA RUTA DEL FICHERO XD
          buscarVideos(fichero.getAbsolutePath());
          thumbGridController.restoreImages();
          thumbGridController.getGridPane().requestLayout();
          System.out.println(fichero.getAbsolutePath());
        }
    }


    @Override
    public void buscarVideos(String rutaxml) {
        // TODO: Implementar la busqueda de videos xD
        controlador.buscarVideos(rutaxml);
        cargarEtiquetas();
        thumbGridController.refreshThumbGrid();

    }

    public void setGridPaneToCenter() {

        this.mainBorderPane.setCenter(this.thumbGridController.getGridPane());

        leftBox.setVisible(true);
        lowerBox.setVisible(true);
        rightBox.setVisible(true);
        topBox.setVisible(true);
    }

    public void restoreImages() {
        switch (tipoListaActual) {
            case EN_RECIENTES:
                LinkedList<Video> listaVideo = controlador.getVideoesRecientesUser();
                if (!listaVideo.isEmpty()) {
                    thumbGridController.displayImages(listaVideo);
                    setTipoDeListaActual(EN_RECIENTES);
                } else {
                    this.mainBorderPane.setCenter(panelListaVacia("No hay videos en esta lista"));
                }
                break;
            case EN_MAS_VISTOS:
                this.mostrarListasMasVistas();
                break;
            default:
                this.thumbGridController.restoreImages();
        }

    }

    public void visualizar(Video video) throws IOException {
        if (!controlador.isPlaying()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VisorWindow.fxml"));
            VBox visor = loader.load();
            VisorController visorController = loader.getController();
            mainBorderPane.setCenter(visor);
            visorController.inicializar();
            visorController.startVideo(video);
            //visorController.playVideo(video);
            leftBox.setVisible(false);
            lowerBox.setVisible(false);
            rightBox.setVisible(false);
            topBox.setVisible(false);
        }
    }

    public void visualizar(LinkedList<Video> lista) throws IOException {
        if (!controlador.isPlaying()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VisorWindow.fxml"));
            VBox visor = loader.load();
            VisorController visorController = loader.getController();
            mainBorderPane.setCenter(visor);
            visorController.inicializar();
            visorController.playVideo(lista);
            leftBox.setVisible(false);
            lowerBox.setVisible(false);
            rightBox.setVisible(false);
            topBox.setVisible(false);
        }
    }

    public void cargarEtiquetas() {
        for (Etiqueta label : controlador.getEtiquetas()) {
            Text textoEtiqueta = new Text(label.getEtiqueta());
            textoEtiqueta.setOnMouseClicked(MouseEvent -> {
                if (MouseEvent.getButton() == MouseButton.SECONDARY) {
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem item1 = new MenuItem(label.getEtiqueta());
                    item1.setDisable(true);
                    MenuItem item2 = new MenuItem("Añadir a Filtro de Etiquetas");
                    contextMenu.getItems().addAll(item1, item2);
                    contextMenu.show(textoEtiqueta, MouseEvent.getScreenX(), MouseEvent.getScreenY());
                    item2.setOnAction(Event -> {
                        if (!cajaEtiquetasBusqueda.getChildren().contains(textoEtiqueta)) {
                            cajaEtiquetasBusqueda.getChildren().add(textoEtiqueta);
                        }
                    });
                }
            });
            cajaEtiquetas.getChildren().add(textoEtiqueta);
        }
    }

    public void buscarPorEtiqueta(MouseEvent mouseEvent) {
        LinkedList<String> listaEtiquetas = new LinkedList<>();
        if (!cajaEtiquetasBusqueda.getChildren().isEmpty()) {
            for (Node n : cajaEtiquetasBusqueda.getChildren()) {
                if (n instanceof Text) {
                    listaEtiquetas.addLast(((Text) n).getText());
                }
            }
            LinkedList<Video> listaVideos = controlador.buscarPorEtiquetas(listaEtiquetas);
            if (listaVideos.isEmpty()) {
                System.out.println("La busqueda etiqueta es vacia xD");
            } else
                thumbGridController.displayImages(listaVideos);
        } else {
            thumbGridController.restoreImages();
        }
    }

    public void eliminarEtiquetasDeBusqueda(MouseEvent mouseEvent) {
        cajaEtiquetas.getChildren().addAll(cajaEtiquetasBusqueda.getChildren());
        cajaEtiquetasBusqueda.getChildren().clear();

    }

    public void buscarPorTitulo(MouseEvent event) {
        String searchTitle = tituloBusqueda.getText();
        if (searchTitle.equals("")) {
            thumbGridController.restoreImages();
        } else {
            LinkedList<Video> lista = controlador.buscarPorTitulo(searchTitle);
            if (lista.isEmpty()) {
                System.out.println("La busqueda por titulo es vacia xD");
                mainBorderPane.setCenter(panelListaVacia("No se han encontrado videos"));
            } else
                thumbGridController.displayImages(lista);
        }
    }


    public void busquedaMultiple(MouseEvent event) {
        String serchTitle = tituloBusqueda.getText();
        LinkedList<String> listaEtiquetas = new LinkedList<>();
        if (!cajaEtiquetasBusqueda.getChildren().isEmpty()) {
            for (Node n : cajaEtiquetasBusqueda.getChildren()) {
                if (n instanceof Text) {
                    listaEtiquetas.addLast(((Text) n).getText());
                }
            }
            LinkedList<Video> listaVideos = controlador.busquedaMultiple(serchTitle, listaEtiquetas);
            if (listaVideos.isEmpty()) {
                System.out.println("La busqueda multiple es vacia xD");
                mainBorderPane.setCenter(panelListaVacia("No se han encontrado videos"));
            } else {
                thumbGridController.displayImages(listaVideos);
            }
        } else {
            buscarPorTitulo(event);
        }

    }


    private void mostrarListasMasVistas() {
        LinkedList<Video> listaVideo = controlador.getVideosMasVistos();
        if (!listaVideo.isEmpty())
             thumbGridController.displayImages(listaVideo);
    }

    private void mostarLista() {

    }

    public void mostrarVideosRecientes(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            ContextMenu menu = opcionRepMultiple(controlador.getVideoesRecientesUser());
            menu.show(botonRecientes, event.getScreenX(), event.getScreenY());
        } else {
            LinkedList<Video> listaVideo = controlador.getVideoesRecientesUser();
            if (!listaVideo.isEmpty()) {
                thumbGridController.displayImages(listaVideo);
                setTipoDeListaActual(EN_RECIENTES);
            } else {
                this.mainBorderPane.setCenter(panelListaVacia("No hay videos en esta lista"));
            }
        }
    }

    public void mostrarInicio(MouseEvent event) {
        thumbGridController.restoreImages();
        thumbGridController.getGridPane().requestLayout();
    }

    public void anadirEtiqueta(MouseEvent event) {
        if (!isCreatingLabel) {
            VBox box = new VBox();
            Text text = new Text("Nombre");
            TextField textField = new TextField();
            box.getChildren().addAll(text, textField);
            isCreatingLabel = true;
            Dialog dialog = new Dialog();
            ButtonType acceptDelete = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(acceptDelete, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(box);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == acceptDelete) {
                    dialog.close();
                    System.out.println(textField.getText());
                    String nombreEtiqueta = textField.getText();
                    Text textoEtiqueta = new Text(nombreEtiqueta);
                    textoEtiqueta.setOnMouseClicked(MouseEvent -> {
                        if (MouseEvent.getButton() == MouseButton.SECONDARY) {
                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem item1 = new MenuItem(nombreEtiqueta);
                            item1.disableProperty();
                            MenuItem item2 = new MenuItem("Añadir a Filtro de Etiquetas");
                            contextMenu.getItems().addAll(item1, item2);
                            contextMenu.show(textoEtiqueta, MouseEvent.getScreenX(), MouseEvent.getScreenY());
                            item2.setOnAction(Event -> {
                                if (!cajaEtiquetasBusqueda.getChildren().contains(textoEtiqueta)) {
                                    cajaEtiquetasBusqueda.getChildren().add(textoEtiqueta);
                                }
                            });
                        }
                    });
                    cajaEtiquetas.getChildren().add(textoEtiqueta);
                    PoolEtiqueta.getUnicaInstancia().addEtiqueta(nombreEtiqueta);
                    return true;
                }
                return false;
            });
            dialog.show();
            if (dialog.getResult() != null) {
                dialog.close();
                isCreatingLabel = false;
            }
        }
    }

    public ContextMenu opcionRepMultiple(LinkedList<Video> lista) {
        ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("Reproducir Lista");
        menu.getItems().add(item);
        item.setOnAction(event -> {
            try {
                visualizar(lista);
            } catch (IOException e) {

            }
        });
        return menu;
    }

    public void setInterval(int interval) {
        this.intervalo = interval;
    }

    public int getIntervalo() {
        return intervalo;
    }


    public void refrescarOpcionesPremium() {
        //TODO:
        if (!usuarioActual.isPremium()) {
          if (masVistos.isVisible())
              if (userListsVox.getChildren().contains(masVistos))
                  userListsVox.getChildren().remove(masVistos);
        } else {
            if (!userListsVox.getChildren().contains(masVistos))
                userListsVox.getChildren().add(masVistos);
            masVistos.setVisible(true);
        }
    }

    public void actualizarNombresDeListas(String nombreLista, String listaNombreNuevo) {
        Iterator<Node> it = userListsVox.getChildren().iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node instanceof Button){
                if (((Button) node).getText().equals(nombreLista))
                    ((Button) node).setText(listaNombreNuevo);
            }
        }
    }

    public static UserWindowController getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new UserWindowController();
            unicaInstancia.inicializar();
        }
        return unicaInstancia;
    }

    private VBox panelListaVacia(String mensaje) {
        VBox vBox = new VBox();
        Text text = new Text(mensaje);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(text);
        return vBox;
    }

    private void setTipoDeListaActual(int tipo) {
        this.tipoListaActual = tipo;
    }


    public Button crearBotonLista(VideoList lista) {
        Button botonLista = new Button(lista.getNombre());
        botonLista.setPrefWidth(115);
        botonLista.setStyle("-fx-cursor: hand");
        botonLista.setOnMouseClicked(ActionEvent -> {
            if (ActionEvent.getButton() == MouseButton.SECONDARY) {
                ContextMenu menu = opcionRepMultiple(lista.getVideos());
                menu.show(botonRecientes, ActionEvent.getScreenX(), ActionEvent.getScreenY());
            } else {
                setTipoDeListaActual(EN_GENERAL);
                mostrarLista(botonLista.getText());
            }
        });
        return botonLista;
    }


}
