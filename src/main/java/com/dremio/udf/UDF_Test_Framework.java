package com.dremio.udf;

import com.dremio.exec.context.AdditionalContext;
import com.dremio.sabot.exec.context.ContextInformation;
import io.netty.buffer.ArrowBuf;
import io.netty.buffer.PooledByteBufAllocatorL;
import io.netty.buffer.UnsafeDirectLittleEndian;
import org.apache.arrow.vector.holders.VarCharHolder;

import java.util.concurrent.atomic.AtomicInteger;

public class UDF_Test_Framework {
    public static void setupProtectTestEnv(Protect t) {
        UnsafeDirectLittleEndian emptyUdle = (new PooledByteBufAllocatorL()).allocate(25);
        t.buffer = new ArrowBuf(new AtomicInteger(), null, emptyUdle, null, null,
                0, 25, false);
        t.token = createAndAllocate("ABC");
        t.val = createAndAllocate("MyFieldValue");
        t.out = createAndAllocate("");
        t.contextInfo = getContext();
        System.out.println("Protect Env Setup");
    }
    public static void setupUnprotectEnv(UnProtect unprot) {
        UnsafeDirectLittleEndian emptyUdle = (new PooledByteBufAllocatorL()).allocate(35);
        unprot.buffer = new ArrowBuf(new AtomicInteger(), null, emptyUdle, null, null,
                0, 25, false);
        unprot.token = createAndAllocate("ABC");
        unprot.val = createAndAllocate("MyFieldValue");
        unprot.out = createAndAllocate("");
        unprot.contextInfo = getContext();
        System.out.println("Unprotect Env Setup");
    }

    public static VarCharHolder createAndAllocate(String val) {
        VarCharHolder t = new VarCharHolder();
        UnsafeDirectLittleEndian emptyUdle = (new PooledByteBufAllocatorL()).allocate(val.length());
        emptyUdle.setBytes(0,val.getBytes());
        t.buffer = new ArrowBuf(new AtomicInteger(), null, emptyUdle, null, null,
                0, val.length(), false);
        t.start = 0;
        t.end = val.length();
        System.out.println("T.buffer null"+t.buffer==null);
        String s = getStringFromArrowBuf(t.buffer);
        System.out.println(s);
        return t;
    }

    public static String getStringFromArrowBuf(ArrowBuf b){
        byte[] tokBytes = new byte[b.capacity()];
        b.getBytes(0,tokBytes,0, b.capacity());
        String s = new String(tokBytes);
        return(s);
    }

    public static ContextInformation getContext() {
        ContextInformation contextInfo = new ContextInformation() {
            @Override
            public String getQueryUser() {
                return "mfj";
            }

            @Override
            public String getCurrentDefaultSchema() {
                return null;
            }

            @Override
            public long getQueryStartTime() {
                return 0;
            }

            @Override
            public int getRootFragmentTimeZone() {
                return 0;
            }

            @Override
            public void registerAdditionalInfo(AdditionalContext additionalContext) {

            }

            @Override
            public <T extends AdditionalContext> T getAdditionalInfo(Class<T> aClass) {
                return null;
            }
        };
        return(contextInfo);
    }
}
