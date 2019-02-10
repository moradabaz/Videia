package Vistas.sample;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import modelo.Etiqueta;
import modelo.Video;

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

    public static boolean confirmationQuestion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            alert.close();
            return true;
        } else if (alert.getResult() == ButtonType.NO){
            alert.close();
            return false;
        }
        return false;
    }

    public static void alertError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    public static void showVideoDetails(Video video) {
        Dialog dialog = new Dialog();

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        Text nombre = new Text("Nombre: " + video.getTitulo());
        Text numReproducciones = new Text("Numero reproducciones: " + video.getReproducciones());
        Text etiquetasText = new Text("Etiquetas:");
        VBox vBoxLabel = new VBox();
        for (Etiqueta etiqueta: video.getEtiquetas()) {
           vBoxLabel.getChildren().add(new Label(" - " + etiqueta.getEtiqueta()));
        }
        vBox.getChildren().addAll(nombre, numReproducciones, etiquetasText, vBoxLabel);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(vBox);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.OK) {
            dialog.close();
        }
    }
}
