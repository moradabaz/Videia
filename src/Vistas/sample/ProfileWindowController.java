package Vistas.sample;

import controlador.Controlador;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import modelo.dominio.Usuario;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileWindowController {

    public Controlador controlador;
    public FlowPane panelPrincipal;
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

    public void inicializar(Controlador controlador) {
        auxiliar = new VBox();
        this.controlador = controlador;
        Usuario user = controlador.getUsuarioActual();
        if (user == null) {
            System.err.println(" > El usuario es nulo");
        } else {
            refrescarInfo(user);
        }
        onEdit = false;
    }

    private void setEditStatus(boolean status) {
        this.onEdit = status;
    }

    private void refrescarInfo(Usuario user) {
        nombreUserName.setText(user.getUsername());

        Text nombreText = new Text(user.getNombre());
        nombreBox.getChildren().add(nombreText);

        Text apellidosText = new Text(user.getApellidos());
        ApellidosBox.getChildren().add(apellidosText);

        Text birtdayText = new Text(user.getStringFecha());
        birthdayBox.getChildren().add(birtdayText);

        Text emailText = new Text(user.getEmail());
        emailBox.getChildren().add(emailText);
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
                String fecha;
                try {
                    fecha = birthDate.getValue().toString();         // TODO: Aqui hay algo
                } catch (NullPointerException n) {
                    fecha = user.getStringFecha();
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
                refrescarInfo(user);
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
        Date fechaNac = null;
        Usuario user = controlador.getUsuarioActual();
        if (nombre.equals("")) {
            nombre = user.getNombre();
        }
        if (apellidos.equals("")) {
            apellidos = user.getApellidos();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        try {
            fechaNac = formatter.parse(fecha);
        } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

        if (!fecha.equals("")) {
            if (fechaNac == null)
                fechaNac = user.getFechaNac();
        }
        if (email.equals("")) {
            email = user.getEmail();
        }
        controlador.modificarDatosUsuario(nombre, apellidos, fechaNac, email);
        Notificacion.changeSuccess(Notificacion.FIELDS);
    }

    public void volveraUserView(MouseEvent mouseEvent) throws IOException {
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane UserView = loader.load();
        Scene userViewScene = new Scene(UserView);
        UserWindowController userController = loader.getController();
        userController.inicializar(controlador);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(userViewScene);
        window.setFullScreen(true);
        window.show();*/

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserWindow.fxml"));
        BorderPane bpUserView = loader.load();
        Scene userScene = new Scene(bpUserView);
        UserWindowController userController = loader.getController();
        userController.inicializar(this.controlador);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(userScene);
        window.setResizable(true);
       // window.setFullScreen(true);
        window.show();
    }
}
