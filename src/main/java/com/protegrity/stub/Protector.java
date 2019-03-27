package com.protegrity.stub;

public class Protector {
    public static Protector getProtector() {
        return new Protector();
    }

    public com.protegrity.stub.SessionObject createSession(String queryUser) {
        return new com.protegrity.stub.SessionObject(queryUser);

    }

    public void protect(com.protegrity.stub.SessionObject session, String dataElement, String[] inputStringArray, byte[][] protectByteArray ) {
        System.out.println("XXX Protect User = "+session.queryUser);
        System.out.println("DataElement = "+dataElement);
        System.out.println("Source value="+inputStringArray[0]);
        protectByteArray[0] = new byte[inputStringArray[0].length()+4];
        System.arraycopy(inputStringArray[0].getBytes(), 0, protectByteArray[0], 0, inputStringArray[0].length());
        System.arraycopy("-enc".getBytes(), 0, protectByteArray[0],inputStringArray[0].length(),4);


    }
}
