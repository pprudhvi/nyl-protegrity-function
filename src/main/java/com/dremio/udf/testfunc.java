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
import io.netty.buffer.ArrowBuf;
import org.apache.arrow.vector.holders.VarCharHolder;

import javax.inject.Inject;

@FunctionTemplate(
    name = "test",
    nulls = NullHandling.NULL_IF_NULL)
public class testfunc implements SimpleFunction {

  @Inject ArrowBuf buffer;

  @Param VarCharHolder val;
  @Output VarCharHolder out;

  @Override
  public void setup() {
  }

  @Override
  public void eval() {
    final int finalLength = val.end - val.start;
    byte[] data = new byte[finalLength];
    val.buffer.getBytes(val.start, data, 0, finalLength);
    String value = new String(data);
    out.buffer = buffer = buffer.reallocIfNeeded(finalLength);
    out.start = 0;
    out.end = finalLength;
    System.out.println("TEST: "+finalLength+" start="+val.start+" end="+val.end);

    System.out.println("Val = "+new String(data));
    val.buffer.getBytes(val.start, out.buffer, 0, finalLength);

  }
}
