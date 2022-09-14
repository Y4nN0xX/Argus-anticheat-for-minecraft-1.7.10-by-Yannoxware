package fr.yannox.feurgus.utils;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinDef;

public class CWMouseHook$MOUSEHOOKSTRUCT
extends Structure {
    public WinDef.POINT pt;
    public WinDef.HWND hwnd;
    public int wHitTestCode;
    public BaseTSD.ULONG_PTR dwExtraInfo;

    public static class ByReference
    extends CWMouseHook$MOUSEHOOKSTRUCT
    implements Structure.ByReference {
    }
}
