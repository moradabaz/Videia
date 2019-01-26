package Vistas.sample;

import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.dominio.Usuario;


public class LoginController implements Initializable {

    public static final int NUM_MENUS = 4;
    public Text textoPassword;

    Controlador controlador = Controlador.getInstanciaUnica();

    @FXML
    FlowPane idFlowPaneLogin;
    @FXML
    VBox idVboxPrincipal;
    @FXML
    Button idButtonRegistro;
    @FXML
    PasswordField passwdField;
    @FXML
    TextField userField;

    VBox parteIzquierda;
    VBox parteSuperior;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlador = Controlador.getInstanciaUnica();
        parteIzquierda = new VBox();
        parteSuperior = new VBox();

        /*Path path = Paths.get("../iconos/loginBackground.jpg");
        Image image = new Image("file:" + path.toAbsolutePath().toString());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        idFlowPaneLogin.setBackground(new Background(backgroundImage));*/
    }

    @FXML
    private void login(ActionEvent event) {
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
                    e.printStackTrace();
                }
            }
        } else {
            textoPassword.setStyle("-fx-stroke: red;");
            textoPassword.setText("Usuario o contraseña incorrectos");
        }

    }

    @FXML
    public void gotoRegistro(ActionEvent actionEvent) throws IOException {
        BorderPane bp = (BorderPane) idFlowPaneLogin.getParent();
        FlowPane registro = FXMLLoader.load(getClass().getResource("registro.fxml"));
        bp.setCenter(registro);
    }

    private void gotoUserWindow(ActionEvent event, Controlador controlador) throws IOException {      // TODO: Funcionq que lleva a la ventana USer
     FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane UserView = loader.load();
        Scene userViewScene = new Scene(UserView);
        UserWindowController userWindowController = loader.getController();
        userWindowController.inicializar();
        Stage window =  new Stage();
        window.setScene(userViewScene);
        window.setFullScreen(true);
        window.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }

    public void logout() {
       // bp.setCenter(new FlowPane());
        controlador.logout();
        salir();
    }

    public void salir() {
        System.exit(0);
    }
}
