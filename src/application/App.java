package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myVideoPlayer.fxml"));
            Parent root  =  loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("My Video Player");
            stage.show();
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){

                @Override
                public void handle(WindowEvent arg0) {
                    Platform.exit();
                    System.exit(0);
                }

            });
        } catch (Exception e) {
          e.printStackTrace();
        }


    }

}
