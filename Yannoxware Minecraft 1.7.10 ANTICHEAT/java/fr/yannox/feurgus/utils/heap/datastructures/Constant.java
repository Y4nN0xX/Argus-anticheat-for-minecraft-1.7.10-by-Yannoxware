package fr.yannox.feurgus.utils.heap.datastructures;

import fr.yannox.feurgus.utils.heap.datastructures.Value;

public class Constant {
    public short constantPoolIndex;
    public Value<?> value;

    public Constant(short constantPoolIndex, Value<?> value) {
        this.constantPoolIndex = constantPoolIndex;
        this.value = value;
    }
}
