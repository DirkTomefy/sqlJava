package SelectAST.base.function.operand.other;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.operand.BinaryOp;

public enum ArithmeticOp implements BinaryOp {
    ADD,
    MIN,
    MUL,
    DIV;

    @Override
    public double evalBySelf(Expression left, Expression rigth) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evalBySelf'");
    }


}
