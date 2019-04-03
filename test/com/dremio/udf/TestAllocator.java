package com.dremio.udf;

import io.netty.buffer.ArrowBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.arrow.memory.AllocationListener;
import org.apache.arrow.memory.AllocationReservation;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.BufferManager;

public class TestAllocator implements BufferAllocator {
    private BufferAllocator actual = null;

    // stats
    int numAllocs;

    TestAllocator(BufferAllocator actual) {
        this.actual = actual;
    }

    int getNumAllocs() {
        return numAllocs;
    }

    void resetNumAllocs() {
        numAllocs = 0;
    }

    @Override
    public ArrowBuf buffer(int i) {
        numAllocs++;
        return actual.buffer(i);
    }

    @Override
    public ArrowBuf buffer(int i, BufferManager bufferManager) {
        numAllocs++;
        return actual.buffer(i, bufferManager);
    }

    @Override
    public ByteBufAllocator getAsByteBufAllocator() {
        return actual.getAsByteBufAllocator();
    }

    @Override
    public BufferAllocator newChildAllocator(String s, long l, long l1) {
        return actual.newChildAllocator(s, l, l1);
    }

    @Override
    public BufferAllocator newChildAllocator(String s, AllocationListener listener, long l, long l1) {
        return actual.newChildAllocator(s, listener, l, l1);
    }

    @Override
    public void close() {
        actual.close();
    }

    @Override
    public long getAllocatedMemory() {
        return actual.getAllocatedMemory();
    }

    @Override
    public long getLimit() {
        return actual.getLimit();
    }

    @Override
    public long getInitReservation() {
        return actual.getInitReservation();
    }

    @Override
    public void setLimit(long l) {
        actual.setLimit(l);
    }

    @Override
    public long getPeakMemoryAllocation() {
        return actual.getPeakMemoryAllocation();
    }

    @Override
    public long getHeadroom() {
        return actual.getHeadroom();
    }

    @Override
    public AllocationReservation newReservation() {
        return actual.newReservation();
    }

    @Override
    public ArrowBuf getEmpty() {
        return actual.getEmpty();
    }

    @Override
    public String getName() {
        return actual.getName();
    }

    @Override
    public boolean isOverLimit() {
        return actual.isOverLimit();
    }

    @Override
    public String toVerboseString() {
        return actual.toVerboseString();
    }

    @Override
    public void assertOpen() {
        actual.assertOpen();
    }
}

