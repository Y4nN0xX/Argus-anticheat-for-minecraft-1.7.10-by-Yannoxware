package fr.yannox.feurgus.checks.minecraft;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutMCVariables;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

public class CheckMCVariables
extends ACheck {
    private double reachBlockBase = 4.5;
    private double reachClassic = 3.0;
    private float reachClassicFloat = 3.0f;
    private float espDistance = 2.5f;
    private double caveFinderDistance = 16.0;
    private double velocity = 8000.0;
    private double timer = 1000.0;
    private double bhop = 0.3;
    private float nameTags = 0.02666667f;
    private float nameTags2 = -0.02666667f;
    private float speed = 0.16277136f;
    private double fly = 0.98f;

    public CheckMCVariables(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        Object mc = this.getMinecraft();
        if (mc == null) {
            return;
        }
        double reach = this.getReach(mc);
        double vel = this.getVelocity(mc);
        double nReachBase = this.getNReachBase();
        double nReachMultiplier = this.getNReachMultiplier();
        this.sendPacket(new PacketOutMCVariables(reach, vel, this.reachBlockBase, this.reachClassic, this.reachClassicFloat, this.espDistance, this.caveFinderDistance, this.velocity, this.timer, this.bhop, this.nameTags, this.nameTags2, this.speed, this.fly, nReachBase, nReachMultiplier));
    }

    private double getNReachBase() {
        try {
            Class<?> clazz = Class.forName("fr.yannox.palamod.api.i");
            Field field = clazz.getDeclaredField("a");
            field.setAccessible(true);
            return field.getDouble(null);
        }
        catch (Exception error) {
            return -1.1;
        }
    }

    private double getNReachMultiplier() {
        try {
            Class<?> clazz = Class.forName("fr.yannox.palamod.api.i");
            Field field = clazz.getDeclaredField("b");
            field.setAccessible(true);
            return field.getDouble(null);
        }
        catch (Exception error) {
            return -1.1;
        }
    }

    private double getVelocity(Object mc) {
        try {
            double vx = 2.19;
            Class<?> entityVelocity = Class.forName("net.minecraft.network.play.server.S12PacketEntityVelocity");
            Constructor<?> constructor = entityVelocity.getDeclaredConstructor(Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
            constructor.setAccessible(true);
            int randId = new Random().nextInt(362) + 2;
            Object obj = constructor.newInstance(randId, 2.19, 2.19, 2.19);
            Field fieldX = entityVelocity.getDeclaredField("field_149415_b");
            fieldX.setAccessible(true);
            Field fieldY = entityVelocity.getDeclaredField("field_149416_c");
            fieldY.setAccessible(true);
            Field fieldZ = entityVelocity.getDeclaredField("field_149414_d");
            fieldZ.setAccessible(true);
            int x = fieldX.getInt(obj);
            int y = fieldX.getInt(obj);
            int z = fieldX.getInt(obj);
            return x + y + z;
        }
        catch (Exception error) {
            error.printStackTrace();
            return -101.0;
        }
    }

    private double getReach(Object mc) {
        Object playerController = this.getPlayerController(mc);
        if (playerController == null) {
            return -1.0;
        }
        try {
            Method cz = playerController.getClass().getDeclaredMethod("func_78757_d", new Class[0]);
            cz.setAccessible(true);
            return ((Float)cz.invoke(playerController, new Object[0])).floatValue();
        }
        catch (Exception error) {
            error.printStackTrace();
            return -2.0;
        }
    }

    private Object getPlayerController(Object mc) {
        try {
            Field cz = mc.getClass().getDeclaredField("field_71442_b");
            cz.setAccessible(true);
            return cz.get(mc);
        }
        catch (Exception error) {
            return null;
        }
    }

    private Object getMinecraft() {
        try {
            Class<?> cz = Class.forName("net.minecraft.client.Minecraft");
            Method method = cz.getDeclaredMethod("func_71410_x", new Class[0]);
            method.setAccessible(true);
            return method.invoke(null, new Object[0]);
        }
        catch (Exception error) {
            return null;
        }
    }

    @Override
    public int getRepeatTime() {
        return 271;
    }
}
