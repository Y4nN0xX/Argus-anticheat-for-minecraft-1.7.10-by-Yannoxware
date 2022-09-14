package fr.yannox.feurgus.utils;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class CWMouseHook {
    public final User32 USER32INST = User32.INSTANCE;
    public final Kernel32 KERNEL32INST = Kernel32.INSTANCE;
    public static Fiakos mouseHook;
    public WinUser.HHOOK hhk;
    public Thread thrd;
    public boolean threadFinish = true;
    public boolean isHooked = false;
    public static final int WM_MOUSEMOVE = 512;
    public static final int WM_LBUTTONDOWN = 513;
    public static final int WM_LBUTTONUP = 514;
    public static final int WM_RBUTTONDOWN = 516;
    public static final int WM_RBUTTONUP = 517;
    public static final int WM_MBUTTONDOWN = 519;
    public static final int WM_MBUTTONUP = 520;

    public CWMouseHook() {
        mouseHook = this.hookTheMouse();
        Native.setProtected(true);
    }

    public void unsetMouseHook() {
        this.threadFinish = true;
        if (this.thrd.isAlive()) {
            this.thrd.interrupt();
            this.thrd = null;
        }
        this.isHooked = false;
    }

    public boolean isIsHooked() {
        return this.isHooked;
    }

    public void setMouseHook() {
        this.thrd = new Thread(new Runnable(){

            @Override
            public void run() {
                block4: {
                    try {
                        if (!CWMouseHook.this.isHooked) {
                            CWMouseHook.this.hhk = CWMouseHook.this.USER32INST.SetWindowsHookEx(14, (WinUser.HOOKPROC)mouseHook, (WinDef.HINSTANCE)CWMouseHook.this.KERNEL32INST.GetModuleHandle(null), 0);
                            CWMouseHook.this.isHooked = true;
                            WinUser.MSG msg = new WinUser.MSG();
                            while (CWMouseHook.this.USER32INST.GetMessage(msg, null, 0, 0) != 0) {
                                CWMouseHook.this.USER32INST.TranslateMessage(msg);
                                CWMouseHook.this.USER32INST.DispatchMessage(msg);
                                System.out.print(CWMouseHook.this.isHooked);
                                if (CWMouseHook.this.isHooked) continue;
                                break block4;
                            }
                            break block4;
                        }
                        System.out.println("The Hook is already installed.");
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                        System.err.println("Caught exception in MouseHook!");
                    }
                }
            }
        }, "Named thread");
        this.threadFinish = false;
        this.thrd.start();
    }

    public Fiakos hookTheMouse() {
        return (nCode, wParam, info) -> {
            if (nCode >= 0) {
                System.out.println("MS: " + wParam);
                switch (wParam.intValue()) {
                    case 513: {
                        break;
                    }
                    case 516: {
                        break;
                    }
                    case 519: {
                        break;
                    }
                    case 514: {
                        break;
                    }
                    case 512: {
                        break;
                    }
                }
                if (this.threadFinish) {
                    this.USER32INST.PostQuitMessage(0);
                }
            }
            return this.USER32INST.CallNextHookEx(this.hhk, nCode, wParam, new WinDef.LPARAM(Pointer.nativeValue(info.getPointer())));
        };
    }

    public static class MOUSEHOOKSTRUCT
    extends Structure {
        public WinDef.POINT pt;
        public WinDef.HWND hwnd;
        public int wHitTestCode;
        public BaseTSD.ULONG_PTR dwExtraInfo;

        public static class ByReference
        extends MOUSEHOOKSTRUCT
        implements Structure.ByReference {
        }
    }

    public class Point
    extends Structure {
        public NativeLong x;
        public NativeLong y;

        public class ByReference
        extends Point
        implements Structure.ByReference {
        }
    }

    private static interface Fiakos
    extends WinUser.HOOKPROC {
        public WinDef.LRESULT callback(int var1, WinDef.WPARAM var2, MOUSEHOOKSTRUCT var3);
    }
}
