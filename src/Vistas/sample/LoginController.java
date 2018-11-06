package Vistas.sample;

import controlador.Controlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import modelo.dominio.Usuario;
import modelo.dominio.VideoList;

import static jdk.nashorn.internal.objects.Global.Infinity;


public class LoginController implements Initializable {

    public static final int NUM_MENUS = 4;

    Controlador controlador = Controlador.getInstanciaUnica();

    @FXML
    FlowPane idflowpaneregister;
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
    }

    public VBox recargarParteIzquierda(Usuario usuario) {
        parteIzquierda.maxHeight(Infinity);
        parteIzquierda.maxWidth(Infinity);
        parteIzquierda.minHeight(Infinity);
        parteIzquierda.minWidth(Infinity);
        parteIzquierda.prefHeight(394);
        parteIzquierda.prefWidth(235);

        Text textoListas = new Text("Listas");
        textoListas.setTextAlignment(TextAlignment.CENTER);
        textoListas.setWrappingWidth(186.21875);


        Button buttonMasReproducidas = new Button("Mas Reproducidos");
        buttonMasReproducidas.setTextAlignment(TextAlignment.CENTER);
        buttonMasReproducidas.prefHeight(27);
        buttonMasReproducidas.setMaxWidth(Infinity);
        buttonMasReproducidas.setAlignment(Pos.CENTER);        //buttonMasReproducidas.prefWidth(235);

        Button buttonRecientes = new Button("VideosRecientes");
        buttonRecientes.setTextAlignment(TextAlignment.CENTER);
        buttonRecientes.prefHeight(27);
        buttonRecientes.setMaxWidth(Infinity);
        buttonRecientes.setAlignment(Pos.CENTER);

        parteIzquierda.getChildren().addAll(textoListas, buttonMasReproducidas, buttonRecientes);

        LinkedList<VideoList> listaVidoes = usuario.getMyVideoLists();
        for (VideoList videoL : listaVidoes) {
            Button boton = new Button(videoL.getNombre());
            boton.prefHeight(27);
            boton.setMaxWidth(Infinity);
            boton.setAlignment(Pos.CENTER);
            parteIzquierda.getChildren().add(boton);
        }

        Button anadirButton = new Button("+");
        anadirButton.prefHeight(27);
        anadirButton.setMaxWidth(Infinity);
        anadirButton.setAlignment(Pos.CENTER);
        parteIzquierda.getChildren().add(anadirButton);
        return parteIzquierda;
    }

    public VBox regargarParteSuperior() {
        MenuBar menubar = new MenuBar();
        Menu mFile = new Menu("File");
        MenuItem itemClose = new MenuItem("Close");
        mFile.getItems().add(itemClose);
        Menu mEdit = new Menu("Edit");
        MenuItem itemMinimize = new MenuItem("Minimize");
        mEdit.getItems().add(itemMinimize);
        Menu mHelp = new Menu("Help");
        MenuItem itemAbout = new MenuItem("About");
        mHelp.getItems().add(itemAbout);
        menubar.getMenus().addAll(mFile, mEdit, mHelp);
        parteSuperior.getChildren().add(menubar);
        HBox topBox = new HBox();
        Button logoutButton = new Button("Log out");
        logoutButton.setAlignment(Pos.CENTER_RIGHT);
        logoutButton.setOnAction(EventActionEvent -> {
            logout();
        });
        topBox.getChildren().add(logoutButton);
        parteSuperior.getChildren().add(topBox);
        return parteSuperior;
    }


    public void login(ActionEvent event) {
        String user = userField.getText();
        String passwd = passwdField.getText();
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
        } else
            System.err.println("El usuario " + user + " NO esta logeado");
    }

    public void gotoRegistro(ActionEvent actionEvent) throws IOException {
        BorderPane bp = (BorderPane) idflowpaneregister.getParent();
        FlowPane registro = FXMLLoader.load(getClass().getResource("registro.fxml"));
        bp.setCenter(registro);
    }

    public void gotoUserWindow(ActionEvent event, Controlador controlador) throws IOException {      // TODO: Funcionq que lleva a la ventana USer
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane UserView = loader.load();
        Scene userViewScene = new Scene(UserView);
        UserWindowController userWindowController = loader.getController();
        userWindowController.inicializar(controlador);
        Stage window = new Stage (); // (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(userViewScene);
        window.setFullScreen(true);
        window.show();

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
