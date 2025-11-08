package SelectAST.base.function.operand;

import SelectAST.base.function.expr.Expression;

public interface BinaryOp {
    public double evalBySelf(Expression left,Expression rigth);
}
