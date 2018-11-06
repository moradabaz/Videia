package Vistas.sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.text.*;

public class Notificacion {

    public static final int PASSWD = 1;
    public static final int FIELDS = 2;

    public static void voidFieldsError() {
        String mensajeError = "Los campos obligatorios estan vacios";
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void listNotFoundError(String listaNombre) {
        String mensajeError = "Error, no se ha encontrado la lista" + listaNombre;
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void passwdMatchError() {
        String mensajeError = "Las contraseñas no coinciden";
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void noNameListError() {
        String mensajeError = "La lista no tiene nombre";
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void nameExistWarning() {
        String mensajeError = "Ya existe una lista para ese nombre";
        Alert alert = new Alert(Alert.AlertType.WARNING, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void oldPasswdError() {
        String mensajeError = "Contraseña de Usuario Incorrecta";
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeError, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void changeSuccess(int tipo) {
        String mensaje = "";
        switch (tipo) {
            case PASSWD:
                mensaje = "Contraseña modificada con éxito";
                break;
            case FIELDS:
                mensaje = "Campos modificados con éxito";
                break;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static boolean deleteAcountQuestion() {
        String mensajeError = "¿Quiere eliminar su cuenta?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensajeError, ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            // TODO: Eliminar Cuenta
            alert.close();
            return true;
        } else if (alert.getResult() == ButtonType.NO){
            alert.close();
            return false;
        }
        return false;
    }

    public static void notificar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
}
