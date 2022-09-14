package fr.yannox.feurgus.utils;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import fr.yannox.feurgus.utils.CWMouseHook;

interface CWMouseHook$Fiakos
extends WinUser.HOOKPROC {
    public WinDef.LRESULT callback(int var1, WinDef.WPARAM var2, CWMouseHook.MOUSEHOOKSTRUCT var3);
}
