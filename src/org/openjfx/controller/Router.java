package org.openjfx.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.openjfx.Main;

public class Router {
    public void redirectToAdminSide() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
        Main.mainStage.setScene(new Scene(root,Main.VIEW_WIDTH,Main.VIEW_HEIGHT));
    }
    public void redirectToClientSide() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/client_1.fxml"));
        Main.mainStage.setScene(new Scene(root, 600, 400));
    }
}
