package org.openjfx.controller.admin;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import org.openjfx.Main;
import org.openjfx.dao.MovieDAO;
import org.openjfx.dao.ShowDAO;
import org.openjfx.dao.TicketDAO;
import org.openjfx.entity.Movie;
import org.openjfx.entity.Show;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.entity.Ticket;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public TableView<Movie> tableMovies;
    public TableColumn<Movie, Integer> columnMovieId;
    public TableColumn<Movie, String> columnMovieName;
    public TableColumn<Movie, Integer> columnMovieDuration;
    public TableColumn<Movie, String> columnMovieDescription;
    public TableColumn<Movie, Movie> columnMovieEdit;
    public TableColumn<Movie, Movie> columnMovieDelete;

    public TableView<Show> tableShows;
    public TableColumn<Show, Integer> columnShowId;
    public TableColumn<Show, String> columnShowMovie;
    public TableColumn<Show, LocalDateTime> columnShowTime;
    public TableColumn<Show, Integer> columnShowDuration;
    public TableColumn<Show, Show> columnShowDelete;

    public Button buttonCreateMovie;
    public Button buttonCreateShow;

    public TableView<Ticket> tableTickets;
    public TableColumn<Ticket, Integer> columnTicketId;
    public TableColumn<Ticket, String> columnTicketMovie;
    public TableColumn<Ticket, String> columnTicketSeat;
    public TableColumn<Ticket, LocalDateTime> columnTicketShowtime;
    public TableColumn<Ticket, String> columnTicketCustomerName;
    public TableColumn<Ticket, String> columnTicketCustomerPhone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initButtonCreateMovie();
        initButtonCreateShow();

        initTableMovies();
        initTableShows();
        initTableTickets();
    }

    private void initTableTickets() {
        TicketDAO ticketDAO = new TicketDAO();
        List<Ticket> tickets = ticketDAO.listAll();

        columnTicketId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTicketMovie
                .setCellValueFactory(param ->
                        new ReadOnlyStringWrapper(param.getValue().getShow().getMovie().getName())
                );
        columnTicketSeat.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        columnTicketShowtime
                .setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper(param.getValue().getShow().getShowtime())
                );
        columnTicketCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnTicketCustomerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        ObservableList<Ticket> ticketObservableList = FXCollections.observableArrayList();
        ticketObservableList.addAll(tickets);
        tableTickets.setItems(ticketObservableList);
    }

    private void initTableShows() {
        columnShowId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnShowMovie
            .setCellValueFactory(param ->
                new ReadOnlyStringWrapper(param.getValue().getMovie().getName())
            );
        columnShowTime.setCellValueFactory(new PropertyValueFactory<>("showtime"));
        columnShowDuration.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper(param.getValue().getMovie().getDurationInMinutes())
        );
        columnShowDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnShowDelete.setCellFactory(param -> new TableCell<Show, Show>(){
            private final Hyperlink deleteBtn = new Hyperlink("x");
            @Override
            protected void updateItem(Show show, boolean empty) {
                super.updateItem(show, empty);
                setGraphic(empty ? null : deleteBtn);
                deleteBtn.setOnAction(event -> {
                    ShowDAO showDAO = new ShowDAO();
                    if (showDAO.delete(show.getId())) {
                        getTableView().getItems().remove(show);
                    }
                });
            }
        });
        ObservableList<Show> showObservableList = FXCollections.observableArrayList();
        ShowDAO showDAO = new ShowDAO();
        showObservableList.addAll(showDAO.listAll());
        tableShows.setItems(showObservableList);
    }

    private void initButtonCreateShow() {

        buttonCreateShow.setOnAction(e -> {
            redirectToCreateNewShow();
        });
    }

    private void initTableMovies() {

        columnMovieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnMovieName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnMovieDuration.setCellValueFactory(new PropertyValueFactory<>("durationInMinutes"));
        columnMovieDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        columnMovieDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        columnMovieDelete.setCellFactory(param -> new TableCell<Movie, Movie>(){
            private final Hyperlink deleteBtn = new Hyperlink("x");
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                setGraphic(empty ? null : deleteBtn);
                deleteBtn.setOnAction(event -> {
                    MovieDAO movieDAO = new MovieDAO();
                    if (movieDAO.delete(movie.getId())) {
                        getTableView().getItems().remove(movie);
                    }
                });
            }
        });

        columnMovieEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        columnMovieEdit.setCellFactory(param -> new TableCell<Movie, Movie>() {
            private final Hyperlink editBtn = new Hyperlink("edit");

            @Override
            protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editBtn);
                editBtn.setOnAction(event -> {
                    redirectToEditMovie(item);
                });
            }
        });


        ObservableList<Movie> movieObservableList = FXCollections.observableArrayList();
        MovieDAO movieDAO = new MovieDAO();
        movieObservableList.addAll(movieDAO.listAll());
        tableMovies.setItems(movieObservableList);
    }

    private void redirectToEditMovie(Movie item) {
        try {
            Main.setEditingMovie(item);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/editMovie.fxml"));
            Main.mainStage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initButtonCreateMovie() {
       buttonCreateMovie.setOnAction(e -> {
           redirectToCreateNewMovie();
       });
    }
    private void redirectToCreateNewMovie() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/createMovie.fxml"));
            Main.mainStage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void redirectToCreateNewShow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/createShow.fxml"));
            Main.mainStage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void redirectBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
            Main.mainStage.setScene(new Scene(root,Main.VIEW_WIDTH,Main.VIEW_HEIGHT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
