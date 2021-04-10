package org.openjfx.entity;

public class Movie {
    private int id;
    private String name;
    private int durationInMinutes;
    private String description;

    public Movie() {
        name = "";
        description = "";
        durationInMinutes = 0;
    }

    public Movie(int id, String name, int durationInMinutes, String description) {
        this.id = id;
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDuration(int duration) {
        this.durationInMinutes = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration in minutes=" + durationInMinutes +
                ", description='" + description + '\'' +
                '}';
    }

    public int isValid() {
        if (name.length() == 0) return -1;
        if (durationInMinutes == 0) return 0;
        return 1;
    }
}
