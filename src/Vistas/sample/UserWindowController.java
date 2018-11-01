package Vistas.sample;

import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modelo.dominio.Usuario;
import modelo.dominio.VideoList;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class UserWindowController {
    public VBox listasBox;
    public VBox userListsVox;
    public BorderPane mainBorderPane;
    private Usuario usuarioActual;
    private Controlador controlador = Controlador.getInstanciaUnica();
    public Label userLabel;
    public MenuBar topMenuBar;
    public HBox topHBox;
    public HashMap<String, VideoList> listasUsuario;

    public void inicializar(Usuario usuario) {
        usuarioActual = usuario;
        controlador = Controlador.getInstanciaUnica();
        listasUsuario = new HashMap<String, VideoList>();
        boolean login = controlador.login(usuarioActual.getUsername(), usuario.getPassword());
        if (login) {
            userLabel.setText(usuarioActual.getUsername());
        }
        if (!usuarioActual.getMyVideoLists().isEmpty()) {
            ListView<String> listView = new ListView<>();
            for (VideoList lista : usuarioActual.getMyVideoLists()) {
                listasUsuario.put(lista.getNombre(), lista);
                Text texto = new Text(lista.getNombre());
                texto.setOnMouseClicked(ActionEvent -> {
                    mostrarLista(texto.getText());
                });
                userListsVox.getChildren().add(texto);
            }
        }
    }

    private void mostrarLista(String videoListNombre) {
        if (!usuarioActual.contieneVideoList(videoListNombre)) {
            showListNotFoundError(videoListNombre);
        } else {

        }
    }

    public void anadirLista(MouseEvent mouseEvent) throws IOException {
        FlowPane nuevaLista = FXMLLoader.load(getClass().getResource("nuevaLista.fxml"));
        mainBorderPane.setCenter(nuevaLista);
    }

    private void showListNotFoundError(String listaNombre) {
        String mensajeError = "Error, no se ha encontrado la lista" + listaNombre;
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
}
