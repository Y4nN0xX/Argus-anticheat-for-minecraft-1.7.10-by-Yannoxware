package fr.yannox.feurgus.connection;

import fr.yannox.feurgus.connection.client.ClientHandler;
import fr.yannox.feurgus.connection.client.ConnectionHandler;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

class NettyClient$1
extends ChannelInitializer<SocketChannel> {
    NettyClient$1() {
    }

    public void initChannel(SocketChannel ch) throws Exception {
        try {
            ch.config().setOption(ChannelOption.IP_TOS, (Object)24);
        }
        catch (ChannelException channelException) {
            // empty catch block
        }
        ch.config().setAllocator((ByteBufAllocator)PooledByteBufAllocator.DEFAULT);
        ch.pipeline().addLast(new ChannelHandler[]{new IdleStateHandler(120, 0, 0)});
        ch.pipeline().addLast("frame", (ChannelHandler)new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        ch.pipeline().addLast("frame-decoder", (ChannelHandler)new StringDecoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast("frame-encoder", (ChannelHandler)new StringEncoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast("handler", (ChannelHandler)new ConnectionHandler(new ClientHandler()));
    }

    public void initChannel(Channel channel) throws Exception {
        this.initChannel((SocketChannel)channel);
    }
}
