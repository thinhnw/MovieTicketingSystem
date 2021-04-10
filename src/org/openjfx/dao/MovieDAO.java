package org.openjfx.dao;

import org.openjfx.config.Connector;
import org.openjfx.entity.Movie;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements GenericDAO<Movie> {

    protected Statement statement = Connector.getInstance().getStatement();

    public MovieDAO() {}

    @Override
    public Movie create(Movie movie) {
        try {
            String sql = String.format(
                "INSERT INTO movies(movie_name, movie_duration_min, movie_description) " +
                "VALUES('%s', %d, '%s')",
                movie.getName(), movie.getDurationInMinutes(), movie.getDescription()
            );
            System.out.println(sql);
            statement.execute(sql);
            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Movie update(Movie movie) {

        try {
            String sql =
                "UPDATE movies " +
                "SET `movie_name`='" + movie.getName() + "', `movie_description`='" + movie.getDescription() + "' " +
                "WHERE `movie_id`=" + movie.getId();
            System.out.println(sql);
            statement.execute(sql);
            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Movie findOne(Object id) {
        String sql = "SELECT * FROM movies WHERE `movie_id`=" + id;
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
                return new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("movie_name"),
                    rs.getInt("movie_duration_min"),
                    rs.getString("movie_description")
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Movie findMovieByName(String name) {
        String sql = String.format("SELECT * FROM movies WHERE `movie_name`='%s'", name);
        System.out.println(sql);
        try {
           ResultSet rs = statement.executeQuery(sql);
           while (rs.next()) {
               if (rs.getString("movie_name").equals(name))
                   return new Movie(
                       rs.getInt("movie_id"),
                       rs.getString("movie_name"),
                       rs.getInt("movie_duration_min"),
                       rs.getString("movie_description")
                   );
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Object id) {
        String sql = "DELETE FROM movies WHERE movie_id=" + id;
        try {
            statement.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Movie> listAll() {
        List<Movie> moviesList = new ArrayList<>();
        try {
            String query = "SELECT * FROM movies";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                moviesList.add(
                    new Movie(
                        resultSet.getInt("movie_id"),
                        resultSet.getString("movie_name"),
                        resultSet.getInt("movie_duration_min"),
                        resultSet.getString("movie_description")
                    )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesList;
    }
}
