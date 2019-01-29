package Vistas.sample;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import controlador.Controlador;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.dominio.Usuario;
import modelo.dominio.Video;
import modelo.dominio.VideoList;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;

import static com.itextpdf.text.Font.*;


public class ProfileWindowController {

    public Controlador controlador;
   // public FlowPane panelPrincipal;
    public VBox panelPrincipal;
    public VBox VBoxProfile;
    public VBox VBoxListas;
    public VBox nombreBox;
    public VBox ApellidosBox;
    public VBox birthdayBox;
    public VBox emailBox;
    public Text nombreUserName;
    public VBox cajaInfoPrincipal;
    public VBox auxiliar;
    public VBox vboxEdit;
    public VBox vboxInfo;
    public boolean onEdit;
    public VBox vboxListas;
    public VBox vboxEditLista;
    public HBox cajaSuperior;
    public HBox cajon;
    public ChoiceBox<String> choiceBox;
    public Button buttonPDF;
    public Spinner<Integer> spinner;
    public RadioButton premiumButton;
    public Text NombreUserName;
    private boolean modeEditList;
    private UserWindowController userWindowController;

    Text nombreText;
    Text apellidosText;
    Text birtdayText;
    Text emailText;
    Text premiumText;

    public void inicializar(UserWindowController userWindowController) {
        auxiliar = new VBox();
        this.controlador = Controlador.getInstanciaUnica();
        this.userWindowController = userWindowController;
        Usuario user = controlador.getUsuarioActual();

        if (user == null) {
            System.err.println(" > El usuario es nulo");
        } else {

            NombreUserName.setText(user.getUsername());
            System.out.println("Premium: " + controlador.getUsuarioActual().isPremium());

            SpinnerValueFactory<Integer> valueFactory = //
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 400, 5);
            spinner.setValueFactory(valueFactory);
            spinner.setVisible(true);
            spinner.setEditable(true);
            this.nombreText = new Text(user.getNombre());
            nombreBox.getChildren().add(nombreText);
            this.apellidosText = new Text(user.getApellidos());
            ApellidosBox.getChildren().add(apellidosText);
            this.birtdayText = new Text(user.getFechaNacString());
            birthdayBox.getChildren().add(birtdayText);
            this.emailText = new Text(user.getEmail());
            emailBox.getChildren().add(emailText);
            this.premiumText = new Text("");
            cajaSuperior.getChildren().add(premiumText);
           if (user.isPremium()) {
               premiumButton.fire();
               String filtroActual = controlador.getFiltroActual();
               choiceBox.setValue(filtroActual);
               choiceBox.getItems().addAll("No Filtro", "Impopulares", "Menores", "Mis Listas", "Nombres Largos");
           } else {
               buttonPDF.setVisible(false);
               choiceBox.setValue("No Filtro");
           }
        }

        onEdit = false;
        modeEditList = false;
        mostrarListasDisponibles();

    }

    private void setEditStatus(boolean status) {
        this.onEdit = status;
    }
    public void setEditList(boolean status) {
        this.modeEditList = status;
    }

    private void refrescarInfo(Usuario user) {
        nombreBox.getChildren().remove(nombreText);
        ApellidosBox.getChildren().remove(apellidosText);
        birthdayBox.getChildren().remove(birtdayText);
        emailBox.getChildren().remove(emailText);

        this.nombreUserName.setText(user.getUsername());

        this.nombreText.setText(user.getNombre());
        nombreBox.getChildren().add(nombreText);
        this.apellidosText.setText(user.getApellidos());
        ApellidosBox.getChildren().add(apellidosText);
        this.birtdayText.setText(user.getFechaNacString());
        birthdayBox.getChildren().add(birtdayText);
        this.emailText.setText(user.getEmail());
        emailBox.getChildren().add(emailText);

        if (user.isPremium()) {
           premiumText.setText("Eres Premium :D");
        }

        panelPrincipal.requestLayout();
    }

    public void cambiarPassword(MouseEvent mouseEvent) {
        auxiliar.getChildren().addAll(cajaInfoPrincipal.getChildren());
        cajaInfoPrincipal.getChildren().removeAll();
        cajaInfoPrincipal.getChildren().addAll(panelChangePassowrd());
    }

    public VBox panelChangePassowrd() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefHeight(200);
        box.setPrefWidth(293);

        // Primera Caja
        HBox hbox1 = new HBox();
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefWidth(263);
        hbox1.setPrefHeight(60);

        VBox vBoxh1box1 = new VBox();
        vBoxh1box1.setAlignment(Pos.CENTER);
        vBoxh1box1.setPrefHeight(40);
        vBoxh1box1.setPrefWidth(157);
        Text oldpasswd = new Text("Old Password");
        vBoxh1box1.getChildren().add(oldpasswd);

        VBox vBoxh2box1 = new VBox();
        vBoxh2box1.setAlignment(Pos.CENTER);
        vBoxh2box1.setPrefHeight(40);
        vBoxh2box1.setPrefWidth(157);
        PasswordField oldpwdField = new PasswordField();
        vBoxh2box1.getChildren().add(oldpwdField);
        vBoxh2box1.setPadding(new Insets(0, 12, 0, 0));

        hbox1.getChildren().addAll(vBoxh1box1, vBoxh2box1);

        // Segunda Caja

        HBox hbox2 = new HBox();
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefWidth(263);
        hbox1.setPrefHeight(60);

        VBox vBoxh1box2 = new VBox();
        vBoxh1box2.setAlignment(Pos.CENTER);
        vBoxh1box2.setPrefHeight(40);
        vBoxh1box2.setPrefWidth(157);
        Text newpasswd = new Text("New Password");
        vBoxh1box2.getChildren().add(newpasswd);

        VBox vBoxh2box2 = new VBox();
        vBoxh2box2.setAlignment(Pos.CENTER);
        vBoxh2box2.setPrefHeight(40);
        vBoxh2box2.setPrefWidth(157);
        PasswordField newpwdField = new PasswordField();
        vBoxh2box2.getChildren().add(newpwdField);
        vBoxh2box2.setPadding(new Insets(0, 12, 0, 0));

        hbox2.getChildren().addAll(vBoxh1box2, vBoxh2box2);

        /// Tercera

        HBox hbox3 = new HBox();
        hbox3.setAlignment(Pos.CENTER);
        hbox3.setPrefWidth(263);
        hbox3.setPrefHeight(60);

        VBox vBoxh1box3 = new VBox();
        vBoxh1box3.setAlignment(Pos.CENTER);
        vBoxh1box3.setPrefHeight(40);
        vBoxh1box3.setPrefWidth(157);
        Text repeatPasswd = new Text("Repeat Password");
        vBoxh1box3.getChildren().add(repeatPasswd);

        VBox vBoxh2box3 = new VBox();
        vBoxh2box3.setAlignment(Pos.CENTER);
        vBoxh2box3.setPrefHeight(40);
        vBoxh2box3.setPrefWidth(157);
        PasswordField repeatpwdField = new PasswordField();
        vBoxh2box3.getChildren().add(repeatpwdField);
        vBoxh2box3.setPadding(new Insets(0, 12, 0, 0));
        hbox3.getChildren().addAll(vBoxh1box3, vBoxh2box3);

        // Cuarta

        HBox hbox4 = new HBox();
        hbox4.setAlignment(Pos.CENTER);
        hbox4.setPrefWidth(263);
        hbox4.setPrefHeight(60);

        VBox vBoxh1box4 = new VBox();
        vBoxh1box4.setAlignment(Pos.CENTER);
        vBoxh1box4.setPrefHeight(40);
        vBoxh1box4.setPrefWidth(157);
        Button acceptButton = new Button("Aceptar");
        acceptButton.setOnMouseClicked(ActioEvent  ->  {
            changePassword(oldpwdField.getText(), newpwdField.getText(), repeatpwdField.getText());
        });
        vBoxh1box4.getChildren().add(acceptButton);

        VBox vBoxh2box4 = new VBox();
        vBoxh2box4.setAlignment(Pos.CENTER);
        vBoxh2box4.setPrefHeight(40);
        vBoxh2box4.setPrefWidth(157);
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnMouseClicked(ActionEvent -> {
            volver();
        });
        vBoxh2box4.getChildren().add(cancelButton);
        vBoxh2box4.setPadding(new Insets(0, 12, 0, 0));
        hbox4.getChildren().addAll(vBoxh1box4, vBoxh2box4);

        box.getChildren().addAll(hbox1, hbox2, hbox3, hbox4);

        return box;
    }




    private void changePassword(String oldpasswd, String newpasswd, String reppasswd) {
        Usuario usuarioActual = controlador.getUsuarioActual();
        if (usuarioActual == null) {
            System.err.println("No hay ningun usuario activo");
            System.exit(0);
        } else {
            String passwd = usuarioActual.getPassword();
            System.out.println("old: " + oldpasswd);
            System.out.println("current: " + passwd);
            if (!oldpasswd.equals(passwd)) {
                Notificacion.oldPasswdError();
            } else {
                if (!newpasswd.equals(reppasswd)) {
                    Notificacion.passwdMatchError();
                } else {
                    controlador.changePassword(newpasswd);
                    System.out.println("Passwd changed");
                    Notificacion.changeSuccess(Notificacion.PASSWD);
                    volver();
                }
            }
        }
    }


    public void editarCampos(MouseEvent mouseEvent) {
        if (!onEdit) {
            setEditStatus(true);
            nombreBox.getChildren().clear();
            ApellidosBox.getChildren().clear();
            birthdayBox.getChildren().clear();
            emailBox.getChildren().clear();
            Usuario user = controlador.getUsuarioActual();

            TextField nombreTextField = new TextField();
            nombreBox.getChildren().add(nombreTextField);

            TextField apellidosTextField = new TextField();
            ApellidosBox.getChildren().add(apellidosTextField);

            DatePicker birthDate = new DatePicker();
            birthdayBox.getChildren().add(birthDate);

            TextField emailTextField = new TextField();
            emailBox.getChildren().add(emailTextField);


            VBox vboxAccept = new VBox();
            VBox vboxCancel = new VBox();
            vboxAccept.setAlignment(Pos.CENTER_RIGHT);
            vboxAccept.setPrefHeight(40);
            vboxAccept.setPrefWidth(157);
            vboxAccept.setPadding(new Insets(10, 0, 0, 0));
            Button acceptButton = new Button("Aceptar");
            acceptButton.setAlignment(Pos.CENTER);
            acceptButton.setOnMouseClicked(ActioEvent -> {
                String nombre = nombreTextField.getText();
                String apellidos = apellidosTextField.getText();
                String fecha = user.getUsername();
                if (birthDate.getValue() != null) {
                    fecha = birthDate.getValue().toString();
                }
                String email = emailTextField.getText();
                actualizarCampos(nombre, apellidos, fecha, email);
                vboxInfo.getChildren().remove(vboxAccept);
                vboxEdit.getChildren().remove(vboxCancel);
                nombreBox.getChildren().clear();
                ApellidosBox.getChildren().clear();
                birthdayBox.getChildren().clear();
                emailBox.getChildren().clear();
                setEditStatus(false);
                refrescarInfo(user);
            });

            vboxAccept.getChildren().add(acceptButton);


            vboxCancel.setAlignment(Pos.CENTER);
            vboxCancel.setPrefHeight(40);
            vboxCancel.setPrefWidth(157);
            Button cancelButton = new Button("Cancel");
            cancelButton.setAlignment(Pos.CENTER);
            cancelButton.setOnMouseClicked(ActionEvent -> {
                vboxInfo.getChildren().remove(vboxAccept);
                vboxEdit.getChildren().remove(vboxCancel);
                nombreBox.getChildren().clear();
                ApellidosBox.getChildren().clear();
                birthdayBox.getChildren().clear();
                emailBox.getChildren().clear();
                setEditStatus(false);
            });
            vboxCancel.getChildren().add(cancelButton);
            vboxCancel.setPadding(new Insets(0, 12, 0, 0));

            vboxInfo.getChildren().add(vboxAccept);
            vboxEdit.getChildren().add(vboxCancel);
        }
    }

    private void volver() {
        cajaInfoPrincipal.getChildren().clear();
        cajaInfoPrincipal.getChildren().addAll(auxiliar.getChildren());
    }

    private void actualizarCampos(String nombre, String apellidos, String fecha, String email) {

        Usuario user = controlador.getUsuarioActual();
        if (nombre.equals("")) {
            nombre = user.getNombre();
        }
        if (apellidos.equals("")) {
            apellidos = user.getApellidos();
        }

        if (email.equals("")) {
            email = user.getEmail();
        }

        if (fecha.equals("") || fecha == null) {
            fecha = user.getFechaNacString();
        }

        controlador.modificarDatosUsuario(nombre, apellidos, fecha , email);
        Notificacion.changeSuccess(Notificacion.FIELDS);
        panelPrincipal.requestLayout();

    }

    public void volveraUserView(MouseEvent mouseEvent) throws IOException {
     /*   FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane bpUserView = loader.load();
        Scene userScene = new Scene(bpUserView);
        UserWindowController userController = loader.getController();
        userController.inicializar();
        Stage window = new Stage();
        window.setScene(userScene);
        window.setResizable(true);
        window.show();
        Window currentWindow =  ((Node)mouseEvent.getTarget()).getScene().getWindow();
        currentWindow.fireEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSE_REQUEST));*/
        userWindowController.setInterval(spinner.getValue());
        userWindowController.restoreImages();


    }

    public void eliminarCuenta(MouseEvent mouseEvent) {
        Usuario usuario = controlador.getUsuarioActual();
        boolean delete = Notificacion.deleteAcountQuestion();
        if (delete) {
            VBox box = new VBox();
            Text text = new Text("Escriba su contraseña otra vez");
            PasswordField passwordField = new PasswordField();
            box.getChildren().addAll(text, passwordField);
            Dialog dialog = new Dialog();
            ButtonType acceptDelete = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(acceptDelete, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(box);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == acceptDelete) {
                    dialog.close();
                    System.out.println(passwordField.getText());
                    String password = passwordField.getText();
                    if (!password.equals(usuario.getPassword())) {
                        Notificacion.passwdMatchError();
                        return false;
                    } else {
                        String username = usuario.getUsername();
                        boolean eliminado = controlador.eliminarUsuario();
                        if (eliminado) {
                            Notificacion.notificar("El usuario " + username + " ha sido eliminado");
                            try {
                                volverALogin(mouseEvent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else  {
                            System.err.println("No se ha eliminado el usuario");
                        }

                    }
                    return true;
                }
                return false;
            });
            dialog.show();
            if (dialog.getResult() != null) {
                dialog.close();
            }
        }
    }

    private void volverALogin(MouseEvent mouseEvent) throws IOException {
        BorderPane root = FXMLLoader.load(getClass().getResource("inicio.fxml"));
        FlowPane login =  FXMLLoader.load(getClass().getResource("login.fxml"));
        root.setCenter(login);
        Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Registration Form FXML Application");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
        try {
            ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
        } catch (NullPointerException e) {
            
        }
    }

    public HBox crearCelda(String nombreLista) {
        HBox hbox = new HBox();
        hbox.setPrefWidth(vboxListas.getPrefWidth());
        hbox.setMaxWidth(vboxListas.getMaxWidth());
        hbox.setMinWidth(vboxListas.getMinWidth());
        hbox.setId(nombreLista);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.prefHeight(30);
        hbox.prefWidth(30);
        Text listName = new Text(nombreLista);
        listName.setWrappingWidth(124.21875);
        Separator separator1 = new Separator();
        separator1.setOpacity(0);
        separator1.setOrientation(Orientation.VERTICAL);

        ImageView imgViewEdit = null;
        try {
            imgViewEdit = new ImageView(new Image(new FileInputStream("/Users/morad/Desktop/TDS/workspace/AppVideo/iconos/editar.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imgViewEdit.setFitHeight(25);
        imgViewEdit.setFitWidth(22);
        imgViewEdit.setStyle("-fx-cursor: hand");
        imgViewEdit.setOnMouseClicked(MouseEvent -> {
            if (!modeEditList) {
                editarLista(listName.getText());
                setEditList(true);
            }
        });

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        separator2.prefHeight(200);

        ImageView imgViewDelete = null;
        try {
            imgViewDelete =  new ImageView(new Image(new FileInputStream("/Users/morad/Desktop/TDS/workspace/AppVideo/iconos/eliminar.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imgViewDelete.setFitHeight(22);
        imgViewDelete.setFitWidth(22);
        imgViewDelete.setStyle("-fx-cursor: hand");
        imgViewDelete.setOnMouseClicked(MouseEvent -> {
            boolean confimacion = Notificacion.confirmationQuestion("¿Esta seguro que quiere eliminar esta lista?");
            if (confimacion)
                eliminarLista(listName.getText());
        });
        hbox.getChildren().addAll(listName, separator1, imgViewEdit, separator2, imgViewDelete);
        hbox.setPadding(new Insets(3, 10, 10, 3));
        return hbox;
    }

    private VBox ventanaEdicionLista(String nombreLista) {

        Usuario user = controlador.getUsuarioActual();

        VideoList videoList = user.getListaVideo(nombreLista);

        VBox vox = new VBox();

        HBox hboxNombre = new HBox();
        hboxNombre.setAlignment(Pos.CENTER);
        hboxNombre.setPrefHeight(47);
        hboxNombre.setPrefWidth(305);
        Text textoNombre = new Text("Nombre");
        Separator separador = new Separator();
        separador.setOrientation(Orientation.VERTICAL);
        separador.setPrefHeight(44);
        separador.setPrefWidth(8);
        separador.setOpacity(0);
        TextField textFieldNombre = new TextField(nombreLista);
        hboxNombre.getChildren().addAll(textoNombre, separador, textFieldNombre);
        hboxNombre.setPadding(new Insets(0, 10, 0, 0));

        HBox hboxVideos = new HBox();                   // TODO: Debe añadir
        hboxVideos.setPrefHeight(146);
        hboxVideos.setPrefWidth(305);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(145);
        scrollPane.setPrefWidth(248);

        VBox vboxVideos = new VBox();
        vboxVideos.setPrefWidth(246);
        vboxVideos.setPrefHeight(246);
        // TODO: Dentro del Vbox pondremos cada uno de los textos
        if (videoList != null) {
            for (Video v : videoList.getVideos()) {
                Text textoVideo = new Text(v.getTitulo());
                textoVideo.setWrappingWidth(246);
                vboxVideos.getChildren().add(textoVideo);
            }
        }
        scrollPane.setContent(vboxVideos);

        VBox iconosBox = new VBox();
        iconosBox.setAlignment(Pos.TOP_CENTER);
        iconosBox.setPrefHeight(145);
        iconosBox.setPrefWidth(56);

        Path pathQuit = Paths.get("iconos/quitar.png");
        System.out.println(pathQuit.toAbsolutePath().toString());
        Image imgQuitar = new Image("file:" + pathQuit.toAbsolutePath().toString());
        ImageView imgViewQuitar = new ImageView(imgQuitar);
        imgViewQuitar.setFitHeight(33);
        imgViewQuitar.setFitWidth(28);

        Separator separador1 = new Separator();
        separador1.setOrientation(Orientation.HORIZONTAL);
        separador1.setPrefHeight(17);
        separador1.setPrefWidth(56);
        separador1.setOpacity(0);

        Path pathEdit = Paths.get("iconos/editar.png");
        System.out.println(pathEdit.toAbsolutePath().toString());
        Image imgEdit = new Image("file:" + pathEdit.toAbsolutePath().toString());
        ImageView imgViewEdit = new ImageView(imgEdit);
        imgViewEdit.setFitHeight(33);
        imgViewEdit.setFitWidth(28);
        iconosBox.getChildren().addAll(imgViewQuitar, separador1, imgViewEdit);
        hboxVideos.getChildren().addAll(scrollPane, iconosBox);

        vox.getChildren().addAll(hboxNombre, hboxVideos);
        // TODO: NOS QUEDAMOS POR AQUÍ
        return vox;
    }

    private void eliminarLista(String nombreLista) {
        Iterator<Node> iter = vboxListas.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            if (node instanceof HBox) {
                if (node.getId().equals(nombreLista)) {
                    iter.remove();
                }
            }
        }
        controlador.eliminarVideoList(nombreLista);
        panelPrincipal.requestLayout();
    }


    private void editarLista(String nombreLista) {
        vboxEditLista.getChildren().add(ventanaEdicionLista(nombreLista));
    }

    private void mostrarListasDisponibles() {
        Usuario user = controlador.getUsuarioActual();
        for (VideoList vl : user.getMyVideoLists()) {
            String nombreLista = vl.getNombre();
            HBox box = crearCelda(nombreLista);
            vboxListas.getChildren().add(box);
        }
    }

    public void convertirEnPremium(MouseEvent mouseEvent) {
        controlador.contratarPremium();
        refrescarInfo(controlador.getUsuarioActual());
    }

    public void generarPDF(MouseEvent mouseEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directorio = directoryChooser.showDialog(panelPrincipal.getScene().getWindow());
        File file = new File(directorio + "/listasCanciones.pdf");
        System.out.println(file);
        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Paragraph paragraph = new Paragraph("Listas de Usuario");
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setExtraParagraphSpace((float) 11.5);
            document.add(paragraph);

            for (VideoList vdList : controlador.getUserVideoLists()) {
                Paragraph paragraph1 = new Paragraph(vdList.getNombre());
                List unorderedList = new List(List.UNORDERED);
                for (Video video : vdList.getVideos()) {
                    unorderedList.add(new ListItem(video.getTitulo()));
                }
                paragraph1.add(unorderedList);
                document.add(paragraph1);
            }

            document.close();

        }catch (DocumentException d) {

        }catch (NullPointerException e) {

        } catch (FileNotFoundException e) {
            System.out.println("Directorio Erroneo");
        }

    }
}
