package fr.redspri.tanmiachat.client.connection;

import fr.redspri.tanmiachat.client.window.windows.ChatWindow;
import fr.redspri.tanmiachat.client.window.windows.LoginWindow;
import fr.redspri.tanmiachat.common.ClientSettings;
import fr.redspri.tanmiachat.common.MessageObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@AllArgsConstructor
public class TanmiaChatClientConnector {
    private static ClientSettings settings;


    private LoginWindow loginWindow;
    public static Channel ch = null;
    public static ChannelFuture future = null;
    ChatWindow chatWindow;

    public void connect(ClientSettings settings) throws Exception {
        TanmiaChatClientConnector.settings = settings;
        final SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap boot = new Bootstrap();
            boot.group(group)
            .channel(NioSocketChannel.class)
            .handler(new TanmiaChatClientCreator(sslContext, settings.getSettings(), this));
            ch = boot.connect(settings.getSettings().getHost(), settings.getSettings().getPort()).sync().channel();
            chatWindow = loginWindow.connected();
            future = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) break;
                future = ch.writeAndFlush(new MessageObject(settings.getName(), settings.getSettings().getPass(), line).serialize() + "\r\n");
                if (future != null) {
                    future.sync();
                }
            }


        } finally {
            group.shutdownGracefully();
        }
    }

    public static void sendMessage(String s) {
        future = ch.writeAndFlush(new MessageObject(settings.getName(), settings.getSettings().getPass(), s).serialize() + "\r\n");
    }
}
