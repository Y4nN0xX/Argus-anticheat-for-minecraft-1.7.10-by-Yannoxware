package fr.yannox.feurgus.utils;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

public class CWMouseHook$Point
extends Structure {
    public NativeLong x;
    public NativeLong y;

    public class ByReference
    extends CWMouseHook$Point
    implements Structure.ByReference {
    }
}
