package fr.yannox.feurgus.checks.minecraft;

import fr.yannox.feurgus.utils.Callback;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CheckMCScreenshot {
    private Object getMinecraft() throws Exception {
        Class<?> cz = Class.forName("net.minecraft.client.Minecraft");
        Method method = cz.getDeclaredMethod("func_71410_x", new Class[0]);
        method.setAccessible(true);
        return method.invoke(null, new Object[0]);
    }

    private Object addScheduledTask(Object cz, Object object) throws Exception {
        Class<?> cz1 = Class.forName("java.lang.Runnable");
        Method method = cz.getClass().getMethod("func_152344_a", cz1);
        method.setAccessible(true);
        return method.invoke(cz, object);
    }

    private Object getFramebuffer(Object minecraft) throws Exception {
        Method method = minecraft.getClass().getDeclaredMethod("func_147110_a", new Class[0]);
        method.setAccessible(true);
        return method.invoke(minecraft, new Object[0]);
    }

    private int getInteger(Object framebuffer, String fieldName) throws Exception {
        Field field = framebuffer.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(framebuffer);
    }

    private int gl11Int(String fieldName) {
        try {
            Class<?> cz = Class.forName("org.lwjgl.opengl.GL11");
            Field field = cz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int gl12Int(String fieldName) {
        try {
            Class<?> cz = Class.forName("org.lwjgl.opengl.GL12");
            Field field = cz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Class getIntBufferClazz() throws Exception {
        return Class.forName("java.nio.IntBuffer");
    }

    private void gl11Invoke(String methodName, Class<?>[] params, Object ... args) throws Exception {
        Class<?> cz = Class.forName("org.lwjgl.opengl.GL11");
        Method method = cz.getDeclaredMethod(methodName, params);
        method.setAccessible(true);
        method.invoke(null, args);
    }

    private boolean isFramebufferEnabled() throws Exception {
        Class<?> cz = Class.forName("net.minecraft.client.renderer.OpenGlHelper");
        Method method = cz.getDeclaredMethod("func_148822_b", new Class[0]);
        method.setAccessible(true);
        return (Boolean)method.invoke(null, new Object[0]);
    }

    private Object createIntBuffer(int size) throws Exception {
        Class<?> clazz = Class.forName("org.lwjgl.BufferUtils");
        Method method = clazz.getDeclaredMethod("createIntBuffer", Integer.TYPE);
        method.setAccessible(true);
        return method.invoke(null, size);
    }

    private void intBufferGet(Object intBuffer, int[] values) throws Exception {
        Method method = intBuffer.getClass().getSuperclass().getDeclaredMethod("get", int[].class);
        method.setAccessible(true);
        method.invoke(intBuffer, new Object[]{values});
    }

    private Object createBufferedImage(int width, int height, int type) throws Exception {
        Class<?> clazz = Class.forName("java.awt.image.BufferedImage");
        Constructor<?> constructor = clazz.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
        return constructor.newInstance(width, height, type);
    }

    private Method getRGBMethod1(Object bufferedImage) throws Exception {
        Method method = bufferedImage.getClass().getDeclaredMethod("setRGB", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, int[].class, Integer.TYPE, Integer.TYPE);
        method.setAccessible(true);
        return method;
    }

    private Method getRGBMethod2(Object bufferedImage) throws Exception {
        Method method = bufferedImage.getClass().getDeclaredMethod("setRGB", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        method.setAccessible(true);
        return method;
    }

    private void setRGB(Object bufferedImage, Method method, int a, int b, int c, int d, int[] e, int f, int g) throws Exception {
        method.invoke(bufferedImage, a, b, c, d, e, f, g);
    }

    private void setRGB(Object bufferedImage, Method method, int a, int b, int c) throws Exception {
        method.invoke(bufferedImage, a, b, c);
    }

    private Object createGraphics(Object bufferedImage) throws Exception {
        Method method = bufferedImage.getClass().getDeclaredMethod("createGraphics", new Class[0]);
        method.setAccessible(true);
        return method.invoke(bufferedImage, new Object[0]);
    }

    private Object resize(Object img, int newW, int newH) throws Exception {
        Class<?> cz = Class.forName("java.awt.Image");
        Method scaledInstance = cz.getDeclaredMethod("getScaledInstance", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        scaledInstance.setAccessible(true);
        Object scaled = scaledInstance.invoke(img, newW, newH, 4);
        Object newImage = this.createBufferedImage(newW, newH, 2);
        Object g2d = this.createGraphics(newImage);
        Class<?> imageClazz = Class.forName("java.awt.Image");
        Class<?> imageObserverClazz = Class.forName("java.awt.image.ImageObserver");
        Class<?> graph = Class.forName("java.awt.Graphics");
        Method drawImageMethod = graph.getDeclaredMethod("drawImage", imageClazz, Integer.TYPE, Integer.TYPE, imageObserverClazz);
        drawImageMethod.setAccessible(true);
        drawImageMethod.invoke(g2d, scaled, 0, 0, null);
        Method dispose = g2d.getClass().getDeclaredMethod("dispose", new Class[0]);
        dispose.setAccessible(true);
        dispose.invoke(g2d, new Object[0]);
        return newImage;
    }

    public void getScreenshot(Callback cb) {
        try {
            Object mc = this.getMinecraft();
            Runnable rn = () -> {
                try {
                    Object img;
                    int height;
                    int width;
                    Object buffer = this.getFramebuffer(mc);
                    int framebufferTextureWidth = this.getInteger(buffer, "field_147622_a");
                    int framebufferTextureHeight = this.getInteger(buffer, "field_147620_b");
                    int framebufferWidth = this.getInteger(buffer, "field_147621_c");
                    int framebufferHeight = this.getInteger(buffer, "field_147618_d");
                    int framebufferTexture = this.getInteger(buffer, "field_147617_g");
                    if (this.isFramebufferEnabled()) {
                        width = this.getInteger(buffer, "field_147622_a");
                        height = this.getInteger(buffer, "field_147620_b");
                    } else {
                        width = this.getInteger(mc, "field_71443_c");
                        height = this.getInteger(mc, "field_71440_d");
                    }
                    int pixels = width * height;
                    Object buff = this.createIntBuffer(pixels);
                    int[] values = new int[pixels];
                    this.gl11Invoke("glPixelStorei", new Class[]{Integer.TYPE, Integer.TYPE}, this.gl11Int("GL_PACK_ALIGNMENT"), 1);
                    this.gl11Invoke("glPixelStorei", new Class[]{Integer.TYPE, Integer.TYPE}, this.gl11Int("GL_UNPACK_ALIGNMENT"), 1);
                    if (this.isFramebufferEnabled()) {
                        this.gl11Invoke("glBindTexture", new Class[]{Integer.TYPE, Integer.TYPE}, this.gl11Int("GL_TEXTURE_2D"), framebufferTexture);
                        this.gl11Invoke("glGetTexImage", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, this.getIntBufferClazz()}, this.gl11Int("GL_TEXTURE_2D"), 0, this.gl12Int("GL_BGRA"), this.gl12Int("GL_UNSIGNED_INT_8_8_8_8_REV"), buff);
                    } else {
                        this.gl11Invoke("glReadPixels", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, this.getIntBufferClazz()}, 0, 0, width, height, this.gl12Int("GL_BGRA"), this.gl12Int("GL_UNSIGNED_INT_8_8_8_8_REV"), buff);
                    }
                    this.intBufferGet(buff, values);
                    this.fillTexture(values, width, height);
                    if (this.isFramebufferEnabled()) {
                        int diff;
                        img = this.createBufferedImage(framebufferWidth, framebufferHeight, 1);
                        Method rgb1 = this.getRGBMethod2(img);
                        for (int i = diff = framebufferTextureHeight - framebufferHeight; i < framebufferTextureHeight; ++i) {
                            for (int j = 0; j < framebufferWidth; ++j) {
                                this.setRGB(img, rgb1, j, i - diff, values[i * framebufferTextureWidth + j]);
                            }
                        }
                        int newW = framebufferWidth;
                        int newH = framebufferHeight;
                        if (newW > 720) {
                            newW = 720;
                            newH = (int)((double)framebufferHeight / (double)framebufferWidth * 720.0);
                        }
                        img = this.resize(img, newW, newH);
                    } else {
                        img = this.createBufferedImage(width, height, 1);
                        Method rgb2 = this.getRGBMethod1(img);
                        this.setRGB(img, rgb2, 0, 0, width, height, values, 0, width);
                        int newW = width;
                        int newH = height;
                        if (newW > 720) {
                            newW = 720;
                            newH = (int)((double)height / (double)width * 720.0);
                        }
                        img = this.resize(img, newW, newH);
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    this.write(img, "png", baos);
                    cb.callback(baos.toByteArray());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            };
            this.addScheduledTask(mc, rn);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void write(Object bufferedImage, Object format, Object output) throws Exception {
        Class<?> clazz = Class.forName("javax.imageio.ImageIO");
        Class<?> renderedImageClazz = Class.forName("java.awt.image.RenderedImage");
        Class<?> outputStreamClazz = Class.forName("java.io.OutputStream");
        Method write = clazz.getMethod("write", renderedImageClazz, String.class, outputStreamClazz);
        write.invoke(null, bufferedImage, format, output);
    }

    private void fillTexture(int[] array, int width, int height) {
        int[] aint1 = new int[width];
        int k = height / 2;
        for (int l = 0; l < k; ++l) {
            System.arraycopy(array, l * width, aint1, 0, width);
            System.arraycopy(array, (height - 1 - l) * width, array, l * width, width);
            System.arraycopy(aint1, 0, array, (height - 1 - l) * width, width);
        }
    }
}
