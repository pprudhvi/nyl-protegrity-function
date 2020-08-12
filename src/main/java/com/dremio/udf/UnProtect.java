/*
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
package com.dremio.udf;

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.FunctionTemplate.NullHandling;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Param;
import com.dremio.exec.expr.annotations.Workspace;
import com.dremio.sabot.exec.context.ContextInformation;
import org.apache.arrow.memory.ArrowBuf;
import org.apache.arrow.vector.holders.VarCharHolder;

import javax.inject.Inject;

// Dremio Function: unprotect("<Column Name with encrypted values>","{Token})
@FunctionTemplate(
        name = "unprotect",
        scope = FunctionTemplate.FunctionScope.SIMPLE,
        nulls = NullHandling.NULL_IF_NULL)
public class UnProtect implements SimpleFunction {

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
    com.protegrity.ap.java.SessionObject session;

    @Workspace
    com.protegrity.ap.java.Protector api;

    @Workspace
    String field_token;

    @Inject
    ContextInformation contextInfo;

    public void setup() {
        System.out.println("Commencing UnProtect Query");
        queryUser = contextInfo.getQueryUser();

        api = com.protegrity.ap.java.Protector.getProtector();
        if(api == null) {
            System.err.println("Unable to access Protegrity Protector");
            throw new RuntimeException("Protector.getProtector unable to get an instance");
        }

        session = api.createSession(queryUser);

        if(session == null){
            System.err.println("Unable to establish a Protegrity Session using user "+queryUser);
            throw new RuntimeException("Unable to establish a Protegrity Session using user "+queryUser);
        }

        byte[] tokBytes = new byte[token.end];
        if ((token.end - token.start) <=0 || token.buffer == null) {
            System.err.println("No token found");
            throw new RuntimeException("No Token Found in protect() function");
        }
        token.buffer.getBytes(0,tokBytes,0,token.end);
        field_token = new String(tokBytes);
    }


    public void eval() {
        String[] unprotectStringArray      = new String[1];
        byte[][] unprotectByteArray      = new byte[1][];

        int finalLength = val.end - val.start;

        unprotectByteArray[0] = new byte[finalLength];
        val.buffer.getBytes(val.start, unprotectByteArray[0], 0, finalLength);

        api.unprotect(session, field_token, unprotectByteArray, unprotectStringArray);

        finalLength = unprotectByteArray[0].length;
        out.buffer = buffer = buffer.reallocIfNeeded(finalLength);
        out.start = 0;
        out.end = finalLength;
        out.buffer.setBytes(0, unprotectByteArray[0], 0, finalLength);
    }




}

