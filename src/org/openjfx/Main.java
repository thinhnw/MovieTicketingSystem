package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.entity.Movie;
import org.openjfx.entity.Show;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static final int VIEW_HEIGHT = 720;
    public static final int VIEW_WIDTH = 1280;
    public static Stage mainStage;
    private static Scene scene;

    private static Movie editingMovie;
    private static Show editingShow;

    @Override
    public void start(Stage primaryStage) throws Exception{

        scene = new Scene(loadFXML("home"), VIEW_WIDTH, VIEW_HEIGHT);
        primaryStage.setScene(scene);
        mainStage = primaryStage;

        primaryStage.show();
    }

//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }
//
    private Parent loadFXML(String fxml) throws IOException {
        URL fxmlLocation = getClass().getResource("/fxml/" + fxml + ".fxml");
        System.out.println("url " + fxmlLocation);
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        return loader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Movie getEditingMovie() {
        return editingMovie;
    }
    public static void setEditingMovie(Movie movie) {
        editingMovie = movie;
    }
}
