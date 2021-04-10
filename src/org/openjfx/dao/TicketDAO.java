package org.openjfx.dao;

import org.openjfx.config.Connector;
import org.openjfx.entity.Movie;
import org.openjfx.entity.Show;
import org.openjfx.entity.Ticket;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements GenericDAO<Ticket>{

    protected Statement statement = Connector.getInstance().getStatement();
    @Override
    public Ticket create(Ticket ticket) {
        try {
            String sql = String.format(
                    "INSERT INTO booked_tickets(show_id, seat_number, ticket_price, bought_time, customer_name, customer_phone) " +
                    "VALUES(%d, '%s', 90000.0, '%s', '%s', '%s')",
                    ticket.getShow().getId(),
                    ticket.getSeatNumber(),
                    ticket.getBoughtTimeInSQLFormat(),
                    ticket.getCustomerName(),
                    ticket.getCustomerPhone()
            );
            System.out.println(sql); statement.execute(sql); return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Ticket update(Ticket ticket) {
        return null;
    }

    @Override
    public Ticket findOne(Object id) {
        return null;
    }

    @Override
    public boolean delete(Object id) {
        return false;
    }

    @Override
    public List<Ticket> listAll() {
        List<Ticket> tickets = new ArrayList<>();
        try {
            String sql = "SELECT * FROM `booked_tickets` LEFT JOIN `shows` ON `booked_tickets`.`show_id`=`shows`.`show_id` LEFT JOIN `movies` ON `shows`.`movie_id`=`movies`.`movie_id`";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int showId = rs.getInt("show_id");
                LocalDateTime showtime =
                        LocalDateTime.of(rs.getDate("showtime").toLocalDate(), rs.getTime("showtime").toLocalTime());
                Movie movie = new Movie(
                        rs.getInt("movie_id") ,
                        rs.getString("movie_name"),
                        rs.getInt("movie_duration_min"),
                        rs.getString("movie_description")
                );
                Show show = new Show(
                        rs.getInt("show_id"),
                        movie,
                        showtime
                );
                LocalDateTime boughTime =
                        LocalDateTime.of(rs.getDate("bought_time").toLocalDate(), rs.getTime("bought_time").toLocalTime());
                Ticket ticket = new Ticket(
                        rs.getInt("ticket_id"),
                        show,
                        rs.getString("seat_number"),
                        90000.0,
                        boughTime,
                        rs.getString("customer_name"),
                        rs.getString("customer_phone")
                );
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> getBookedTicketsOfShow(Object showId) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            String sql = "SELECT * FROM `booked_tickets` " +
                    "WHERE `booked_tickets`.`show_id`=" + showId    ;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Ticket tk = new Ticket();
                tk.setSeatNumber(rs.getString("seat_number"));
                tickets.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return tickets;
    }
}
