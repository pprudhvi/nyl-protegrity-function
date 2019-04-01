package com.dremio.udf;/*
 * Copyright (C) 2017-2018 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//import com.protegrity.ap.java.*;

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Param;
import com.dremio.exec.expr.annotations.Workspace;
import com.dremio.sabot.exec.context.ContextInformation;
import com.protegrity.stub.Protector;
import com.protegrity.stub.SessionObject;
import io.netty.buffer.ArrowBuf;
import org.apache.arrow.vector.holders.VarCharHolder;

import javax.inject.Inject;
// Dremio Function: protect("<Column Name with unencrypted values>","{Token})
@FunctionTemplate(
        name = "protect",
        scope = FunctionTemplate.FunctionScope.SIMPLE,
        nulls = FunctionTemplate.NullHandling.NULL_IF_NULL,
        isDynamic = true)
public class Protect implements SimpleFunction {

    @Inject
    ArrowBuf buffer;

    @Param
    VarCharHolder val;  //The column value to encrypt

    @Param
    VarCharHolder token; // The column token to specify the type of encryption

    @Output
    VarCharHolder out; // The buffer containing the output to the column

    @Workspace
    String queryUser;

    @Workspace
    SessionObject session;

    @Workspace
    Protector api;

    @Workspace
    String field_token;


    @Inject
    ContextInformation contextInfo;
    public void setup() {
        queryUser = contextInfo.getQueryUser();
        api = Protector.getProtector();
        session = api.createSession(queryUser);
        byte[] tokBytes = new byte[token.end];
        token.buffer.getBytes(0,tokBytes,0,token.end);
        field_token = new String(tokBytes);

    }




    @Override
    public void eval() {
        String[] inputStringArray      = new String[1];
        byte[][] protectByteArray      = new byte[1][];

        final int finalLength = val.end - val.start;
        System.out.println("TEST: "+finalLength+" start="+val.start+" end="+val.end);

        byte[] data = new byte[finalLength];
        val.buffer.getBytes(val.start, data, 0, finalLength);
        inputStringArray[0] = new String(data);

        api.protect(session, field_token, inputStringArray, protectByteArray);
        out.buffer = buffer = buffer.reallocIfNeeded(protectByteArray[0].length);
        out.start = 0;
        out.end = finalLength;

        System.out.println("TEST: "+finalLength+" start="+val.start+" end="+val.end);
        System.out.println("Val = "+new String(data));

        val.buffer.getBytes(val.start, out.buffer, 0, finalLength);
    }



}

