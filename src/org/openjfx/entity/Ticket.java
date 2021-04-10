package org.openjfx.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    private int id;
    private Show show;
    private String seatNumber;
    private Double price;
    private LocalDateTime boughtTime;
    private String customerName;
    private String customerPhone;

    public Ticket() {
    }

    public Ticket(int id, Show show, String seatNumber, Double price, LocalDateTime boughtTime, String customerName, String customerPhone) {
        this.id = id;
        this.show = show;
        this.seatNumber = seatNumber;
        this.price = price;
        this.boughtTime = boughtTime;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getBoughtTime() {
        return boughtTime;
    }

    public String getBoughtTimeInSQLFormat() {
        return boughtTime.format(DateTimeFormatter.ISO_DATE_TIME).replace("T", " ");
    }

    public void setBoughtTime(LocalDateTime boughtTime) {
        this.boughtTime = boughtTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    @Override
    public String toString() {
        return "BookedTicket{" +
                "id=" + id +
                ", show=" + show +
                ", seatNumber='" + seatNumber + '\'' +
                ", price=" + price +
                ", boughtTime=" + boughtTime +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                '}';
    }
}
