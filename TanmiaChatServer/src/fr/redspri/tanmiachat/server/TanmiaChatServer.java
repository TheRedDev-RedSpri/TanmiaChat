package fr.redspri.tanmiachat.server;

import fr.redspri.tanmiachat.common.ServerSettings;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Getter;

class TanmiaChatServer {
    @Getter
    private final ServerSettings serverSettings;
    private final boolean running;

    private TanmiaChatServer(ServerSettings serverSettings) {
        this.serverSettings = serverSettings;
        running = false;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Merci de spécifier l'ip, le port ainsi que le mot de passe du serveur");
            return;
        }
        try {
            new TanmiaChatServer(new ServerSettings(args[0], Integer.parseInt(args[1]), args[2])).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() throws Exception {
        if (running) return;
        SelfSignedCertificate ssl = new SelfSignedCertificate();
        SslContext sslContext = SslContextBuilder.forServer(ssl.certificate(), ssl.privateKey()).build(); //on crypte les données
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new TanmiaChatServerCreator(sslContext, this));

            boot.bind(serverSettings.getHost(), serverSettings.getPort()).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
