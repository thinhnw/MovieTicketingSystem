package org.openjfx.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Connector {
    private static Connector connector;

    public final static String URI = "jdbc:mysql://localhost:3306/movie_ticketing_system";
    public static final String user = "root";
    public static final String password = "";

    Statement statement;

    private Connector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URI, user, password);
            setStatement(conn.createStatement());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connector getInstance() {
        if (connector == null)
            connector = new Connector();
        return connector;
    }
    public Statement getStatement(){
        return statement;
    }
    private void setStatement(Statement statement) {
        this.statement = statement;
    }
}
