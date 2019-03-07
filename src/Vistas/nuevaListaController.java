package Vistas;

import controlador.Controlador;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.Usuario;
import modelo.Video;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class nuevaListaController {

    public FlowPane flowpaneNuevaLista;
    public VBox cajaSeleccionVideos;
    public VBox panelLista;
    public ImageView buscarVideoNombres;
    public Button botonBuscar;
    public TextField busquedaField;
    Controlador controlador = Controlador.getInstanciaUnica();
    public TextField nombreField;
    public Button acceptButton;
    public UserWindowController userWindowController;
    private LinkedList<CheckBox> listaCheckBox;

    public void inicializar() {
        listaCheckBox = new LinkedList<CheckBox>();
        this.controlador = Controlador.getInstanciaUnica();
        this.userWindowController = UserWindowController.getInstancia();
        insertarVideosDisponibles();
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
                LinkedList<String> listaTitulos = new LinkedList<>();
                for (Node node : panelLista.getChildren()) {
                    if (node instanceof CheckBox) {
                        if (((CheckBox) node).isSelected()) {
                           String nombreVideo = ((CheckBox) node).getText();
                           listaTitulos.add(nombreVideo);
                        }
                    }
                }
                controlador.registrarVideoList(nombreLista, listaTitulos);
                System.out.println("Exito");
                userWindowController.actualizarVistaListas(nombreLista);  //TODO FALLO
                volver();
            }

        }

    }

    private void buscarVideo(String nombre) {
        panelLista.getChildren().clear();
        insertarVideosDisponibles();
        if (!nombre.equals("")) {
            Iterator<Node> it = panelLista.getChildren().iterator();
            while (it.hasNext()) {
                Node node = it.next();
                String texto = ((CheckBox) node).getText();
                if (!texto.contains(nombre)) {
                   it.remove();
                }
            }

        }
    }

    private void insertarVideosDisponibles() {
        for (Video video : controlador.getVideoes()) {
            CheckBox checkBox = new CheckBox();
            checkBox.setText(video.getTitulo());
            panelLista.getChildren().add(checkBox);
            listaCheckBox.add(checkBox);
        }
    }

    public void cancelar(ActionEvent mouseEvent) throws IOException {
        volver();
    }

    private void volver() {
        userWindowController.restoreImages();
    }

    public void buscar(MouseEvent mouseEvent) {
        if (!busquedaField.getText().equals(""))  {
            buscarVideo(busquedaField.getText());
        } else {

        }
    }

    public void restablecer(MouseEvent mouseEvent) {
        panelLista.getChildren().clear();
        insertarVideosDisponibles();
    }
}
