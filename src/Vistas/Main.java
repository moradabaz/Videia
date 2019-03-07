package Vistas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            BorderPane root = FXMLLoader.load(getClass().getResource("inicio.fxml"));
            FlowPane login =  FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));

            root.setCenter(login);
            primaryStage.setTitle("Registration Form FXML Application");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("materialFX.css").toExternalForm());
           // primaryStage.setFullScreen(true);
            primaryStage.setScene(scene);
            primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
