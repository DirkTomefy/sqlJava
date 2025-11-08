package SelectAST.base.function.operand.other;

import java.util.Vector;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.operand.BinaryOp;
import about.Individual;

public enum ArithmeticOp implements BinaryOp {
    ADD,
    MIN,
    MUL,
    DIV;

    @Override
    public boolean applyByBool(boolean left, boolean right) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyByBool'");
    }

    @Override
    public Object applyByCtx(Individual row, Vector<String> fieldName, Expression left, Expression right) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyByCtx'");
    }
}
