package com.dremio.udf;

import com.dremio.exec.context.AdditionalContext;
import com.dremio.sabot.exec.context.ContextInformation;
import io.netty.buffer.ArrowBuf;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.BufferManager;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.holders.VarCharHolder;


public class UDF_Test_Framework {
    public static void setupProtectTestEnv(Protect t,String unprotectedString, String token) {
        t.buffer = createArrowBuf(256);
        t.token = createAndAllocate(token);
        t.val = createAndAllocate(unprotectedString);
        t.out = createAndAllocate("                               ");
        t.contextInfo = getContext();
    }
    public static void setupUnprotectEnv(UnProtect unprot, String protectedVal, String token) {
        unprot.buffer = createArrowBuf(25);
        unprot.token = createAndAllocate(token);
        unprot.val = createAndAllocate(protectedVal);
        unprot.out = createAndAllocate("");
        unprot.contextInfo = getContext();
    }

    public static VarCharHolder createAndAllocate(String val) {
        VarCharHolder t = new VarCharHolder();
        t.buffer = createArrowBuf(val.length());
        t.buffer.setBytes(0,val.getBytes(),0,val.length());
        t.start = 0;
        t.end = val.length();
        return t;
    }

    private static ArrowBuf createArrowBuf(int maxLen) {
        RootAllocator allocator = new RootAllocator(256);
        BufferAllocator bm = allocator.newChildAllocator("Child1",0,512);
        return bm.buffer(maxLen);
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
