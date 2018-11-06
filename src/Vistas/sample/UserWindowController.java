package Vistas.sample;

import com.sun.tools.corba.se.idl.constExpr.BooleanNot;
import com.sun.tools.javac.comp.Flow;
import controlador.Controlador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.dominio.Usuario;
import modelo.dominio.VideoList;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class UserWindowController {
    public VBox listasBox;
    public VBox userListsVox;
    public BorderPane mainBorderPane;
    public Button comboBoxProfile;

    public MenuItem logoutItem;
    public MenuItem closeItem;

    private Usuario usuarioActual;
    private Controlador controlador = Controlador.getInstanciaUnica();
    public Label userLabel;
    public MenuBar topMenuBar;
    public HBox topHBox;
    public HashMap<String, VideoList> listasUsuario;
    public HashMap<Boolean, VideoList> listasVisibles;


    public void inicializar(Controlador controlador) {
        this.controlador = controlador;                     // TODO: Cambio efectuado :S
        usuarioActual = controlador.getUsuarioActual();
        listasUsuario = new HashMap<String, VideoList>();
        listasVisibles = new HashMap<Boolean, VideoList>();
        boolean login = controlador.login(usuarioActual.getUsername(), usuarioActual.getPassword());
        if (login) {
            userLabel.setText(usuarioActual.getUsername());
        }

        String perfil = "Ver Perfil";

        logoutItem.setOnAction(event -> {
            logout();
        });

        closeItem.setOnAction(event -> {
            close();
        });

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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfileWindow.fxml"));
        FlowPane profilePane = loader.load();
        ProfileWindowController profileController = loader.getController();
        profileController.inicializar(controlador);
        Scene profileScene = new Scene(profilePane);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(profileScene);
        window.setResizable(false);
        window.show();
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
       // Stage primaryStage = (Stage) ((Node) this.getClass().getClassLoader().getParent().getSource()).getScene().getWindow();
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
        logout();
        System.exit(0);
    }

}
