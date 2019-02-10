package Vistas.sample;

import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.Usuario;

public class RegistroController implements Initializable {

    public Text textoError;
    Controlador controlador = Controlador.getInstanciaUnica();

    @FXML
    Button idButtonAceptar;
    @FXML
    Button idButtonCancelar;
    @FXML
    TextField idNombre;
    @FXML
    TextField idApellidos;
    @FXML
    TextField idTxtFldUsuario;
    @FXML
    TextField  idTxtFldEmail;
    @FXML
    PasswordField idPasswdField;
    @FXML
    PasswordField idPasswdFieldRepeat;
    @FXML
    FlowPane idflowpaneregister;
    @FXML
    DatePicker FechaNac;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlador = Controlador.getInstanciaUnica();
    }

    @FXML
    private void volverAlInicio(ActionEvent actionEvent) throws IOException {
        Parent inicio = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        BorderPane bp = (BorderPane) idflowpaneregister.getParent();
        bp.setCenter(inicio);
    }

    @FXML
    private void registrarUsuario(ActionEvent mouseEvent) throws IOException {
        boolean registrado = false;
        String user = idTxtFldUsuario.getText();
        String passwd = idPasswdField.getText();
        if (checkedValues()) {
            String repassword = idPasswdFieldRepeat.getText();
            String nombre = idNombre.getText();
            String fecha = String.valueOf(FechaNac.getValue());
            System.out.println("Fecha de registro: " + fecha);
            String apellidos = idApellidos.getText();
            String email = idTxtFldEmail.getText();
            registrado = controlador.registrarUsuario(user, passwd, nombre, apellidos, fecha, email);
        }
        if (registrado) {
            this.controlador.login(user, passwd);
            gotoUserWindow(mouseEvent, controlador.getUsuarioActual());
        }
    }


    private boolean checkedValues() {
        String user = idTxtFldUsuario.getText();
        String passwd = idPasswdField.getText();
        String repassword = idPasswdFieldRepeat.getText();
        String nombre = idNombre.getText();
        String fecha = FechaNac.getValue().toString();
        System.out.println("FECHA: " + fecha);
        if (checkFields(user, passwd, repassword, nombre)) {

            if (controlador.usuarioRegistrado(user)) {
                textoError.setStyle("-fx-stroke: red;");
                textoError.setText("El usuario " + user + " ya existe");
                return false;
            }

            if (!passwd.equals(repassword)) {
                Notificacion.passwdMatchError();
                System.out.println("Las contrasenas no coinciden");
                return false;
            }
        } else {
            return  false;
        }
        return true;
    }

    private boolean checkFields(String ... fields) {
       for (String field : fields) {
           if (field.trim().isEmpty()) {
               Notificacion.voidFieldsError();
               return false;
           }
       }
        return true;
    }


    private void gotoUserWindow(ActionEvent event, Usuario usuario) throws IOException {      // TODO: Funcionq que lleva a la ventana USer
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane UView = loader.load();
        Scene UViewScene = new Scene(UView);
        UserWindowController uControler = loader.getController();
        uControler.inicializar();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(UViewScene);
        window.setFullScreen(true);
        window.show();
    }




}
