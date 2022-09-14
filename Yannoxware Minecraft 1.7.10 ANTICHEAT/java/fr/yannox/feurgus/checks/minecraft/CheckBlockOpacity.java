package fr.yannox.feurgus.checks.minecraft;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutBlockOpacity;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CheckBlockOpacity
extends ACheck {
    private final String[] blockNames = new String[]{"bedrock", "bookshelf", "brick_block", "cobblestone", "dirt", "end_stone", "farmland", "leaves", "leaves2", "log", "log2", "netherrack", "nether_brick", "planks", "stone"};

    public CheckBlockOpacity(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    private Object getMinecraft() throws Exception {
        Class<?> cz = Class.forName("net.minecraft.client.Minecraft");
        Method method = cz.getDeclaredMethod("func_71410_x", new Class[0]);
        method.setAccessible(true);
        return method.invoke(null, new Object[0]);
    }

    private Object getRenderGlobal(Object minecraft) throws Exception {
        Class<?> cz = Class.forName("net.minecraft.client.Minecraft");
        Field field = cz.getDeclaredField("field_71438_f");
        field.setAccessible(true);
        return field.get(minecraft);
    }

    private Object getBlockRegistry() throws Exception {
        Class<?> cz = Class.forName("net.minecraft.block.Block");
        Field field = cz.getDeclaredField("field_149771_c");
        field.setAccessible(true);
        return field.get(null);
    }

    private Object getBlockRegistry(Object object, String blockName) throws Exception {
        Class<?> cz = Class.forName("net.minecraft.util.RegistryNamespaced");
        Method method = cz.getDeclaredMethod("func_82594_a", String.class);
        return method.invoke(object, blockName);
    }

    private Object getIIcon(Object block) throws Exception {
        Class<?> cz = Class.forName("net.minecraft.block.Block");
        Method method = cz.getDeclaredMethod("func_149735_b", Integer.TYPE, Integer.TYPE);
        method.setAccessible(true);
        return method.invoke(block, 0, 0);
    }

    private Object getResourceLocation(String blockTexture) {
        String domain = "minecraft";
        String path = blockTexture;
        int domainSeparator = blockTexture.indexOf(58);
        if (domainSeparator >= 0) {
            path = blockTexture.substring(domainSeparator + 1);
            if (domainSeparator > 1) {
                domain = blockTexture.substring(0, domainSeparator);
            }
        }
        String resourcePath = "textures/blocks/" + path + ".png";
        try {
            Class<?> cz = Class.forName("net.minecraft.util.ResourceLocation");
            Object resourceLocation = cz.getDeclaredConstructor(String.class, String.class).newInstance(domain, resourcePath);
            return resourceLocation;
        }
        catch (Exception error) {
            return null;
        }
    }

    private String getIconName(Object icon) {
        try {
            Class<?> cz = Class.forName("net.minecraft.util.IIcon");
            Method method = cz.getDeclaredMethod("func_94215_i", new Class[0]);
            method.setAccessible(true);
            return (String)method.invoke(icon, new Object[0]);
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    private Object getResourceManager(Object mc) {
        try {
            Class<?> cz = Class.forName("net.minecraft.client.Minecraft");
            Method m = cz.getDeclaredMethod("func_110442_L", new Class[0]);
            m.setAccessible(true);
            return m.invoke(mc, new Object[0]);
        }
        catch (Exception error) {
            return null;
        }
    }

    private Object getIResource(Object resourceManager, Object resourceLocation) {
        try {
            Class<?> cz = Class.forName("net.minecraft.client.resources.IResourceManager");
            Class<?> resLoc = Class.forName("net.minecraft.util.ResourceLocation");
            Method m = cz.getDeclaredMethod("func_110536_a", resLoc);
            m.setAccessible(true);
            return m.invoke(resourceManager, resourceLocation);
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    private Object getInputStream(Object object) {
        try {
            Class<?> cz = Class.forName("net.minecraft.client.resources.IResource");
            Method m = cz.getDeclaredMethod("func_110527_b", new Class[0]);
            m.setAccessible(true);
            return m.invoke(object, new Object[0]);
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    private Object imageIoRead(Object inputStream) {
        try {
            Class<?> cz = Class.forName("javax.imageio.ImageIO");
            Class<?> is = Class.forName("java.io.InputStream");
            Method m = cz.getDeclaredMethod("read", is);
            m.setAccessible(true);
            return m.invoke(null, inputStream);
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    private BufferedImage loadTexture(Object icon) {
        try {
            Object mc = this.getMinecraft();
            String iconName = this.getIconName(icon);
            Object resourceLocation = this.getResourceLocation(iconName);
            Object resourceManager = this.getResourceManager(mc);
            Object resource = this.getIResource(resourceManager, resourceLocation);
            Object in = this.getInputStream(resource);
            return (BufferedImage)this.imageIoRead(in);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int[][][] getPixelArray(BufferedImage image) {
        int[][][] array = new int[image.getWidth()][image.getHeight()][4];
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                int px = image.getRGB(x, y);
                array[x][y][0] = px >> 24 & 0xFF;
                array[x][y][1] = px >> 16 & 0xFF;
                array[x][y][2] = px >> 8 & 0xFF;
                array[x][y][3] = px & 0xFF;
            }
        }
        return array;
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            Object minecraft = this.getMinecraft();
            Object renderGlobal = this.getRenderGlobal(minecraft);
            ByteArrayDataOutput bos = ByteStreams.newDataOutput();
            if (renderGlobal != null) {
                int v = 0;
                Object blockRegistry = this.getBlockRegistry();
                bos.writeByte(19);
                for (String blockName : this.blockNames) {
                    Object block = this.getBlockRegistry(blockRegistry, blockName);
                    Object icon = this.getIIcon(block);
                    BufferedImage image = this.loadTexture(icon);
                    int alphaPix = 0;
                    int overallPix = 0;
                    if (image != null) {
                        for (int x = 0; x < image.getHeight(); ++x) {
                            for (int y = 0; y < image.getWidth(); ++y) {
                                alphaPix += this.isAlmostTransparent(image, y, x) ? 1 : 0;
                                ++overallPix;
                            }
                        }
                    }
                    bos.writeByte(v);
                    bos.writeShort((short)alphaPix);
                    bos.writeShort((short)overallPix);
                    v = (byte)(v + 1);
                }
            }
            PacketOutBlockOpacity packet = new PacketOutBlockOpacity(bos.toByteArray());
            this.sendPacket(packet);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    private boolean isAlmostTransparent(BufferedImage img, int a, int b) {
        return img.getRGB(a, b) >> 24 == 0;
    }

    @Override
    public int getRepeatTime() {
        return 247;
    }
}
