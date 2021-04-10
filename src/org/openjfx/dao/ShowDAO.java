package org.openjfx.dao;

import org.openjfx.config.Connector;
import org.openjfx.entity.Movie;
import org.openjfx.entity.Show;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO implements GenericDAO<Show>{

    protected Statement statement = Connector.getInstance().getStatement();

    public ShowDAO() {

    }
    @Override
    public Show create(Show show) {
        try {
            String sql = String.format(
                "INSERT INTO shows(movie_id, showtime) " +
                "VALUES(%d, '%s')",
                show.getMovie().getId(),
                show.getShowtime()
            );
            System.out.println(sql);
            statement.execute(sql);
            return show;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Show update(Show show) {
        return null;
    }

    @Override
    public Show findOne(Object id) {
        try {
            List<Show> allShows = listAll();
            return allShows.stream().filter(show -> show.getId() == (int) id).findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Object id) {

        String sql = "DELETE FROM shows WHERE `show_id`=" + id;
        try {
            statement.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Show> listAll() {

        List<Show> shows = new ArrayList<>();
        try {
            String query = "SELECT * FROM shows LEFT JOIN movies ON `shows`.`movie_id`=`movies`.`movie_id`";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int showId = resultSet.getInt("show_id");
                LocalDateTime showtime =
                    LocalDateTime.of(resultSet.getDate("showtime").toLocalDate(), resultSet.getTime("showtime").toLocalTime());
                Movie movie = new Movie(
                   resultSet.getInt("movie_id") ,
                    resultSet.getString("movie_name"),
                    resultSet.getInt("movie_duration_min"),
                    resultSet.getString("movie_description")
                );
                Show newShow = new Show(showId, movie, showtime);
                shows.add(newShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shows;
    }
}
