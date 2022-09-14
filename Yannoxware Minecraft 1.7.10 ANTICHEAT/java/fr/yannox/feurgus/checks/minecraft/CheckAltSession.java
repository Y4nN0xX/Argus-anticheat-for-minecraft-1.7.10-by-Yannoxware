package fr.yannox.feurgus.checks.minecraft;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutAltSession;
import fr.yannox.feurgus.utils.reflections.ClassExistsCheck;
import fr.yannox.feurgus.utils.reflections.GetMethod3;
import fr.yannox.feurgus.utils.reflections.SetFieldAccessible;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CheckAltSession
extends ACheck {
    private final Set<String> usernames = new HashSet<String>();
    private final Set<String> playerUUIDs = new HashSet<String>();

    public CheckAltSession(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            this.checkCurrentSession();
            this.sendPacket(new PacketOutAltSession(this.usernames, this.playerUUIDs));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCurrentSession() throws Exception {
        if (!ClassExistsCheck.exists("net.minecraft.client.Minecraft")) {
            return;
        }
        Object methodGetMinecraft = GetMethod3.getMethod("net.minecraft.client.Minecraft", "func_71410_x");
        Object minecraftInstance = ((Method)methodGetMinecraft).invoke(null, new Object[0]);
        Field fieldSession = minecraftInstance.getClass().getDeclaredField("field_71449_j");
        SetFieldAccessible.setFieldAccessible(fieldSession);
        Object session = fieldSession.get(minecraftInstance);
        Field fieldUsername = session.getClass().getDeclaredField("field_74286_b");
        SetFieldAccessible.setFieldAccessible(fieldUsername);
        String username = (String)fieldUsername.get(session);
        Field fieldPlayerId = session.getClass().getDeclaredField("field_148257_b");
        SetFieldAccessible.setFieldAccessible(fieldPlayerId);
        String playerId = (String)fieldPlayerId.get(session);
        Field fieldToken = session.getClass().getDeclaredField("field_148258_c");
        SetFieldAccessible.setFieldAccessible(fieldToken);
        String token = (String)fieldToken.get(session);
        playerId = playerId.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        this.usernames.add(username);
        this.playerUUIDs.add(playerId);
    }

    @Override
    public int getRepeatTime() {
        return 139;
    }

    public static long[] getPlayerUuid() throws Exception {
        if (!ClassExistsCheck.exists("net.minecraft.client.Minecraft")) {
            UUID random = UUID.randomUUID();
            return new long[]{random.getMostSignificantBits(), random.getLeastSignificantBits()};
        }
        Object methodGetMinecraft = GetMethod3.getMethod("net.minecraft.client.Minecraft", "func_71410_x");
        Object minecraftInstance = ((Method)methodGetMinecraft).invoke(null, new Object[0]);
        Field fieldSession = minecraftInstance.getClass().getDeclaredField("field_71449_j");
        SetFieldAccessible.setFieldAccessible(fieldSession);
        Object session = fieldSession.get(minecraftInstance);
        Field fieldPlayerId = session.getClass().getDeclaredField("field_148257_b");
        SetFieldAccessible.setFieldAccessible(fieldPlayerId);
        String playerId = (String)fieldPlayerId.get(session);
        if (playerId == null) {
            return null;
        }
        playerId = playerId.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        UUID uuid = UUID.fromString(playerId);
        return new long[]{uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()};
    }

    @Override
    public void unload() {
        if (this.usernames != null) {
            this.usernames.clear();
        }
        if (this.playerUUIDs != null) {
            this.playerUUIDs.clear();
        }
        super.unload();
    }
}
