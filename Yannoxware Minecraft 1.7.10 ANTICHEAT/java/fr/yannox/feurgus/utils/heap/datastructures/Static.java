package fr.yannox.feurgus.utils.heap.datastructures;

import fr.yannox.feurgus.utils.heap.datastructures.Value;

public class Static {
    public long staticFieldNameStringId;
    public Value<?> value;

    public Static(long staticFieldNameStringId, Value<?> value) {
        this.staticFieldNameStringId = staticFieldNameStringId;
        this.value = value;
    }
}
