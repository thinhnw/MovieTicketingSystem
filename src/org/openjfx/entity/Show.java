package org.openjfx.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Show implements Comparable<Show> {
    public static List<Show> shows = new ArrayList<>();
    private int id;
    private Movie movieById;
    private LocalDateTime showtime;
    public Show() { id = 0;
        movieById = new Movie();
        showtime = LocalDateTime.MIN;
    }

    public Show(int id, Movie movie, LocalDateTime showtime) {
        this.id = id;
        this.movieById = movie;
        this.showtime = showtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movieById;
    }

    public void setMovie(Movie movie) {
        this.movieById = movie;
    }

    public LocalDateTime getShowtime() {
        return showtime;
    }

    public void setShowtime(LocalDateTime showtime) {
        this.showtime = showtime;
    }

    @Override
    public int compareTo(Show show) {
        if (this.showtime.isBefore(show.getShowtime())) return -1;
        if (this.showtime.isAfter(show.getShowtime())) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", movieById=" + movieById +
                ", showtime=" + showtime +
                '}';
    }
}
