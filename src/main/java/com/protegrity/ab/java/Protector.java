package com.protegrity.ab.java;


public class Protector {
    public static com.protegrity.ab.java.Protector getProtector() {
        return new com.protegrity.ab.java.Protector();
    }

    public SessionObject createSession(String queryUser) {
        return new com.protegrity.ab.java.SessionObject(queryUser);

    }

    public void protect(SessionObject session, String dataElement, String[] inputStringArray, byte[][] protectByteArray ) {
        System.out.println("XXX Protect User = "+session.queryUser);
        System.out.println("DataElement = "+dataElement);
        System.out.println("Source value="+inputStringArray[0]);
        protectByteArray[0] = ("Protected field-"+inputStringArray[0]).getBytes();
//        protectByteArray[0] = null;
        System.out.println("Protected value ="+(new String(protectByteArray[0])));
    }

    public void unprotect(SessionObject session, String dataElement, byte[][] unprotectArray , String[] protectedStringArray) {
        System.out.println("### UnProtect User = "+session.queryUser);
        System.out.println("DataElement = "+dataElement);
        unprotectArray[0]="Unprotected string".getBytes();
        System.out.println("ProtectedString="+(new String(unprotectArray[0])));

    }
}
