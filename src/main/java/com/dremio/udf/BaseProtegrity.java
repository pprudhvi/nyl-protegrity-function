package com.dremio.udf;

import com.protegrity.ap.java.*;
import com.protegrity.ap.java.SessionObject;
import org.apache.arrow.vector.holders.VarCharHolder;

public class BaseProtegrity  {

    public  Protector getProtegrityProtector() {
        Protector api;
        try {
            api = com.protegrity.ap.java.Protector.getProtector();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return api;
    }
    public  SessionObject createProtegritySession(Protector api, String queryUser) {
        SessionObject session;
        try {
            session = api.createSession(queryUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return session;
    }
    public  String getToken(VarCharHolder token) {
        byte[] tokBytes = new byte[token.end];
        if ((token.end - token.start) <=0 || token.buffer == null) {
            System.err.println("No token found");
            throw new RuntimeException("No Token Found in protect() function");
        }
        token.buffer.getBytes(0,tokBytes,0,token.end);
        return new String(tokBytes);
    }

    public  String toString(VarCharHolder vc){
        int l = vc.end - vc.start;
        byte[] tokBytes = new byte[l];
        vc.buffer.getBytes(0,tokBytes,vc.start, vc.end);
        String s = new String(tokBytes);
        return(s);
    }

    protected  void writeByteArrayToArrowBuf(byte[][] protectByteArray, VarCharHolder out) {
        int finalLength = protectByteArray[0].length;
        out.buffer = out.buffer.reallocIfNeeded(finalLength);
        out.start = 0;
        out.end = finalLength;
        out.buffer.setBytes(0, protectByteArray[0], 0, finalLength);
    }
}
