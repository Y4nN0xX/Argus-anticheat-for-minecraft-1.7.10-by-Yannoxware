package fr.yannox.feurgus.connection;

import fr.yannox.feurgus.connection.NettyClient;
import fr.yannox.feurgus.connection.login.InternalSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

class NettyClient$2
implements ChannelFutureListener {
    NettyClient$2() {
    }

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
}
