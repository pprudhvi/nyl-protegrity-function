package com.protegrity.stub;

public class Protector {
    public static Protector getProtector() {
        return new Protector();
    }

    public SessionObject createSession(String queryUser) {
        return new SessionObject(queryUser);

    }

    public void protect(SessionObject session, String dataElement, String[] inputStringArray, byte[][] protectByteArray ) {
    }
}
