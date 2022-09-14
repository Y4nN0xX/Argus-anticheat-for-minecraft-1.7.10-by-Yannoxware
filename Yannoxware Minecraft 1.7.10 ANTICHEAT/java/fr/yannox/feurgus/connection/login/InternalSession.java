package fr.yannox.feurgus.connection.login;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.checks.CheckLoader;
import fr.yannox.feurgus.checks.minecraft.CheckAltSession;
import fr.yannox.feurgus.connection.MainThread;
import fr.yannox.feurgus.connection.NettyClient;
import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.connection.packets.in.PacketIn;
import fr.yannox.feurgus.connection.packets.in.PacketInAttachedModuleInfo;
import fr.yannox.feurgus.connection.packets.in.PacketInClassInfo;
import fr.yannox.feurgus.connection.packets.in.PacketInCrash;
import fr.yannox.feurgus.connection.packets.in.PacketInMCScreenshot;
import fr.yannox.feurgus.connection.packets.in.PacketInSignature;
import fr.yannox.feurgus.connection.packets.out.PacketOutLogin;
import fr.yannox.feurgus.keys.KeyUtils;
import fr.yannox.feurgus.utils.NetworkKey;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InternalSession {
    public static InternalSession instance;
    private Thread networkThread;
    private MainThread mainThread;
    private NettyClient client;
    public NetworkKey networkKey;
    private boolean started;
    private List<ACheck> checks;
    private List<Class<? extends ACheck>> checksClasses;
    public Set<PacketIn> inPackets;
    public ExecutorService srv;

    public InternalSession(MainThread mainThread, NettyClient client) {
        instance = this;
        this.mainThread = mainThread;
        this.client = client;
        this.checks = new ArrayList<ACheck>();
        this.checksClasses = new ArrayList<Class<? extends ACheck>>();
        this.inPackets = new HashSet<PacketIn>();
        this.srv = Executors.newSingleThreadExecutor();
    }

    public void generateKeys() {
        this.networkKey = new NetworkKey();
    }

    public List<Class<? extends ACheck>> getChecks() {
        return this.checksClasses;
    }

    public void start() {
        if (this.started) {
            return;
        }
        this.started = true;
        this.initializeClient();
    }

    private void initializeClient() {
        this.networkThread = new Thread(this.client::start);
        this.networkThread.start();
    }

    public void sendLogin() throws Exception {
        this.sendPacket(new PacketOutLogin(this.networkKey.getBaseKey(), CheckAltSession.getPlayerUuid()));
        Thread.sleep(5000L);
        this.registerChecks();
    }

    private void registerChecks() {
        CheckLoader loader = new CheckLoader(this);
        this.checks.addAll(loader.loadChecks());
        this.checks.forEach(c -> this.checksClasses.add(c.getClass()));
        this.registerInboundPackets();
    }

    private void registerInboundPackets() {
        this.inPackets.add(new PacketInAttachedModuleInfo());
        this.inPackets.add(new PacketInClassInfo());
        this.inPackets.add(new PacketInMCScreenshot());
        this.inPackets.add(new PacketInCrash());
        this.inPackets.add(new PacketInSignature());
    }

    public void sendPacket(PacketOut packet) throws Exception {
        if (!this.client.isConnected()) {
            return;
        }
        Channel channel = this.client.channel();
        byte[] gn = packet.generatePacket();
        if (gn == null) {
            return;
        }
        byte[] content = KeyUtils.encrypt(gn);
        String pkt = Base64.getEncoder().encodeToString(content);
        pkt = pkt + "\r\n";
        channel.writeAndFlush((Object)pkt);
    }

    public void unregisterChecks() {
        if (this.checks != null) {
            for (ACheck check : this.checks) {
                check.unload();
            }
            this.checks.clear();
        }
    }

    public void stop() {
        this.unregisterChecks();
        if (this.client != null) {
            this.client.stop();
        }
        if (this.mainThread != null && this.mainThread.isAlive()) {
            this.mainThread.interrupt();
        }
    }
}
