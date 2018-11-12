package Vistas.sample;

import controlador.Controlador;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import modelo.dominio.Usuario;
import modelo.dominio.VideoList;

import java.io.IOException;
import java.util.LinkedList;

public class nuevaListaController {

    public FlowPane flowpaneNuevaLista;
    Controlador controlador = Controlador.getInstanciaUnica();
    public TextField nombreField;
    public Button acceptButton;
    public BorderPane borderPaneParent;
    public UserWindowController userWindowController;

    public void inicializar(Controlador controlador, UserWindowController userWindowController) {
        this.controlador = controlador;
        if (userWindowController.getMainBorderPane() != null)
            setBorderPaneParent(userWindowController.getMainBorderPane());
        else System.err.println("FlowNuevaLista no tiene padre");
        this.userWindowController = userWindowController;
    }

    public void crearLista(ActionEvent actionEvent) {
        String nombreLista = nombreField.getText();
        if (nombreLista.equals("") || nombreField == null) {
           Notificacion.noNameListError();
           System.out.println("lista: " + nombreLista);
        } else {
            Usuario usuario = controlador.getUsuarioActual();
            if (usuario.contieneVideoList(nombreLista)) {
                Notificacion.nameExistWarning();
            } else {
                VideoList videoList = new VideoList(nombreField.getText());
                usuario.addVideoList(videoList);
                controlador.registrarVideoList(videoList.getNombre(), new LinkedList<>());
                System.out.println("Exito");
                userWindowController.actualizarVistaListas(videoList);  //TODO FALLO

            }
        }
        volver();
    }

    public BorderPane getBorderPaneParent() {
        return borderPaneParent;
    }

    public void setBorderPaneParent(BorderPane borderPaneParent) {
        this.borderPaneParent = borderPaneParent;
    }

    public void cancelar(ActionEvent mouseEvent) throws IOException {
        volver();
    }

    private void volver() {
        if (borderPaneParent != null) {
            borderPaneParent.setCenter(new FlowPane());
        } else {
            System.err.println("BorderPane es nulo");
        }
    }
}
