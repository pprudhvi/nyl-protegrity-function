package com.dremio.udf;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.Param;
import com.dremio.exec.expr.annotations.Output;
import org.apache.arrow.vector.holders.Float8Holder;

public class AddStuff {

    @FunctionTemplate(
            name = "addstuff",
            scope = FunctionTemplate.FunctionScope.SIMPLE,
            nulls = FunctionTemplate.NullHandling.NULL_IF_NULL)
    public static class FloatFloatAdd implements SimpleFunction {
        @Param
        Float8Holder in1;
        @Param
        Float8Holder in2;
        @Output
        Float8Holder out;
        public void setup() {
        }
        public void eval() {
            out.value = in1.value + in2.value;
        }
    }
}
