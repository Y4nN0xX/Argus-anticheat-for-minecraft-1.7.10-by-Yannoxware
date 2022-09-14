package fr.yannox.feurgus.connection;

import fr.yannox.feurgus.connection.client.ClientHandler;
import fr.yannox.feurgus.connection.client.ConnectionHandler;
import fr.yannox.feurgus.connection.login.InternalSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class NettyClient {
    private static final String host = "feurgus.yannox-pvp.fr";
    private static final int port = 6606;
    private final Timer timer;
    private Channel c;
    private Bootstrap bootstrap;
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public NettyClient() {
        this.timer = new Timer();
    }

    public void start() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            this.bootstrap = new Bootstrap();
            this.bootstrap.group((EventLoopGroup)workerGroup);
            this.bootstrap.channel(NioSocketChannel.class);
            this.bootstrap.option(ChannelOption.SO_KEEPALIVE, (Object)true);
            this.bootstrap.option(ChannelOption.SO_SNDBUF, (Object)65535);
            this.bootstrap.option(ChannelOption.SO_KEEPALIVE, (Object)true);
            this.bootstrap.handler((ChannelHandler)new ChannelInitializer<SocketChannel>(){

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
            });
            this.connect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() throws Exception {
        ChannelFuture f = this.bootstrap.connect(host, 6606);
        f.addListener((GenericFutureListener)new ChannelFutureListener(){

            public void operationComplete(ChannelFuture future) {
                if (NettyClient.this.stopped.get()) {
                    return;
                }
                if (!future.isSuccess()) {
                    future.channel().close();
                    NettyClient.this.c = null;
                    InternalSession.instance.unregisterChecks();
                    NettyClient.this.bootstrap.connect(NettyClient.host, 6606).addListener((GenericFutureListener)this);
                    InternalSession.instance.generateKeys();
                    try {
                        InternalSession.instance.sendLogin();
                    }
                    catch (Exception error) {
                        error.printStackTrace();
                    }
                } else {
                    NettyClient.this.c = future.channel();
                    InternalSession.instance.unregisterChecks();
                    InternalSession.instance.generateKeys();
                    try {
                        InternalSession.instance.sendLogin();
                    }
                    catch (Exception error) {
                        error.printStackTrace();
                    }
                    this.addCloseDetectListener(NettyClient.this.c);
                }
            }

            private void addCloseDetectListener(Channel channel) {
                channel.closeFuture().addListener((GenericFutureListener)((ChannelFutureListener)future -> NettyClient.this.scheduleConnect(5000L)));
            }

            public void operationComplete(Future future) throws Exception {
                this.operationComplete((ChannelFuture)future);
            }
        });
    }

    private void scheduleConnect(long millis) {
        if (this.stopped.get()) {
            return;
        }
        this.timer.schedule(new TimerTask(){

            @Override
            public void run() {
                try {
                    NettyClient.this.connect();
                }
                catch (Exception error) {
                    error.printStackTrace();
                }
            }
        }, millis);
    }

    public Channel channel() {
        return this.c;
    }

    public boolean isConnected() {
        return this.c != null && this.c.isOpen();
    }

    public void stop() {
        try {
            if (this.c == null) {
                return;
            }
            this.stopped.set(true);
            if (this.c.isOpen()) {
                this.c.close();
            }
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }
}
