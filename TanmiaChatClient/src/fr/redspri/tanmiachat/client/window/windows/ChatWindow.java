package fr.redspri.tanmiachat.client.window.windows;

import com.jfoenix.controls.JFXTextArea;
import fr.redspri.tanmiachat.client.connection.TanmiaChatClientConnector;
import fr.redspri.tanmiachat.client.window.Window;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

public class ChatWindow extends Window {
    @Getter@Setter
    private static ChatWindow instance;
    public Label content;
    public JFXTextArea tosend;

    public ChatWindow() {
        instance = this;
    }

    @Override
    public URL getFXMLRessource() {
        return getClass().getResource("ChatFXML.fxml");
    }

    public void send(MouseEvent mouseEvent) {
        TanmiaChatClientConnector.sendMessage(tosend.getText());
        tosend.setText("");
    }

    @FXML
    protected void initialize() {
        ChatWindow.setInstance(this);
    }
}
