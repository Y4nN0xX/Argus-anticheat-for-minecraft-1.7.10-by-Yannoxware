package fr.yannox.feurgus.connection.client;

import fr.yannox.feurgus.connection.client.Connection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectionHandler
extends SimpleChannelInboundHandler<String> {
    private final Connection connection;

    public ConnectionHandler(Connection connection) {
        this.connection = connection;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (this.connection != null) {
            this.connection.connected(ctx.channel());
        }
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.connection != null) {
            this.connection.disconnected(ctx.channel());
        }
    }

    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (this.connection != null) {
            this.connection.handle(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause != null) {
            cause.printStackTrace();
        }
        if (!ctx.channel().isActive()) {
            return;
        }
        if (this.connection != null) {
            this.connection.exception(cause);
        }
        ctx.channel().close();
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.channelRead0(channelHandlerContext, (String)object);
    }
}
