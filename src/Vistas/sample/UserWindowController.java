package Vistas.sample;


import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import modelo.dominio.Usuario;
import modelo.dominio.VideoList;
import umu.tds.videos.IBuscadorVideos;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


public class UserWindowController implements IBuscadorVideos {

    public VBox listasBox;
    public VBox userListsVox;
    public BorderPane mainBorderPane;
    public Button comboBoxProfile;

    public MenuItem logoutItem;
    public MenuItem closeItem;
    public FlowPane flowPaneCenter;
    public HBox birthdayBox1;
    public HBox birthdayBox2;
    public HBox bottomBox;
    public Button botonCargarVideos;

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


    public void inicializar() {

        this.editMode = false;
        this.controlador = Controlador.getInstanciaUnica();                     // TODO: Cambio efectuado :S
        usuarioActual = controlador.getUsuarioActual();
        listasUsuario = new HashMap<String, VideoList>();
        listasVisibles = new HashMap<Boolean, VideoList>();

//        controlador.eliminarTodoslosVideos();

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
            Button masVistos = new Button("Mas Vistos");
            masVistos.setPrefWidth(115);
            masVistos.setStyle("-fx-cursor: hand");
            masVistos.setOnMouseClicked(ActionEvent -> {
                //mostrarLista(masVistos.getText());
            });
            userListsVox.getChildren().add(masVistos);
            
            // Cumpleaños

            if (usuarioActual.isBirthday()) {
                Path pathQuit = Paths.get("iconos/birthday.png");
                System.out.println(pathQuit.toAbsolutePath().toString());
                Image imgCumple = new Image("file:" + pathQuit.toAbsolutePath().toString());
                ImageView imgBirthday1 = new ImageView(imgCumple);
                ImageView imgBirthday2 = new ImageView(imgCumple);
                birthdayBox1.getChildren().add(imgBirthday1);
                birthdayBox2.getChildren().add(imgBirthday2);
                userLabel.setText("Feliz Cumpleaños " + usuarioActual.getNombre());
            }
        }

        if (!usuarioActual.getMyVideoLists().isEmpty()) {
            ListView<String> listView = new ListView<>();
            for (VideoList lista : usuarioActual.getMyVideoLists()) {
                listasUsuario.put(lista.getNombre(), lista);
                Button botonLista = new Button(lista.getNombre());
                botonLista.setPrefWidth(115);
                botonLista.setStyle("-fx-cursor: hand");
                botonLista.setOnMouseClicked(ActionEvent -> {
                    mostrarLista(botonLista.getText());
                });
                userListsVox.getChildren().add(botonLista);
            }
        }

    //    String urls[] = {"https://www.youtube.com/watch?v=i-Xn9zWJTvk", "https://www.youtube.com/watch?v=i-Xn9zWJTvk", "https://www.youtube.com/watch?v=i-Xn9zWJTvk",
     //           "https://www.youtube.com/watch?v=i-Xn9zWJTvk", "https://www.youtube.com/watch?v=i-Xn9zWJTvk"};


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ThumGridWindow.fxml"));
        GridPane gridPane = null;

        try {
            gridPane = loader.load();
        } catch (IOException e) {}

        thumbGridController = loader.getController();
        thumbGridController.inicializar(this);

        if (gridPane != null) {
            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setMaxWidth(600);
            scrollPane.setMinHeight(400);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            mainBorderPane.setCenter(scrollPane);
        }

        thumbGridController.insertImages();
    }

    private void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    private boolean isEditMode() {
        return editMode;
    }

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    private void mostrarLista(String videoListNombre) {
        if (!usuarioActual.contieneVideoList(videoListNombre)) {
            Notificacion.listNotFoundError(videoListNombre);
        } else {

        }
    }

    public void anadirLista(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("nuevaLista.fxml"));
        FlowPane nuevaLista = loader.load();
        nuevaListaController nuevaListaController = loader.getController();
        nuevaListaController.inicializar(controlador, this);
        mainBorderPane.setCenter(nuevaLista);
        mainBorderPane.requestLayout();
    }

    public void actualizarVistaListas(VideoList videoList) {
        Button botonLista = new Button(videoList.getNombre());
        botonLista.setStyle("-fx-cursor: hand");
        botonLista.setPrefWidth(115);
        botonLista.setOnMouseClicked(ActionEvent -> {
            mostrarLista(botonLista.getText());
        });
        userListsVox.getChildren().add(botonLista);
        mainBorderPane.requestLayout();
    }

    public void gotoProfileWindow(MouseEvent mouseEvent) throws IOException {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfileWindow.fxml"));
        VBox profilePane = loader.load();
        ProfileWindowController profileController = loader.getController();
        profileController.inicializar(controlador);
        Scene profileScene = new Scene(profilePane);
        Stage window = new Stage();
        window.setResizable(true);
        window.setScene(profileScene);
        window.show();
        currentStage.close();
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
        FlowPane login =  FXMLLoader.load(getClass().getResource("login.fxml"));
        root.setCenter(login);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Registration Form FXML Application");
        Scene scene = new Scene(root);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
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
          System.out.println(fichero.getAbsolutePath());
        }
    }


    @Override
    public void buscarVideos(String rutaxml) {
        // TODO: Implementar la busqueda de videos xD
        controlador.buscarVideos(rutaxml);
        thumbGridController.refreshThumbGrid();
    }


    public void setGridPaneToCenter() {
        this.mainBorderPane.setCenter(this.thumbGridController.getGridPane());
    }


    public void visualizar(String url) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VisorWindow.fxml"));
        VBox visor = loader.load();
        VisorController visorController = loader.getController();
        mainBorderPane.setCenter(visor);
        visorController.inicializar(this, url);
    }
}
