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

//import com.protegrity.ap.java.*;

import javax.inject.Inject;

import com.dremio.exec.expr.annotations.Workspace;
import com.dremio.exec.record.VectorAccessibleComplexWriter;
import com.dremio.exec.record.VectorContainer;
import com.dremio.sabot.exec.context.BufferManagerImpl;
import com.dremio.sabot.exec.context.ContextInformation;
import com.protegrity.stub.Protector;
import com.protegrity.stub.SessionObject;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.BufferManager;
import org.apache.arrow.vector.holders.VarCharHolder;

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.FunctionTemplate.NullHandling;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Param;

import io.netty.buffer.ArrowBuf;

import java.nio.charset.Charset;

import static jdk.nashorn.internal.objects.Global.print;

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
        session = api.createSession( queryUser);  //TODO: Assuming this is really the Query user, the docs have it as "policy user".
        byte[] tokBytes = new byte[token.end];
        token.buffer.getBytes(0,tokBytes,0, token.end);
        field_token = new String(tokBytes);
    }



    @Override
    public void eval() {
        String[] inputStringArray      = new String[1];
        byte[][] protectByteArray      = new byte[1][];
        // TODO: Assuming that "data element" in the docs is just the field type indicator, which is the 'token' parameter

        byte[] valBytes = new byte[val.end];
        val.buffer.getBytes(0,valBytes,0, val.end);
        inputStringArray[0] = new String(valBytes);
        api.protect( session, field_token, inputStringArray, protectByteArray );
        out.buffer = buffer = buffer.reallocIfNeeded(protectByteArray[0].length);
        out.buffer = buffer.setBytes(0, protectByteArray[0],0,protectByteArray[0].length);
    }



}
