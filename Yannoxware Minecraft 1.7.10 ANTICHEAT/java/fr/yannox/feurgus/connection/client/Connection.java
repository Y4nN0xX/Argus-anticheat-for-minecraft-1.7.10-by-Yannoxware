package fr.yannox.feurgus.connection.client;

import io.netty.channel.Channel;

public interface Connection {
    public void connected(Channel var1);

    public void disconnected(Channel var1);

    public void handle(String var1);

    public void exception(Throwable var1);
}
