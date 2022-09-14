package fr.yannox.feurgus.connection;

import fr.yannox.feurgus.connection.NettyClient;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.utils.threading.ThreadStarter;

public class MainThread
extends Thread {
    public static Class<?> thisClass;

    public MainThread() {
        thisClass = this.getClass();
        ThreadStarter.start(this);
    }

    @Override
    public void run() {
        NettyClient nettyClient = new NettyClient();
        InternalSession session = new InternalSession(this, nettyClient);
        session.start();
    }
}
