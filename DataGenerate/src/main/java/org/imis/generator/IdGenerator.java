package org.imis.generator;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private AtomicInteger idGen = new AtomicInteger();

    public int getNextId(){
        return idGen.getAndIncrement();
    }
}
