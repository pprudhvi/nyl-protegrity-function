package com.dremio.udf;

import static org.junit.jupiter.api.Assertions.*;

class ProtectTest {

    Protect t;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        t = new Protect();
    }

    @org.junit.jupiter.api.Test
    void setup_simple() {
        UDF_Test_Framework.setupProtectTestEnv(t,"MyUnprotected String", "ABC");
        t.setup();
    }
    @org.junit.jupiter.api.Test
    void setup_empty_token() {
        UDF_Test_Framework.setupProtectTestEnv(t,"MyUnprotected String", "");
        try {
            t.setup();
            fail("Runtime exception not caught");
        } catch(RuntimeException e) {

        }

    }

    @org.junit.jupiter.api.Test
    void eval_simple() {
        String testString = "MyUnprotected String";
        UDF_Test_Framework.setupProtectTestEnv(t,testString, "ABC");
        t.setup();
        t.eval();
        String result = UDF_Test_Framework.getStringFromArrowBuf(t.out.buffer);
        assertEquals(("Protected field"+testString),result,"Not Outputting the correct string");

    }
}