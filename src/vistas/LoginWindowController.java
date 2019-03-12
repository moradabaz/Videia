package vistas;

import controlador.Controlador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.Usuario;

import java.io.IOException;

public class LoginWindowController {

    public Button idButtonRegistro;
    public Text textoPassword;
    public TextField userField;
    public VBox idVboxPrincipal;
    public PasswordField passwdField;
    public FlowPane idFlowPaneLogin;
    private Controlador controlador = Controlador.getInstanciaUnica();


    @FXML
    private void login(ActionEvent event) {
        textoPassword.setText("Cargando...");
        String user = userField.getText();
        String passwd = passwdField.getText();
        textoPassword.setText("");
        boolean logeado = controlador.login(user, passwd);
        if (logeado) {
            System.out.println("El usuario " + user + "esta logeado");
            Usuario usuarioActual = controlador.getUsuarioActual();
            if (usuarioActual == null) {
                System.err.println("El usuario " +  user + " ha podido ser recogido");
            } else {
                try {

                    gotoUserWindow(event, this.controlador);
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
        } else {
            textoPassword.setStyle("-fx-stroke: red;");
            textoPassword.setText("Usuario o contraseña incorrectos");
        }

    }


    public void gotoUserWindow(ActionEvent event, Controlador controlador) throws IOException {      // TODO: Funcionq que lleva a la ventana USer
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane UserView = loader.load();
        Scene userViewScene = new Scene(UserView);
        userViewScene.getStylesheets().add(UserView.getStyle());
        UserWindowController userWindowController = loader.getController();
        userWindowController.inicializar();
        Stage window =  new Stage();
        window.setScene(userViewScene);
        window.setFullScreen(false);
        window.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }

    @FXML
    public void gotoRegistro(ActionEvent actionEvent) throws IOException {
        BorderPane bp = (BorderPane) idFlowPaneLogin.getParent();
        FlowPane registro = FXMLLoader.load(getClass().getResource("registro.fxml"));
        bp.setCenter(registro);
    }

    public void logout() {
        controlador.logout();
        salir();
    }

    public void salir() {
        System.exit(0);
    }

}
