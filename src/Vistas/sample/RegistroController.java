package Vistas.sample;

import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;

public class RegistroController implements Initializable {

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

    public void volverAlInicio(ActionEvent actionEvent) throws IOException {
        Parent inicio = FXMLLoader.load(getClass().getResource("login.fxml"));
        BorderPane bp = (BorderPane) idflowpaneregister.getParent();
        bp.setCenter(inicio);
    }



    public void registrarUsuario(ActionEvent actionEvent) {
        boolean registrado = false;
        if (checkedValues()) {
            String user = idTxtFldUsuario.getText();
            String passwd = idPasswdField.getText();
            String repassword = idPasswdFieldRepeat.getText();
            String nombre = idNombre.getText();
            String fecha = FechaNac.getValue().toString();
            String apellidos = idApellidos.getText();
            String email = idTxtFldEmail.getText();
            registrado = controlador.registrarUsuario(user, passwd, nombre, apellidos, fecha, email);
        }
        if (registrado) {

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

}
