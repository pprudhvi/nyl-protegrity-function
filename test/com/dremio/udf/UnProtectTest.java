package com.dremio.udf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnProtectTest {
    UnProtect up;

    @BeforeEach
    void setUp() {
        up =  new UnProtect();
    }

    @org.junit.jupiter.api.Test
    void setup_simple() {
        UDF_Test_Framework.setupUnprotectEnv(up,"MyUnprotected String", "ABC");
        up.setup();
    }
    @org.junit.jupiter.api.Test
    void setup_empty_token() {
        UDF_Test_Framework.setupUnprotectEnv(up,"MyUnprotected String", "");
        try {
            up.setup();
            fail("Runtime exception not caught");
        } catch(RuntimeException e) {

        }

    }

    @org.junit.jupiter.api.Test
    void eval_simple() {
        String testString = "MyUnprotected String";
        UDF_Test_Framework.setupUnprotectEnv(up,testString, "ABC");
        up.setup();
        up.eval();
        String result = UDF_Test_Framework.getStringFromArrowBuf(up.out.buffer);
        assertEquals(("Protected field"+testString),result,"Not Outputting the correct string");

    }
}