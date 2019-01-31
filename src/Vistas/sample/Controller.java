package Vistas.sample;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javax.print.DocFlavor;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Controller implements Initializable {

    @FXML
    BorderPane idMainPane;
    @FXML
    VBox idVboxPrincipal;
    @FXML
    Button idButtonRegistro;

    public void bindPanels(Pane pane1, Pane pane2) {
        pane1.prefWidthProperty().bind(pane2.prefWidthProperty());
        pane1.maxWidthProperty().bind(pane2.maxWidthProperty());
        pane1.minWidthProperty().bind(pane2.minWidthProperty());
        pane1.prefHeightProperty().bind(pane2.prefHeightProperty());
        pane1.maxHeightProperty().bind(pane2.maxHeightProperty());
        pane1.minHeightProperty().bind(pane2.minHeightProperty());
    }

    public void cambiarTextoRegistro(ActionEvent mouseEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //bindPanels(idVboxPrincipal, idAnchorPane);
    }

    public void gotoRegistro(ActionEvent actionEvent) throws IOException {

        FlowPane registro = FXMLLoader.load(getClass().getResource("registro.fxml"));
        idMainPane.setCenter(registro);

        /*       Scene scene = new Scene(registro);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        boolean fullscreen = window.isFullScreen();
        window.setResizable(true);
        //scene.getStylesheets().add
        //        (RegistroController.class.getResource("Styles.css").toExternalForm());
        if (fullscreen) {

        }
        window.setScene(scene);
        window.setFullScreen(true);
        window.show();*/
    }


    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }
}
