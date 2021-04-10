package org.openjfx.controller.admin;

import org.openjfx.Main;
import org.openjfx.dao.MovieDAO;
import org.openjfx.dao.ShowDAO;
import org.openjfx.entity.Movie;
import org.openjfx.entity.Show;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CreateShowController implements Initializable {

    public Pane rootPane;
    public TextField textMovieName;
    public DatePicker datePicker;
    public Button btnConfirm;

    public Spinner<Integer> hourSpinner;
    public Spinner<Integer> minuteSpinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTimePicker();
        initConfirmBtn();
    }

    private void initConfirmBtn() {
        btnConfirm.setOnAction(e -> {
            handleConfirm();
        });
    }

    private void handleConfirm() {
        Show show = new Show();
        MovieDAO movieDAO = new MovieDAO();
        System.out.println(textMovieName.getText());
        Movie movie = movieDAO.findMovieByName(textMovieName.getText());

        if (movie == null) {
            alertMessage("Can not find movie with name '" + textMovieName.getText() + "'" );
            return;
        }

        show.setMovie(movie);
        try {
            LocalDate showDate = datePicker.getValue();
            LocalDateTime showTime =
                LocalDateTime.of(
                    showDate,
                    LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue())
                );
            if (showTime.isBefore(LocalDateTime.now())) {
               alertMessage("Showtime cannot be in the past!!!");
               return;
            }
            ShowDAO showDAO = new ShowDAO();
            show.setShowtime(showTime);
            show.setMovie(movie);
            if (checkValid(show)) {
                showDAO.create(show);
                redirectBack();
            } else {
                alertMessage("New showtime cannot be overlap with others!!!");
            }
        } catch (Exception e) {
            alertMessage("Showtime is not valid!!!");
        }
    }

    public void initTimePicker() {
        hourSpinner = new Spinner<>();
        minuteSpinner = new Spinner<>();
        int minHour = 0;
        int maxHour = 23;
        int initialHour = 12;
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory
                .IntegerSpinnerValueFactory(minHour, maxHour, initialHour);
        hourSpinner.setValueFactory(valueFactory);

        hourSpinner.setPrefWidth(40);
        hourSpinner.setPrefHeight(40);
        hourSpinner.setLayoutX(310);
        hourSpinner.setLayoutY(220);

        int minMin= 0;
        int maxMin = 59;
        int initialMin = 0;
        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory
                .IntegerSpinnerValueFactory(minMin, maxMin, initialMin);
        minuteSpinner.setValueFactory(minuteValueFactory);

        hourSpinner.setPrefWidth(60);
        hourSpinner.setPrefHeight(40);
        hourSpinner.setLayoutX(410);
        hourSpinner.setLayoutY(220);
        minuteSpinner.setPrefWidth(60);
        minuteSpinner.setPrefHeight(40);
        minuteSpinner.setLayoutX(480);
        minuteSpinner.setLayoutY(220);
        rootPane.getChildren().add(hourSpinner);
        rootPane.getChildren().add(minuteSpinner);
    }

    public void redirectBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
            Main.mainStage.setScene(new Scene(root,Main.VIEW_WIDTH,Main.VIEW_HEIGHT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkValid(Show myShow) {
        // check if myShow is overlap with others
        ShowDAO showDAO = new ShowDAO();
        List<Show> shows = showDAO.listAll();
        for (Show show : shows) {
            // show <= myShow <= show.end()
            if ((myShow.getShowtime().isAfter(show.getShowtime()) || myShow.getShowtime().equals(show.getShowtime()))
                    && (myShow.getShowtime().isBefore(show.getShowtime().plusMinutes(show.getMovie().getDurationInMinutes()))
                        || myShow.getShowtime().equals(show.getShowtime().plusMinutes(show.getMovie().getDurationInMinutes())))) {
                return false;
            }
            // myShow <= show.begin() <= myShow.end()
            if ((show.getShowtime().isAfter(myShow.getShowtime()) || show.getShowtime().equals(myShow.getShowtime()))
                && (show.getShowtime().isBefore(myShow.getShowtime().plusMinutes(myShow.getMovie().getDurationInMinutes()))
                    || show.getShowtime().equals(myShow.getShowtime().plusMinutes(myShow.getMovie().getDurationInMinutes()))))
                return false;
        }
        return true;
    }

    private void alertMessage(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}
