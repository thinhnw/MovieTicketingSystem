package org.openjfx.controller.admin;

import javafx.scene.control.Alert;
import org.openjfx.Main;
import org.openjfx.dao.MovieDAO;
import org.openjfx.entity.Movie;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateMovieController implements Initializable {

    public TextField textMovieName;
    public TextField textMovieDuration;
    public TextArea textMovieDescription;
    public Button buttonBack;
    public Button buttonConfirm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initButtonConfirm();
    }

    private void initButtonConfirm() {
        buttonConfirm.setOnAction(e -> {
            handleConfirm();
        });
    }

    public void handleConfirm() {
        Movie newMovie = new Movie();
        newMovie.setName(textMovieName.getText());
        newMovie.setDescription(textMovieDescription.getText());
        System.out.println(newMovie);
        try {
            newMovie.setDuration(Integer.parseInt(textMovieDuration.getText()));
        } catch (Exception e){
            e.printStackTrace();
        }
        switch (newMovie.isValid()) {
            case -1:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Movie name can not be empty!!!");
                alert.showAndWait();
                break;
            case 0:
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                alert2.setContentText("Movie duration must be a positive integer!!!");
                alert2.showAndWait();
                break;
            case 1:
                MovieDAO movieDAO = new MovieDAO();
                movieDAO.create(newMovie);
                redirectBack();
                break;
        }
    }

    public void redirectBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
            Main.mainStage.setScene(new Scene(root,Main.VIEW_WIDTH,Main.VIEW_HEIGHT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
