package Vistas.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            BorderPane root = FXMLLoader.load(getClass().getResource("inicio.fxml"));
            FlowPane login =  FXMLLoader.load(getClass().getResource("login.fxml"));
            root.setCenter(login);
            primaryStage.setTitle("Registration Form FXML Application");
            Scene scene = new Scene(root);
            primaryStage.setFullScreen(true);
            primaryStage.setScene(scene);
            primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
