package SelectAST.base.function.operand;


import java.util.Vector;

import SelectAST.base.function.expr.Expression;
import SelectAST.err.EvalErr;
import about.Individual;

public interface BinaryOp {
    public boolean applyByBool(boolean left, boolean right) throws EvalErr;

    public Object applyByCtx(Individual row, Vector<String> fieldName, Expression left, Expression right) throws EvalErr;

}
