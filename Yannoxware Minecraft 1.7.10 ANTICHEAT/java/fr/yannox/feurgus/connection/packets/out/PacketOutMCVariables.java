package fr.yannox.feurgus.connection.packets.out;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutMCVariables
extends PacketOut {
    private final double reach;
    private final double vel;
    private final double reachBlockBase;
    private final double reachClassic;
    private final float reachClassicFloat;
    private final float espDistance;
    private final double caveFinderDistance;
    private final double velocity;
    private final double timer;
    private final double bhop;
    private final float nameTags;
    private final float nameTags2;
    private final float speed;
    private final double fly;
    private final double nReachBase;
    private final double nReachMultiplier;

    public PacketOutMCVariables(double reach, double vel, double reachBlockBase, double reachClassic, float reachClassicFloat, float espDistance, double caveFinderDistance, double velocity, double timer, double bhop, float nameTags, float nameTags2, float speed, double fly, double nReachBase, double nReachMultiplier) {
        this.reach = reach;
        this.vel = vel;
        this.reachBlockBase = reachBlockBase;
        this.reachClassic = reachClassic;
        this.reachClassicFloat = reachClassicFloat;
        this.espDistance = espDistance;
        this.caveFinderDistance = caveFinderDistance;
        this.velocity = velocity;
        this.timer = timer;
        this.bhop = bhop;
        this.nameTags = nameTags;
        this.nameTags2 = nameTags2;
        this.speed = speed;
        this.fly = fly;
        this.nReachBase = nReachBase;
        this.nReachMultiplier = nReachMultiplier;
    }

    @Override
    public int getPacketId() {
        return 32;
    }

    private byte[] generateOutput() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeDouble(this.reach);
        output.writeDouble(this.vel);
        output.writeDouble(this.reachBlockBase);
        output.writeDouble(this.reachClassic);
        output.writeFloat(this.reachClassicFloat);
        output.writeFloat(this.espDistance);
        output.writeDouble(this.caveFinderDistance);
        output.writeDouble(this.velocity);
        output.writeDouble(this.timer);
        output.writeDouble(this.bhop);
        output.writeFloat(this.nameTags);
        output.writeFloat(this.nameTags2);
        output.writeFloat(this.speed);
        output.writeDouble(this.fly);
        output.writeDouble(this.nReachBase);
        output.writeDouble(this.nReachMultiplier);
        return output.toByteArray();
    }

    @Override
    public byte[] toBytes() {
        try {
            byte[] a = this.generateOutput();
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            arrayOutputStream.writeInt(a.length);
            arrayOutputStream.write(a);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            return null;
        }
    }
}
