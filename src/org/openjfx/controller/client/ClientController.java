package org.openjfx.controller.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.openjfx.Main;
import org.openjfx.dao.MovieDAO;
import org.openjfx.dao.ShowDAO;
import org.openjfx.dao.TicketDAO;
import org.openjfx.entity.Show;
import org.openjfx.entity.Ticket;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientController implements Initializable {

    private static Integer showId = -1;
    private static String seatNumber = "00";
    private static List<Show> availableShows = new ArrayList<>();

    public ChoiceBox<String> choiceOfMovies;
    public DatePicker datePicker;
    public ChoiceBox<LocalTime> choiceOfTime;
    public Button buttonGoToSeating;

    public Pane paneSeating;

    public TextField textCustomerName;
    public TextField textCustomerPhone;
    public Text textMovie;
    public Text textSeatNumber;
    public Text textShowtime;
    public Button buttonPurchase;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (url.toString().contains("client_1")) {
            initChoiceOfMovies();
            datePicker.setOnAction(event -> {
                System.out.println(datePicker.getValue());
                initChoiceOfTime();
            });
            handleBtnGoToSeating();
        } else if (url.toString().contains("client_2")){
            initSeatPicker();
        } else {
            initCheckout();
        }
    }

    private void initCheckout() {
        ShowDAO showDAO = new ShowDAO();
        Show myShow = showDAO.findOne(showId);
        textMovie.setText(myShow.getMovie().getName());
        textSeatNumber.setText(seatNumber);
        textShowtime.setText(myShow.getShowtime().toString());

        buttonPurchase.setOnAction(event -> {
            Ticket soldTicket = new Ticket();
            soldTicket.setSeatNumber(seatNumber);
            soldTicket.setBoughtTime(LocalDateTime.now());
            soldTicket.setPrice(90000.0);
            soldTicket.setShow(myShow);
            soldTicket.setCustomerName(textCustomerName.getText());
            soldTicket.setCustomerPhone(textCustomerPhone.getText());
            TicketDAO ticketDAO = new TicketDAO();
            ticketDAO.create(soldTicket);
            redirectHome();
        });
    }

    private void initSeatPicker() {
        List<Button>  seats = new ArrayList<>();
        int row = 6;
        int col = 8;
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                Button seatIJ = new Button((char) (i + 'A') + "" + (char) (j + '1'));
                seatIJ.setLayoutY(160 + i * 30);
                seatIJ.setLayoutX(24 + j * 70);
                seatIJ.setPrefHeight(25);
                seatIJ.setPrefWidth(59);
                seats.add(seatIJ);
           }
        }
        TicketDAO ticketDAO = new TicketDAO();
        List<Ticket> bookedSeats = ticketDAO.getBookedTicketsOfShow(showId);
        for (Ticket bookedTicket: bookedSeats) {
            String IJ = bookedTicket.getSeatNumber();
            System.out.println(IJ);
            int i = (int) IJ.charAt(0) - (int) 'A';
            int j = (int) IJ.charAt(1) - (int) '1';
            int index = i * col + j;
            seats.get(index).setDisable(true);
        }
        seats.forEach(seat -> {
            seat.setOnAction(event -> {
                seatNumber = seat.getText();
                goToCheckout();
            });
            paneSeating.getChildren().add(seat);
        });
    }

    private void initChoiceOfMovies() {
        MovieDAO movieDAO = new MovieDAO();
        ObservableList<String> movies = FXCollections.observableArrayList();
        movies.addAll(
            movieDAO
                .listAll()
                .stream()
                .map(movie -> movie.getName())
                .collect(Collectors.toList())
        );
        choiceOfMovies.setItems(movies);

        choiceOfMovies
            .getSelectionModel()
            .selectedIndexProperty()
            .addListener((observableValue, number, index) -> {
                System.out.println(choiceOfMovies.getItems().get((Integer) index));
                initChoiceOfTime();
            }
        );
    }

    private void initChoiceOfTime() {

        availableShows = new ArrayList<>();

        ShowDAO showDAO = new ShowDAO();
        List<Show> shows = showDAO.listAll();
        ObservableList<LocalTime> showtimeList = FXCollections.observableArrayList();
        for (Show show: shows) {
            System.out.println(show);
            if (!show.getMovie().getName().equals(choiceOfMovies.getValue())) continue;
            System.out.println("compare date...");
            System.out.println(datePicker.getValue() + " " + datePicker.getValue().getClass());
            System.out.println(show.getShowtime() + " " + show.getShowtime().getClass());
            if (datePicker.getValue().equals(show.getShowtime().toLocalDate())) {
                showtimeList.add(show.getShowtime().toLocalTime());
                availableShows.add(show);
            }
        }
        choiceOfTime.setItems(showtimeList);
        choiceOfTime
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, number, index) -> {
                    showId = availableShows.get((Integer) index).getId();
                });
    }

    public void handleBtnGoToSeating() {
        buttonGoToSeating.setOnAction(event -> {
            if (showId != -1) {
                redirectToSeating();
                System.out.println(showId);
            }
        });
    }

    public void goToCheckout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/client_checkout.fxml"));
            Main.mainStage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void redirectHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
            Main.mainStage.setScene(new Scene(root, 1280, 720));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void redirectToSeating() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/client_2.fxml"));
            Main.mainStage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void redirectToStep1() {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/client_1.fxml"));
            Main.mainStage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
