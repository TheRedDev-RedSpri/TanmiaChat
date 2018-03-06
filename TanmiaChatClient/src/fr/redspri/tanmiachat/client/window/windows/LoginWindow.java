package fr.redspri.tanmiachat.client.window.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import fr.redspri.tanmiachat.client.connection.TanmiaChatClientConnector;
import fr.redspri.tanmiachat.client.window.Window;
import fr.redspri.tanmiachat.common.ClientSettings;
import fr.redspri.tanmiachat.common.ServerSettings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class LoginWindow extends Window {
    private MouseEvent mouseEvent;
    public JFXButton button;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXTextField ip;
    @FXML
    private Label info;

    @Override
    public URL getFXMLRessource() {
        return getClass().getResource("LoginFXML.fxml");
    }

    public void connect(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
        if (ip.getText().length() < 1 || port.getText().length() < 1 || username.getText().length() < 1 || password.getText().length() < 1) {
            info.setText("Merci de renseigner tout les champs!");
            info.setTextFill(Color.web("#ff0000"));
            info.setVisible(true);
            return;
        }
        if (!isInteger(port.getText())) {
            info.setText("Le port spécifié doit être un nombre");
            info.setTextFill(Color.web("#ff0000"));
            info.setVisible(true);
            return;
        }
        ClientSettings settings = new ClientSettings(username.getText(), new ServerSettings(ip.getText(), Integer.parseInt(port.getText()), password.getText()));
        info.setText("Connection...");
        info.setTextFill(Color.web("#f49542"));
        info.setVisible(true);
            new Thread(() -> {
                try {
                    new TanmiaChatClientConnector(this, null).connect(settings);
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        info.setText(e.getClass().getSimpleName() + ":\n " + e.getMessage());
                        info.setTextFill(Color.web("#ff0000"));
                        info.setVisible(true);
                    });
                }
            }).start();

    }

    private boolean isInteger(String s) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),10) < 0) return false;
        }
        return true;
    }

    public ChatWindow connected() {
        Platform.runLater(() -> {
            info.setText("Connecté!");
            info.setTextFill(Color.web("#0a9b22"));
            info.setVisible(true);
        });
        try {
            TimeUnit.SECONDS.sleep(1);
            Platform.runLater(() -> {
                try {
                    new ChatWindow().show((Node) mouseEvent.getSource());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ChatWindow.getInstance();
    }
}
