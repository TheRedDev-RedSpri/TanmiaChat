package fr.redspri.tanmiachat.client.connection;

import fr.redspri.tanmiachat.common.ServerSettings;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class TanmiaChatClientCreator extends ChannelInitializer<SocketChannel> {
    private SslContext sslContext;
    private ServerSettings settings;

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(sslContext.newHandler(channel.alloc(), settings.getHost(), settings.getPort()));
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new TanmiaChatClientWorker());
    }
}
