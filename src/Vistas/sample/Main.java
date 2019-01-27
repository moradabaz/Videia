package Vistas.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            BorderPane root = FXMLLoader.load(getClass().getResource("inicio.fxml"));
            FlowPane login =  FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));

         /*    Path path = Paths.get("/iconos/loginBackground.jpg");
             Image image = new Image("file:" + path.toAbsolutePath().toString());
             BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
             root.setBackground(new Background(backgroundImage));
             login.setBackground(new Background(backgroundImage));*/

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
