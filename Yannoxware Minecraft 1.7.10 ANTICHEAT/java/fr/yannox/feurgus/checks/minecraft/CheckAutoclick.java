package fr.yannox.feurgus.checks.minecraft;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import java.lang.reflect.Field;

public class CheckAutoclick
extends ACheck {
    private long lastNano;

    public CheckAutoclick(InternalSession session) {
        super(session);
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            Class mouse = this.getMouse();
            int eventButton = this.getEventButton(mouse);
            long eventTime = this.getMouseNanos(mouse);
            boolean eventState = this.getEventState(mouse);
            if (eventButton == 0 && eventTime != this.lastNano && eventState) {
                long diff = eventTime - this.lastNano;
                diff /= 1000000L;
                this.lastNano = eventTime;
            }
            Thread.sleep(1L);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    private Class getMouse() throws Exception {
        Class<?> cz = Class.forName("org.lwjgl.input.Mouse");
        return cz;
    }

    private long getMouseNanos(Class c) throws Exception {
        Field field = c.getDeclaredField("event_nanos");
        field.setAccessible(true);
        return field.getLong(null);
    }

    private int getEventButton(Class c) throws Exception {
        Field field = c.getDeclaredField("eventButton");
        field.setAccessible(true);
        return field.getInt(null);
    }

    private boolean isGrabbed(Class c) throws Exception {
        Field field = c.getDeclaredField("isGrabbed");
        field.setAccessible(true);
        return field.getBoolean(null);
    }

    private boolean getEventState(Class c) throws Exception {
        Field field = c.getDeclaredField("eventState");
        field.setAccessible(true);
        return field.getBoolean(null);
    }

    @Override
    public int getRepeatTime() {
        return 0;
    }
}
