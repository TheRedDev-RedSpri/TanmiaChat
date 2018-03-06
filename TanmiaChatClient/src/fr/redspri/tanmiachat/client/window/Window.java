package fr.redspri.tanmiachat.client.window;

import fr.redspri.tanmiachat.client.WindowManager;
import fr.redspri.tanmiachat.client.window.windows.LoginWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class Window {
    public abstract URL getFXMLRessource();

    public void open() throws IOException {
        Parent fxml = FXMLLoader.load(getFXMLRessource());
        WindowManager.getPrimaryStage().setScene(new Scene(fxml));
        WindowManager.getPrimaryStage().show();
    }

    public void show(Node source) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("TanmiaChat");
        primaryStage.getIcons().add(new Image(LoginWindow.class.getResourceAsStream("TanmiaLogo.png")));
        //primaryStage.initStyle(StageStyle.TRANSPARENT); Bug record
        Parent fxml = FXMLLoader.load(getFXMLRessource());
        primaryStage.setScene(new Scene(fxml));
        primaryStage.show();
        source.getScene().getWindow().hide();
    }


}
