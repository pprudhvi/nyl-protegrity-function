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
import com.dremio.sabot.exec.context.ContextInformation;
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

    @Inject
    ArrowBuf userBuffer;

    @Param
    VarCharHolder val;

    @Param
    VarCharHolder token;

    @Output
    VarCharHolder out;

    @Inject
    ArrowBuf user;
//    Protector api;
    @Workspace
    int queryUserBytesLength;

    @Inject
    ContextInformation contextInfo;
    public void setup() {
        final byte[] queryUserNameBytes = contextInfo.getQueryUser().getBytes();
        userBuffer = userBuffer.reallocIfNeeded(queryUserNameBytes.length);
        queryUserBytesLength = queryUserNameBytes.length;
        userBuffer.setBytes(0, queryUserNameBytes);
    }



    @Override
    public void eval() {
/*      SessionObject session = api.createSession( val.buffer );
        api.protect( session, "data element", inputStringArray, protectByteArray );
        api.unprotect( session, "data element", protectByteArray, unprotectStringArray );*/

        final int bytesValArg = val.end - val.start;
        final int bytesTokenArg = token.end - token.start;
        final int finalLength = bytesValArg + bytesTokenArg + queryUserBytesLength;

        out.buffer = buffer = buffer.reallocIfNeeded(finalLength);
        out.start = 0;
        out.end = finalLength;

        val.buffer.getBytes(val.start, out.buffer, 0, bytesValArg);
        token.buffer.getBytes(token.start, out.buffer, bytesValArg, bytesTokenArg);
        userBuffer.getBytes(0, out.buffer, bytesTokenArg, finalLength);


    }
/*
    CREATE TEMPORARY

    MACRO protect_medid(val string) case
    val rlike '[a-zA-Z0-9]{3,}'when true
    then protegrity.

    ptyprotectStr(val, 'TE_AN_P_SL13_L0R0_MEDID') when false
    then val
    end;
    CREATE TEMPORARY

    MACRO unprotect_medid(val string) case
    val rlike '[a-zA-Z0-9]{3,}'when true
    then protegrity.

    ptyUnprotectStr(val, 'TE_AN_P_SL13_L0R0_MEDID') when false
    then val
    end;*/


}

