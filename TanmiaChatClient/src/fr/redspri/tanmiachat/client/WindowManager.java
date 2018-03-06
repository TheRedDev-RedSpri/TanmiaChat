package fr.redspri.tanmiachat.client;

import fr.redspri.tanmiachat.client.window.windows.LoginWindow;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class WindowManager extends Application {
    @Getter@Setter(AccessLevel.PRIVATE)
    private static Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TanmiaChat");
        primaryStage.getIcons().add(new Image(LoginWindow.class.getResourceAsStream("TanmiaLogo.png")));
        //primaryStage.initStyle(StageStyle.TRANSPARENT); Bug record
        WindowManager.setPrimaryStage(primaryStage);
        new LoginWindow().open();
    }
}
