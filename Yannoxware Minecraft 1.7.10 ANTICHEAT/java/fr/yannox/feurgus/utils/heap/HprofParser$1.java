package fr.yannox.feurgus.utils.heap;

import fr.yannox.feurgus.utils.heap.datastructures.Type;

class HprofParser$1 {
    static final int[] $SwitchMap$utils$heap$datastructures$Type;

    static {
        $SwitchMap$utils$heap$datastructures$Type = new int[Type.values().length];
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.OBJ.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.BOOL.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.CHAR.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.FLOAT.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.DOUBLE.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.BYTE.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.SHORT.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.INT.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            HprofParser$1.$SwitchMap$utils$heap$datastructures$Type[Type.LONG.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
