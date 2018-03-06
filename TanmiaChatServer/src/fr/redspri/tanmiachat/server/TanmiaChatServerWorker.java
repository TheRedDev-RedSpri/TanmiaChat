package fr.redspri.tanmiachat.server;

import fr.redspri.tanmiachat.common.MessageObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AllArgsConstructor;

import java.net.InetAddress;
import java.util.Objects;

@AllArgsConstructor
public class TanmiaChatServerWorker extends SimpleChannelInboundHandler<String> {
    TanmiaChatServer server;

    static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                future -> {
                    clients.add(ctx.channel());
                    sendMessage(new MessageObject("SERVEUR",  "", "Connexion réussie, bienvenue! NOTE: Pour pouvoir envoyer des messages, le mot de passe doit être correct!"), ctx);
                     }
        );
    }

    public void sendMessage(MessageObject object, ChannelHandlerContext context) {
        System.out.println("Sending \"" + object.getContent() + "\" by \"" + object.getAuthor() + "\"" + " to " + context.channel().id().asLongText());
        context.writeAndFlush(object.serialize() + "\n");
    }

    public void sendMessage(MessageObject object, Channel context) {
        System.out.println("Sending \"" + object.getContent() + "\" by \"" + object.getAuthor() + "\"" + " to " + context.id().asLongText());
        context.writeAndFlush(object.serialize() + "\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext context, String serialized) {
        System.out.println("CC");
        if (serialized == "leave") {
            context.close();
            return;
        }

        MessageObject object = MessageObject.deserialize(serialized);
        System.out.println("Receiving \"" + object.getContent() + "\" by \"" + object.getAuthor() + "\"" + " from " + context.channel().id().asLongText());

        if (!Objects.equals(object.getPassword(), server.getServerSettings().getPass())) {
            sendMessage(new MessageObject("SERVEUR", "", "Mot de passe incorrect, le message n'est pas envoyé!"), context);
            return;
        }
        clients.forEach(c -> {
            if (c != context.channel());
            sendMessage(object, c);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
