package fr.redspri.tanmiachat.client.connection;

import fr.redspri.tanmiachat.client.window.windows.ChatWindow;
import fr.redspri.tanmiachat.common.MessageObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class TanmiaChatClientWorker extends SimpleChannelInboundHandler<String> {
    private TanmiaChatClientConnector connector;
    final ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void channelRead0(ChannelHandlerContext context, String serialized) throws Exception {
        if (messages.size() < 1) TimeUnit.SECONDS.sleep(1);
        MessageObject object = MessageObject.deserialize(serialized);
        System.out.println(object.getAuthor() + " : " + object.getContent());
        if (messages.size() < 13) messages.add(object.getAuthor() + " : " + object.getContent());
        else {
            messages.remove(0);
            messages.add(object.getAuthor() + " : " + object.getContent());
        }
        Platform.runLater(() -> {
            ChatWindow.getInstance().content.setText("");
            for (String s : messages) {
                ChatWindow.getInstance().content.setText(ChatWindow.getInstance().content.getText() + s + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
