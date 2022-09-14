package fr.yannox.feurgus.utils;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

class CWMouseHook$1
implements Runnable {
    CWMouseHook$1() {
    }

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
}
